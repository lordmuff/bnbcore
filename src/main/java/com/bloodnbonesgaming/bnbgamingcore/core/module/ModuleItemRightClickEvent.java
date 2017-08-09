package com.bloodnbonesgaming.bnbgamingcore.core.module;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMAdditionRegistry;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMDebugHelper;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ObfNameHelper;

import squeek.asmhelper.com.bloodnbonesgaming.bnbgamingcore.ASMHelper;

public class ModuleItemRightClickEvent implements IClassTransformerModule
{
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{
				ObfNameHelper.Classes.ITEMSTACK.getName()
		};
	}

	@Override
	public String getModuleName()
	{
		return "itemRightClickEvent";
	}

	@Override
	public boolean canBeDisabled()
	{
		return true;
	}

	@Override
	public byte[] transform(final String name, final String transformedName, final byte[] bytes)
	{
		final ClassNode classNode = ASMHelper.readClassFromBytes(bytes);

		String methodName = ObfNameHelper.Methods.USEITEMRIGHTCLICK.getName();
		String methodDesc = ObfNameHelper.Methods.USEITEMRIGHTCLICK.getDescriptor();
		final MethodNode method = ASMHelper.findMethodNodeOfClass(classNode, methodName, methodDesc);

		if (method != null)
		{
			ASMDebugHelper.logAttemptingTransform(methodName, transformedName);
			
			if (this.redirectUseItemRightClick(method, transformedName))
			{
				ASMDebugHelper.logSuccessfulTransform(methodName, transformedName);
				
				return ASMHelper.writeClassToBytes(classNode);
			}
		}
		else
		{
			ASMDebugHelper.logNotFound(methodName, transformedName);
		}
		return bytes;
	}

	private boolean redirectUseItemRightClick(final MethodNode method, final String transformedName)
	{
		final AbstractInsnNode target = ASMHelper.find(method.instructions, ObfNameHelper.Methods.ONITEMRIGHTCLICK.toInsnNode(Opcodes.INVOKEVIRTUAL));

		if (target == null)
		{
			ASMDebugHelper.unexpectedMethodInstructionPattern(method.name, transformedName);
			return false;
		}
		final MethodInsnNode replacement = ObfNameHelper.Methods.ONUSEITEMRIGHTCLICK.toInsnNode(Opcodes.INVOKESTATIC);

		method.instructions.set(target, replacement);
		return true;
	}

	@Override
	public void registerAdditions(final ASMAdditionRegistry arg0) {}
}