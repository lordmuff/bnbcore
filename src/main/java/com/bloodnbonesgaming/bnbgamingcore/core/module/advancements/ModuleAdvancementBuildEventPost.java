package com.bloodnbonesgaming.bnbgamingcore.core.module.advancements;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import com.bloodnbonesgaming.bnbgamingcore.core.module.IClassTransformerModule;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMAdditionRegistry;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMDebugHelper;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMUtils;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ObfNameHelper;

import squeek.asmhelper.com.bloodnbonesgaming.bnbgamingcore.ASMHelper;

public class ModuleAdvancementBuildEventPost implements IClassTransformerModule
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
				
				if (this.addAdvancementBuildEventPostHook(method, transformedName))
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
		return "advancementBuildEventPost";
	}

	@Override
	public void registerAdditions(final ASMAdditionRegistry arg0)
	{
	}

	private boolean addAdvancementBuildEventPostHook(final MethodNode method, final String transformedName)
	{
		AbstractInsnNode target = ASMHelper.find(method.instructions, ObfNameHelper.Methods.LOADADVANCEMENTS.toInsnNode(Opcodes.INVOKEVIRTUAL));
		
		if (target == null)
		{
			ASMDebugHelper.unexpectedMethodInstructionPattern(method.name + " post", transformedName);
			return false;
		}
		final InsnList toInject = new InsnList();
		
		toInject.add(ObfNameHelper.Fields.ADVANCEMENTMANAGER_ADVANCEMENTLIST.toInsnNode(Opcodes.GETSTATIC));
		toInject.add(ObfNameHelper.Methods.ONADVANCEMENTBUILDPOST.toInsnNode(Opcodes.INVOKESTATIC));
		
		method.instructions.insertBefore(target, toInject);
		return true;
	}
}