package com.bloodnbonesgaming.bnbgamingcore.core.module;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMAdditionRegistry;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMDebugHelper;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ObfNameHelper;

import squeek.asmhelper.com.bloodnbonesgaming.bnbgamingcore.ASMHelper;

public class ModuleDamageSourceClass implements IClassTransformerModule
{
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{ObfNameHelper.Classes.DAMAGESOURCE.getName()};
	}

	@Override
	public String getModuleName()
	{
		return "buildDamageSourceList";
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

		String methodName = ObfNameHelper.Methods.DAMAGESOURCE_INIT.getName();
		String methodDesc = ObfNameHelper.Methods.DAMAGESOURCE_INIT.getDescriptor();
		final MethodNode method = ASMHelper.findMethodNodeOfClass(classNode, methodName, methodDesc);

		if (method != null)
		{
			ASMDebugHelper.logTransforming(methodName, transformedName);
			
			if (this.injectListCall(method, transformedName))
			{
				return ASMHelper.writeClassToBytes(classNode);
			}
		}
		else
		{
			ASMDebugHelper.logNotFound(methodName, transformedName);
		}
		return bytes;
	}

	private boolean injectListCall(final MethodNode method, final String transformedName)
	{
		final AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, Opcodes.RETURN);

		if (target == null)
		{
			ASMDebugHelper.unexpectedMethodInstructionPattern(method.name, transformedName);
			return false;
		}
		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(ObfNameHelper.Methods.ONINITDAMAGESOURCE.toInsnNode(Opcodes.INVOKESTATIC));

		method.instructions.insertBefore(target, toInject);
		return true;
	}

	@Override
	public void registerAdditions(final ASMAdditionRegistry arg0) {}
}