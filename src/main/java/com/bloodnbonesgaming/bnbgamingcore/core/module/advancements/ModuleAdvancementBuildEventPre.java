package com.bloodnbonesgaming.bnbgamingcore.core.module.advancements;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.bloodnbonesgaming.bnbgamingcore.core.module.IClassTransformerModule;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMAdditionRegistry;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMDebugHelper;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMUtils;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ObfNameHelper;

import squeek.asmhelper.com.bloodnbonesgaming.bnbgamingcore.ASMHelper;

public class ModuleAdvancementBuildEventPre implements IClassTransformerModule
{
	@Override
	public byte[] transform(final String name, final String transformedName, final byte[] basicClass)
	{
		if (ASMUtils.doesClassEqual(transformedName, ObfNameHelper.Classes.ADVANCEMENTMANAGER))
		{
			final ClassNode classNode = ASMHelper.readClassFromBytes(basicClass);
			String methodName = ObfNameHelper.Methods.ADVANCEMENTMANAGERRELOAD.getName();
			String methodDesc = ObfNameHelper.Methods.ADVANCEMENTMANAGERRELOAD.getDescriptor();
			final MethodNode method = ASMHelper.findMethodNodeOfClass(classNode, methodName, methodDesc);

			if (method != null)
			{
				ASMDebugHelper.logAttemptingTransform(methodName, transformedName);
				
				if (this.addAdvancementBuildEventPreHook(method, transformedName))
				{
					ASMDebugHelper.logSuccessfulTransform(methodName, transformedName);
					return ASMHelper.writeClassToBytes(classNode);
				}
			}
			else
			{
				ASMDebugHelper.logNotFound(methodName, transformedName);
			}
		}
		return basicClass;
	}

	@Override
	public boolean canBeDisabled()
	{
		return true;
	}

	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{ObfNameHelper.Classes.ADVANCEMENTMANAGER.getName()};
	}

	@Override
	public String getModuleName()
	{
		return "advancementBuildEventPre";
	}

	@Override
	public void registerAdditions(final ASMAdditionRegistry arg0)
	{
	}

	private boolean addAdvancementBuildEventPreHook(final MethodNode method, final String transformedName)
	{
		AbstractInsnNode target = ASMHelper.find(method.instructions, ObfNameHelper.Methods.LOADADVANCEMENTS.toInsnNode(Opcodes.INVOKEVIRTUAL));
		
		if (target == null)
		{
			ASMDebugHelper.unexpectedMethodInstructionPattern(method.name + " pre", transformedName);
			return false;
		}
		target = ASMHelper.move(target, -2);
		
		if (target == null)
		{
			ASMDebugHelper.unexpectedMethodInstructionPattern(method.name + " pre2", transformedName);
			return false;
		}
		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
		toInject.add(ObfNameHelper.Methods.ONADVANCEMENTBUILDPRE.toInsnNode(Opcodes.INVOKESTATIC));
		
		method.instructions.insertBefore(target, toInject);
		return true;
	}
}