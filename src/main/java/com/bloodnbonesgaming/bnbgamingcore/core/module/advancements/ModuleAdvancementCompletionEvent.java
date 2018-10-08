package com.bloodnbonesgaming.bnbgamingcore.core.module.advancements;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.bloodnbonesgaming.bnbgamingcore.core.module.IClassTransformerModule;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMAdditionRegistry;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMDebugHelper;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMUtils;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ObfNameHelper;

import squeek.asmhelper.com.bloodnbonesgaming.bnbgamingcore.ASMHelper;

public class ModuleAdvancementCompletionEvent implements IClassTransformerModule
{
	@Override
	public byte[] transform(final String name, final String transformedName, final byte[] basicClass)
	{
		if (ASMUtils.doesClassEqual(transformedName, ObfNameHelper.Classes.ADVANCEMENT_PROGRESS))
		{
			final ClassNode classNode = ASMHelper.readClassFromBytes(basicClass);
			String methodName = ObfNameHelper.Methods.ADVANCEMENT_PROGRESS_IS_DONE.getName();
			String methodDesc = ObfNameHelper.Methods.ADVANCEMENT_PROGRESS_IS_DONE.getDescriptor();
			final MethodNode method = ASMHelper.findMethodNodeOfClass(classNode, methodName, methodDesc);

			if (method != null)
			{
				ASMDebugHelper.logAttemptingTransform(methodName, transformedName);
				
				if (this.addAdvancementAdvancementCompletionEventHook(method, transformedName))
				{
					ASMDebugHelper.logSuccessfulTransform(methodName, transformedName);
					return ASMHelper.writeClassToBytes(classNode, 3);
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
		return new String[]{ObfNameHelper.Classes.ADVANCEMENT_PROGRESS.getName()};
	}

	@Override
	public String getModuleName()
	{
		return "advancementCompletionEvent";
	}

	@Override
	public void registerAdditions(final ASMAdditionRegistry arg0)
	{
	}

	private boolean addAdvancementAdvancementCompletionEventHook(final MethodNode method, final String transformedName)
	{
		AbstractInsnNode target = ASMHelper.findFirstInstruction(method);
		
		if (target == null)
		{
			ASMDebugHelper.unexpectedMethodInstructionPattern(method.name, transformedName);
			return false;
		}
		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(ObfNameHelper.Methods.ON_ADVANCEMENT_COMPLETION_EVENT.toInsnNode(Opcodes.INVOKESTATIC));
		toInject.add(new InsnNode(Opcodes.IRETURN));
		
		method.instructions.insertBefore(target, toInject);
		return true;
	}
}