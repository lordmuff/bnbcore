package com.bloodnbonesgaming.bnbgamingcore.core.module;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.commons.Method;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import com.bloodnbonesgaming.bnbgamingcore.core.BNBGamingCorePlugin;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMAdditionHelper;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMAdditionRegistry;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ObfNameHelper;

import squeek.asmhelper.com.bloodnbonesgaming.bnbgamingcore.ASMHelper;
import squeek.asmhelper.com.bloodnbonesgaming.bnbgamingcore.ObfHelper;


public class ModuleASMAdditionHelper implements IClassTransformerModule{

	@Override
	public byte[] transform(final String name, final String transformedName, final byte[] bytes) {
		final Map<String, List<FieldNode>> fieldAdditions = ASMAdditionRegistry.getFieldAdditions();
		final Map<String, List<MethodNode>> methodAdditions = ASMAdditionRegistry.getMethodAdditions();
		final ClassNode cNode = ASMHelper.readClassFromBytes(bytes);
		if(fieldAdditions != null && !fieldAdditions.isEmpty()){
			try {
				MethodNode mNode = ASMHelper.findMethodNodeOfClass(cNode, "get", "(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;");
				if(mNode != null){
					this.transformGet(cNode, mNode, fieldAdditions);
					BNBGamingCorePlugin.log.info("Successfully populated ASMAdditionHelper.get");
				} else {
					BNBGamingCorePlugin.log.error("Failed to find a 'get' method to populate! Things aren't going to work!");
				}

				mNode = ASMHelper.findMethodNodeOfClass(cNode, "set", "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)V");
				if(mNode != null){
					this.transformSet(cNode, mNode, fieldAdditions);
					BNBGamingCorePlugin.log.info("Successfully populated ASMAdditionHelper.set");
				} else {
					BNBGamingCorePlugin.log.error("Failed to find a 'set' method to populate! Things aren't going to work!");
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}

		if(methodAdditions != null && !methodAdditions.isEmpty()){
			final MethodNode mNode = ASMHelper.findMethodNodeOfClass(cNode, "invoke", "(Ljava/lang/Object;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/Object;");
			if(mNode != null){
				this.transformInvoke(cNode, mNode, methodAdditions);
				BNBGamingCorePlugin.log.info("Successfully populated ASMAdditionHelper.invoke");
			} else {
				BNBGamingCorePlugin.log.error("Failed to find an 'invoke' method to populate! Things aren't going to work!");
			}
		}
		return ASMHelper.writeClassToBytesNoDeobf(cNode);
	}

	@Override
	public String[] getClassesToTransform() {
		return new String[] {ObfNameHelper.Classes.ASMADDITIONHELPER.getName()};
	}

	@Override
	public String getModuleName() {
		return "transformASMAdditionHelperClass";
	}

	@Override
	public boolean canBeDisabled() {
		return false;
	}

	/**
	 * Generates a gigantic if-else if block for every field added. The if statement checks for equality and then gets the field. e.g.
	 * <p><code>
	 * if(fieldName.equals("addedField")) <br>
	 * <blockquote>    return ((SomeClass)obj).addedField;</blockquote>
	 * </code></p>
	 * This obviously isn't possible with plain written code. Look at {@link ASMAdditionHelper} to see the empty methods.
	 */
	private void transformGet(final ClassNode cNode, final MethodNode node, final Map<String, List<FieldNode>> fieldAdditions){
		//Remove the old method. We will make a fresh one.
		cNode.methods.remove(node);
		//Create method visitor and generator adaptor
		final MethodVisitor mVis = cNode.visitMethod(node.access, node.name, node.desc, node.signature, node.exceptions.toArray(new String[0]));
		final GeneratorAdapter genA = new GeneratorAdapter(node.access, new Method(node.name, node.desc), mVis);
		genA.visitCode();
		for(final Entry<String, List<FieldNode>> entry:fieldAdditions.entrySet()){
			for(final FieldNode fNode:entry.getValue()){
				//Perform the variable name check
				final Label label = genA.newLabel();
				genA.loadArg(1);
				genA.visitLdcInsn(fNode.name);
				genA.invokeVirtual(Type.getObjectType("java/lang/Object"), new Method("equals", "(Ljava/lang/Object;)Z"));
				genA.ifZCmp(GeneratorAdapter.EQ, label);
				//Cast the obj we are operating on to the field's type
				genA.loadArg(0);
				genA.checkCast(Type.getObjectType(entry.getKey()));
				//Get the value and box if necessary, since our return type is Object
				genA.getField(Type.getObjectType(entry.getKey()), fNode.name, Type.getType(fNode.desc));
				genA.box(Type.getType(fNode.desc));
				//Return value, add label for end of if, and add FRAME_SAME node
				genA.returnValue();
				genA.visitLabel(label);
				mVis.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			}
		}
		//Load null constant and return if all if statements failed.
		genA.push((String) null);
		genA.returnValue();
		//Values retrieved from sample method.
		genA.visitMaxs(2, 3);
		genA.endMethod();
	}

	/**
	 * Generates a gigantic if-else if block for every field added. The if statement checks for equality and then gets the field. e.g.
	 * <p><code>
	 * if(fieldName.equals("addedField")) <br>
	 * <blockquote>    ((SomeClass)obj).addedField = value;</blockquote>
	 * </code></p>
	 * This obviously isn't possible with plain written code. Look at {@link ASMAdditionHelper} to see the empty methods.
	 */
	private void transformSet(final ClassNode cNode, final MethodNode node, final Map<String, List<FieldNode>> fieldAdditions){
		//Remove the old method. We will make a fresh one.
		cNode.methods.remove(node);
		//Create method visitor and generator adaptor
		final MethodVisitor mVis = cNode.visitMethod(node.access, node.name, node.desc, node.signature, node.exceptions.toArray(new String[0]));
		final GeneratorAdapter genA = new GeneratorAdapter(node.access, new Method(node.name, node.desc), mVis);
		genA.visitCode();
		for(final Entry<String, List<FieldNode>> entry:fieldAdditions.entrySet()){
			for(final FieldNode fNode:entry.getValue()){
				//Perform the variable name check
				final Label label = genA.newLabel();
				genA.loadArg(1);
				genA.visitLdcInsn(fNode.name);
				genA.invokeVirtual(Type.getObjectType("java/lang/Object"), new Method("equals", "(Ljava/lang/Object;)Z"));
				genA.ifZCmp(GeneratorAdapter.EQ, label);
				//Cast the obj we are operating on to the field's type
				genA.loadArg(0);
				genA.checkCast(Type.getObjectType(entry.getKey()));
				//Load the value paramter, and unbox it if necessary. The unbox method also adds a cast.
				genA.loadArg(2);
				final Type fieldT = Type.getType(fNode.desc);
				genA.unbox(fieldT);
				//Put the value in the field
				genA.putField(Type.getObjectType(entry.getKey()), fNode.name, Type.getType(fNode.desc));
				//Add label for end of if, and add FRAME_SAME node
				genA.visitLabel(label);
				mVis.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			}
		}
		//Even empty methods that return null require a return node
		genA.returnValue();
		//Values retrieved from sample method.
		genA.visitMaxs(2, 4);
		genA.endMethod();
	}

	/**
	 * Generates a gigantic if-else if block for every method added. The if statement checks for equality and then gets the field. e.g.
	 * <p><code>
	 * if(fieldName.equals("addedMethod")) <br>
	 * <blockquote>    return ((SomeClass)obj).addedMethod();</blockquote>
	 * </code></p>
	 * This obviously isn't possible with plain written code. Look at {@link ASMAdditionHelper} to see the empty methods.
	 */
	private void transformInvoke(final ClassNode cNode, final MethodNode node, final Map<String, List<MethodNode>> methodAdditions){
		//Remove the old method. We will make a fresh one.
		cNode.methods.remove(node);
		//Create method visitor and generator adaptor
		final MethodVisitor mVis = cNode.visitMethod(node.access, node.name, node.desc, node.signature, node.exceptions.toArray(new String[0]));
		final GeneratorAdapter genA = new GeneratorAdapter(node.access, new Method(node.name, node.desc), mVis);
		genA.visitCode();
		for(final Entry<String, List<MethodNode>> entry:methodAdditions.entrySet()){
			for(final MethodNode mNode:entry.getValue()){
				//Perform the variable name check
				final Label label = genA.newLabel();
				genA.loadArg(1);
				genA.visitLdcInsn(mNode.name);
				genA.invokeVirtual(Type.getObjectType("java/lang/Object"), new Method("equals", "(Ljava/lang/Object;)Z"));
				genA.ifZCmp(GeneratorAdapter.EQ, label);
				//Cast the obj we are operating on to the field's type
				genA.loadArg(0);
				genA.checkCast(Type.getObjectType(entry.getKey()));
				//Go through every paramter of the method we are invoking, load it from the 'paramaters' array, and perform a cast.
				final Type[] types = Type.getArgumentTypes(mNode.desc);
				if(types.length != 0) {
					for(int i = 0; i < types.length; i++){
						genA.loadArg(2);
						genA.push(i);
						final String wrapped = this.wrap(types[i].getDescriptor());
						genA.arrayLoad(wrapped == null ? types[i]:Type.getObjectType(wrapped));
						genA.unbox(types[i]);
					}
				}
				//Actually invoke the method, and box the return type if necessary
				genA.invokeVirtual(Type.getObjectType(entry.getKey()), new Method(mNode.name, mNode.desc));
				final Type methodType = Type.getMethodType(mNode.desc);
				genA.box(methodType.getReturnType());
				//Return value, add label for end of if, and add FRAME_SAME node
				genA.returnValue();
				genA.visitLabel(label);
				mVis.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			}
		}
		//Load null constant and return if all if statements failed.
		genA.push((String) null);
		genA.returnValue();
		genA.visitMaxs(2, 4);
		genA.endMethod();
	}

	private MethodInsnNode wrapNode(final String desc)
	{
		final String className = this.wrap(desc);
		if(className == null) {
			return null;
		}
		return new MethodInsnNode(Opcodes.INVOKESTATIC, className, "valueOf", "("+desc+")"+ObfHelper.getDescriptor(className), false);
	}

	private String wrap(final String className){
		if (className.equals("Z")) {
			return "java/lang/Boolean";
		} else if (className.equals("B")) {
			return "java/lang/Byte";
		} else if (className.equals("C")) {
			return "java/lang/Character";
		} else if (className.equals("S")) {
			return "java/lang/Short";
		} else if (className.equals("I")) {
			return "java/lang/Integer";
		} else if (className.equals("J")) {
			return "java/lang/Long";
		} else if (className.equals("F")) {
			return "java/lang/Float";
		} else if (className.equals("D")) {
			return "java/lang/Double";
		}
		return null;
	}

	private MethodInsnNode unwrap(final String desc)
	{
		if (desc.equals("Z")) {
			return new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z", false);
		} else if (desc.equals("B")) {
			return new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/Byte", "byteValue", "()B", false);
		} else if (desc.equals("C")) {
			return new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/Character", "charValue", "()C", false);
		} else if (desc.equals("S")) {
			return new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/Short", "shortValue", "()S", false);
		} else if (desc.equals("I")) {
			return new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I", false);
		} else if (desc.equals("J")) {
			return new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/Long", "longValue", "()J", false);
		} else if (desc.equals("F")) {
			return new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/Float", "floatValue", "()F", false);
		} else if (desc.equals("D")) {
			return new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()D", false);
		}
		return null;
	}

	private String getReturnType(final String desc){
		return desc.substring(desc.indexOf(")")+1);
	}

	private String stripDescToClass(final String desc){
		return desc.replaceFirst("L", "").replace(";", "");
	}

	private AbstractInsnNode getIntInsnNode(final int i){
		switch(i){
		case -1:
			return new InsnNode(Opcodes.ICONST_M1);
		case 0:
			return new InsnNode(Opcodes.ICONST_0);
		case 1:
			return new InsnNode(Opcodes.ICONST_1);
		case 2:
			return new InsnNode(Opcodes.ICONST_2);
		case 3:
			return new InsnNode(Opcodes.ICONST_3);
		case 4:
			return new InsnNode(Opcodes.ICONST_4);
		case 5:
			return new InsnNode(Opcodes.ICONST_5);
		}
		return new IntInsnNode(Opcodes.BIPUSH, i);
	}

	@Override
	public void registerAdditions(final ASMAdditionRegistry registry) {}

}
