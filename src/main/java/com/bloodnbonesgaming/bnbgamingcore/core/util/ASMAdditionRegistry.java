package com.bloodnbonesgaming.bnbgamingcore.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import com.bloodnbonesgaming.bnbgamingcore.core.insn.RedirectedFieldInsnNode;
import com.bloodnbonesgaming.bnbgamingcore.core.insn.RedirectedMethodInsnNode;
import com.bloodnbonesgaming.bnbgamingcore.core.insn.RedirectedMethodVisitor;
import com.bloodnbonesgaming.bnbgamingcore.core.module.IClassTransformerModule;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMAdditionHelper.ASMAdditionHelperWrapper;

/**
 * This is the class that handles the registration and storage of fields and methods that must be added at runtime via ASM.<br>
 * When a transformer that extends {@link BNBGamingClassTransformer} is instantiated, all of its submodules returned by {@link BNBGamingClassTransformer#createModules() createModules}
 *  will be provided with a wrapper for this registry in the method {@link IClassTransformerModule#registerAdditions(ASMAdditionRegistry)} which will most likely be an instance of {@link ASMAdditionRegistryWrapper}.
 *  This wrapper will append a String, usually given by {@link BNBGamingClassTransformer#getName()}, to the beginning of all field and method names. This is done to alleviate inter-mod conflictions.<br>
 *  Note that special classes ({@link RedirectedFieldInsnNode}, {@link RedirectedMethodInsnNode}, and {@link RedirectedMethodVisitor}) are provided to use when making tweaks via ASM.
 *  These will attempt to redirect references to the proper field or method, since the name of the field or method given in {@link IClassTransformerModule#registerAdditions(ASMAdditionRegistry) registerAdditions}
 *  is not the name of the field or method that will be generated.<br> e.g. If you add the field "testField", the actual field generated will have an id appended to it, so referring to it in ASM code can be tricky since you don't know its actual name.
 *   Instead, when referring to this field in ASM code,
 *  you can use a {@link RedirectedFieldInsnNode} and pass the name "testField" without issues. The node will redirect the name to the actual name for you.
 */
public abstract class ASMAdditionRegistry {

	private static final Map<String, List<FieldNode>> fieldAdditions = new HashMap<>();
	private static final Map<String, List<MethodNode>> methodAdditions = new HashMap<>();
	private static final Map<String, ASMAdditionRegistryWrapper> wrappers = new HashMap<>();

	/**
	 * Notifies BNBGamingLib that a field should be added via ASM.
	 * This will cause the field prefixed with the submods name (Gotten via {@link BNBGamingClassTransformer#getName()}) to be generated.
	 * This field can then be interacted with via the {@link ASMAdditionHelper}
	 * @param clazz The class the field should be added to. Correction is done for non-internal class names.
	 * @param node The field to be added. (Note that {@link FieldNode#accept(org.objectweb.asm.ClassVisitor) is called to do the actual addition}}).
	 * @return If the registration was successful.
	 */
	public boolean registerFieldAddition(String clazz, final FieldNode node){
		clazz = clazz.replace('.', '/');
		if(!ASMAdditionRegistry.fieldAdditions.containsKey(clazz)) {
			ASMAdditionRegistry.fieldAdditions.put(clazz, new ArrayList<FieldNode>());
		}
		//TODO check for conflictions
		return ASMAdditionRegistry.fieldAdditions.get(clazz).add(node);
	}

	/**
	 * Notifies BNBGamingLib that a method should be added via ASM.
	 * This will cause the method prefixed with the submods name (Gotten via {@link BNBGamingClassTransformer#getName()}) to be generated.
	 * This field can then be interacted with via the {@link ASMAdditionHelper}
	 * @param clazz The class the method should be added to. Correction is done for non-internal class names.
	 * @param node The method to be added. (Note that {@link MethodNode#accept(org.objectweb.asm.ClassVisitor)} is called to do the actual addition).
	 * @return If the registration was successful.
	 */
	public boolean registerMethodAddition(String clazz, final MethodNode node){
		clazz = clazz.replace('.', '/');
		if(!ASMAdditionRegistry.methodAdditions.containsKey(clazz)) {
			ASMAdditionRegistry.methodAdditions.put(clazz, new ArrayList<MethodNode>());
		}
		//TODO check for conflictions
		return ASMAdditionRegistry.methodAdditions.get(clazz).add(node);
	}

	public abstract String getWrapString();

	/**
	 * Retrieves an already existing {@link ASMAdditionHelperWrapper} or creates one for the given id.
	 * @param id The id to use.
	 * @return The retrieved or created wrapper.
	 */
	public static ASMAdditionRegistryWrapper getWrapper(final String id){
		if(!ASMAdditionRegistry.wrappers.containsKey(id)) {
			ASMAdditionRegistry.wrappers.put(id, new ASMAdditionRegistryWrapper(id));
		}
		return ASMAdditionRegistry.wrappers.get(id);
	}

	public static Map<String, List<FieldNode>> getFieldAdditions() {
		return new HashMap(ASMAdditionRegistry.fieldAdditions);
	}

	public static Map<String, List<MethodNode>> getMethodAdditions() {
		return new HashMap(ASMAdditionRegistry.methodAdditions);
	}

	public static class ASMAdditionRegistryWrapper extends ASMAdditionRegistry{

		private final String wrap;
		private final Map<String, List<FieldNode>> fieldAdditions = new HashMap<>();
		private final Map<String, List<MethodNode>> methodAdditions = new HashMap<>();

		private ASMAdditionRegistryWrapper(final String wrappedName) {
			this.wrap = wrappedName;
		}

		@Override
		public boolean registerFieldAddition(String clazz, final FieldNode node) {
			//Append the id to the front to make the field unique per submod
			node.name = this.wrap+node.name;
			//Store it locally for lookup elsewhere
			clazz = clazz.replace('.', '/');
			if(!this.fieldAdditions.containsKey(clazz)) {
				this.fieldAdditions.put(clazz, new ArrayList<FieldNode>());
			}
			//TODO check for conflictions
			this.fieldAdditions.get(clazz).add(node);
			//Call the super method to store it globally
			return super.registerFieldAddition(clazz, node);
		}

		@Override
		public boolean registerMethodAddition(String clazz, final MethodNode node) {
			//Append the id to the front to make the field unique per submod
			node.name = this.wrap+node.name;
			//Store it locally for lookup elsewhere
			clazz = clazz.replace('.', '/');
			if(!this.methodAdditions.containsKey(clazz)) {
				this.methodAdditions.put(clazz, new ArrayList<MethodNode>());
			}
			//TODO check for conflictions
			this.methodAdditions.get(clazz).add(node);
			//Call the super method to store it globally
			return super.registerMethodAddition(clazz, node);
		}

		@Override
		public String getWrapString() {
			return this.wrap;
		}

		public Map<String, List<FieldNode>> getWrapperFieldAdditions() {
			return new HashMap(this.fieldAdditions);
		}

		public Map<String, List<MethodNode>> getWrapperMethodAdditions() {
			return new HashMap(this.methodAdditions);
		}

	}

}
