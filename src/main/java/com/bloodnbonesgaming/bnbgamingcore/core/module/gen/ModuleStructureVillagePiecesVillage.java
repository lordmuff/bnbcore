package com.bloodnbonesgaming.bnbgamingcore.core.module.gen;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FrameNode;
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

public class ModuleStructureVillagePiecesVillage implements IClassTransformerModule
{
	@Override
	public byte[] transform(final String name, final String transformedName, final byte[] basicClass)
	{
		if (ASMUtils.doesClassEqualOrExtend(transformedName, ObfNameHelper.Classes.STRUCTURE_VILLAGE_PIECES$VILLAGE, basicClass))
		{
			boolean modified = false;
			final ClassNode classNode = ASMHelper.readClassFromBytes(basicClass);
			String methodName = ObfNameHelper.Methods.VILLAGE_REPLACE_AIR_AND_LIQUID_DOWNWARDS.getName();
			String methodDesc = ObfNameHelper.Methods.VILLAGE_REPLACE_AIR_AND_LIQUID_DOWNWARDS.getDescriptor();
			MethodNode method = ASMHelper.findMethodNodeOfClass(classNode, methodName, methodDesc);

			if (method != null)
			{
				ASMDebugHelper.logAttemptingTransform(methodName, transformedName);
				
				if (this.inject(method, transformedName))
				{
					modified = true;
					ASMDebugHelper.logSuccessfulTransform(methodName, transformedName);
				}
			}
			else
			{
				ASMDebugHelper.logNotFound(methodName, transformedName);
			}

			methodName = ObfNameHelper.Methods.VILLAGE_GET_AVERAGE_GROUND_LEVEL.getName();
			methodDesc = ObfNameHelper.Methods.VILLAGE_GET_AVERAGE_GROUND_LEVEL.getDescriptor();
			method = ASMHelper.findMethodNodeOfClass(classNode, methodName, methodDesc);

			if (method != null)
			{
				ASMDebugHelper.logAttemptingTransform(methodName, transformedName);
				
				if (this.injectGetAverageGroundLevel(method, transformedName))
				{
					modified = true;
					ASMDebugHelper.logSuccessfulTransform(methodName, transformedName);
				}
			}
			else
			{
				ASMDebugHelper.logNotFound(methodName, transformedName);
			}
			
			if (modified)
			{
				return ASMHelper.writeClassToBytes(classNode);
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
		return new String[]{ObfNameHelper.Classes.STRUCTURE_VILLAGE_PIECES$VILLAGE.getName()};
	}

	@Override
	public String getModuleName()
	{
		return "transformStructureVillagePiecesVillage";
	}

	@Override
	public void registerAdditions(final ASMAdditionRegistry arg0)
	{
	}
	
	private boolean inject(final MethodNode method, final String transformedName)
	{
		final AbstractInsnNode target = ASMHelper.findFirstInstruction(method);
		
		if (target == null)
		{
			ASMDebugHelper.unexpectedMethodInstructionPattern(method.name, transformedName);
			return false;
		}
		
//		mv.visitVarInsn(ALOAD, 0);
//		mv.visitVarInsn(ALOAD, 1);
//		mv.visitVarInsn(ALOAD, 2);
//		mv.visitVarInsn(ILOAD, 3);
//		mv.visitVarInsn(ILOAD, 4);
//		mv.visitVarInsn(ILOAD, 5);
//		mv.visitVarInsn(ALOAD, 6);
//		mv.visitMethodInsn(INVOKESTATIC, "com/bloodnbonesgaming/bnbgamingcore/events/BNBEventFactory", "onStructureVillageFillBlocksDown", "(Lnet/minecraft/world/gen/structure/StructureVillagePieces$Village;Lnet/minecraft/world/World;Lnet/minecraft/block/state/IBlockState;IIILnet/minecraft/world/gen/structure/StructureBoundingBox;)Z", false);
//		Label l1 = new Label();
//		mv.visitJumpInsn(IFEQ, l1);
//		Label l2 = new Label();
//		mv.visitLabel(l2);
//		mv.visitLineNumber(371, l2);
//		mv.visitInsn(RETURN);
//		mv.visitLabel(l1);
//		mv.visitLineNumber(373, l1);
//		mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
		
		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 2));
		toInject.add(new VarInsnNode(Opcodes.ILOAD, 3));
		toInject.add(new VarInsnNode(Opcodes.ILOAD, 4));
		toInject.add(new VarInsnNode(Opcodes.ILOAD, 5));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 6));
		toInject.add(ObfNameHelper.Methods.ON_STRUCTURE_VILLAGE_FILL_BLOCKS_DOWN_EVENT.toInsnNode(Opcodes.INVOKESTATIC));
		final LabelNode label = new LabelNode();
		toInject.add(new JumpInsnNode(Opcodes.IFEQ, label));
		toInject.add(new InsnNode(Opcodes.RETURN));
		toInject.add(label);
		toInject.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
		
		method.instructions.insertBefore(target, toInject);
		return true;
	}
	
	private boolean injectGetAverageGroundLevel(final MethodNode method, final String transformedName)
	{
		final AbstractInsnNode target = ASMHelper.findFirstInstruction(method);
		
		if (target == null)
		{
			ASMDebugHelper.unexpectedMethodInstructionPattern(method.name, transformedName);
			return false;
		}
		
//		mv.visitVarInsn(ALOAD, 0);
//		mv.visitVarInsn(ALOAD, 1);
//		mv.visitVarInsn(ALOAD, 2);
//		mv.visitMethodInsn(INVOKESTATIC, "com/bloodnbonesgaming/bnbgamingcore/events/BNBEventFactory", "onVillageGetAverageGroundLevel", "(Lnet/minecraft/world/gen/structure/StructureVillagePieces$Village;Lnet/minecraft/world/World;Lnet/minecraft/world/gen/structure/StructureBoundingBox;)I", false);
//		mv.visitVarInsn(ISTORE, 3);
//		Label l1 = new Label();
//		mv.visitLabel(l1);
//		mv.visitLineNumber(155, l1);
//		mv.visitVarInsn(ILOAD, 3);
//		mv.visitInsn(ICONST_M1);
//		Label l2 = new Label();
//		mv.visitJumpInsn(IF_ICMPLT, l2);
//		Label l3 = new Label();
//		mv.visitLabel(l3);
//		mv.visitLineNumber(156, l3);
//		mv.visitVarInsn(ILOAD, 3);
//		mv.visitInsn(IRETURN);
//		mv.visitLabel(l2);
//		mv.visitLineNumber(159, l2);
//		mv.visitFrame(Opcodes.F_APPEND,1, new Object[] {Opcodes.INTEGER}, 0, null);
		
		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 2));
		toInject.add(ObfNameHelper.Methods.ON_VILLAGE_GET_AVERAGE_GROUND_LEVEL_EVENT.toInsnNode(Opcodes.INVOKESTATIC));
		toInject.add(new VarInsnNode(Opcodes.ISTORE, 3));
		toInject.add(new VarInsnNode(Opcodes.ILOAD, 3));
		toInject.add(new InsnNode(Opcodes.ICONST_M1));

		final LabelNode label = new LabelNode();
		toInject.add(new JumpInsnNode(Opcodes.IF_ICMPLT, label));
		toInject.add(new VarInsnNode(Opcodes.ILOAD, 3));
		toInject.add(new InsnNode(Opcodes.IRETURN));
		toInject.add(label);
		toInject.add(new FrameNode(Opcodes.F_APPEND,1, new Object[] {Opcodes.INTEGER}, 0, null));
		
		method.instructions.insertBefore(target, toInject);
		return true;
	}
}
