package com.bloodnbonesgaming.bnbgamingcore.core.module;

import java.util.List;
import java.util.Map;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import com.bloodnbonesgaming.bnbgamingcore.core.BNBGamingCorePlugin;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMAdditionRegistry;

import squeek.asmhelper.com.bloodnbonesgaming.bnbgamingcore.ASMHelper;

public class ModulePerformAdditions implements IClassTransformerModule{

	@Override
	public byte[] transform(final String name, final String transformedName, final byte[] bytes) {
		if(bytes == null || bytes.length == 0) {
			return bytes;
		}
		final Map<String, List<FieldNode>> fieldAdditions = ASMAdditionRegistry.getFieldAdditions();
		final Map<String, List<MethodNode>> methodAdditions = ASMAdditionRegistry.getMethodAdditions();

		final List<FieldNode> fNodes = fieldAdditions == null ? null:fieldAdditions.get(transformedName.replace(".", "/"));
		final List<MethodNode> mNodes = methodAdditions == null ? null:methodAdditions.get(transformedName.replace(".", "/"));
		final ClassNode cNode = ASMHelper.readClassFromBytes(bytes);
		boolean didSomething = false;
		if(fNodes != null && !fNodes.isEmpty()) {
			this.addFields(cNode, fNodes);
			didSomething = true;
		}
		if(mNodes != null && !mNodes.isEmpty()) {
			this.addMethods(cNode, mNodes);
			didSomething = true;
		}
		cNode.visitEnd();

		if (didSomething)
		{
			return ASMHelper.writeClassToBytes(cNode);
		}
		else
		{
			return bytes;
		}
		//return didSomething ? ASMHelper.writeClassToBytes(cNode):bytes;
	}

	private void addFields(final ClassNode node, final List<FieldNode> fNodes){
		BNBGamingCorePlugin.log.info("Found "+fNodes.size()+" fields to add to "+node.name);
		for(final FieldNode fNode:fNodes){
			fNode.accept(node);
		}
	}

	private void addMethods(final ClassNode node, final List<MethodNode> mNodes){
		BNBGamingCorePlugin.log.info("Found "+mNodes.size()+" methods to add to "+node.name);
		for(final MethodNode mNode:mNodes){
			mNode.accept(node);
		}
	}

	@Override
	public String[] getClassesToTransform() {
		return new String[] {"*"};
	}

	@Override
	public String getModuleName() {
		return "performAdditionsModule";
	}

	@Override
	public boolean canBeDisabled() {
		return false;
	}

	@Override
	public void registerAdditions(final ASMAdditionRegistry registry) {}

}
