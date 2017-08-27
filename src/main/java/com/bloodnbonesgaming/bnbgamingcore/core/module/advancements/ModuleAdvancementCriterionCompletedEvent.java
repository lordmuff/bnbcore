package com.bloodnbonesgaming.bnbgamingcore.core.module.advancements;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.bloodnbonesgaming.bnbgamingcore.core.module.IClassTransformerModule;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMAdditionRegistry;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMDebugHelper;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMUtils;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ObfNameHelper;

import squeek.asmhelper.com.bloodnbonesgaming.bnbgamingcore.ASMHelper;

public class ModuleAdvancementCriterionCompletedEvent implements IClassTransformerModule
{
	@Override
	public byte[] transform(final String name, final String transformedName, final byte[] basicClass)
	{
		if (ASMUtils.doesClassEqual(transformedName, ObfNameHelper.Classes.PLAYER_ADVANCEMENTS))
		{
			final ClassNode classNode = ASMHelper.readClassFromBytes(basicClass);
			String methodName = ObfNameHelper.Methods.GRANT_CRITERION.getName();
			String methodDesc = ObfNameHelper.Methods.GRANT_CRITERION.getDescriptor();
			final MethodNode method = ASMHelper.findMethodNodeOfClass(classNode, methodName, methodDesc);

			if (method != null)
			{
				ASMDebugHelper.logAttemptingTransform(methodName, transformedName);
				
				if (this.addAdvancementCriterionCompletedEventHook(method, transformedName))
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
		return new String[]{ObfNameHelper.Classes.PLAYER_ADVANCEMENTS.getName()};
	}

	@Override
	public String getModuleName()
	{
		return "advancementCriterionCompletedEvent";
	}

	@Override
	public void registerAdditions(final ASMAdditionRegistry arg0)
	{
	}

	private boolean addAdvancementCriterionCompletedEventHook(final MethodNode method, final String transformedName)
	{
		AbstractInsnNode target = ASMHelper.find(method.instructions, new InsnNode(Opcodes.IRETURN));
		
		if (target == null)
		{
			ASMDebugHelper.unexpectedMethodInstructionPattern(method.name, transformedName);
			return false;
		}
		target = ASMHelper.move(target, -2);
		
		if (target == null)
		{
			ASMDebugHelper.unexpectedMethodInstructionPattern(method.name + " 2", transformedName);
			return false;
		}
		final InsnList toInject = new InsnList();
//		toInject.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
//		toInject.add(new FrameNode(Opcodes.F_FULL, 5, new Object[]{"net/minecraft/advancements/Advancement", "java/lang/String", Opcodes.INTEGER, "net/minecraft/advancements/AdvancementProgress", Opcodes.INTEGER}, 0, null));
		
		toInject.add(new VarInsnNode(Opcodes.ILOAD, 5));
		final LabelNode label = new LabelNode();
		toInject.add(new JumpInsnNode(Opcodes.IFNE, label));
		
		toInject.add(new VarInsnNode(Opcodes.ILOAD, 3));
		toInject.add(new JumpInsnNode(Opcodes.IFEQ, label));
		
		toInject.add(new LabelNode());
		
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(ObfNameHelper.Fields.PLAYER_ADVANCEMENTS_PLAYER.toInsnNode(Opcodes.GETFIELD));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 4));
		toInject.add(ObfNameHelper.Methods.ON_ADVANCEMENT_CRITERION_COMPLETED.toInsnNode(Opcodes.INVOKESTATIC));
		
		toInject.add(label);
		
		method.instructions.insertBefore(target, toInject);
		return true;
	}
}