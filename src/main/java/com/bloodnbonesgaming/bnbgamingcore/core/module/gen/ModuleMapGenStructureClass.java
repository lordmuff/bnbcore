package com.bloodnbonesgaming.bnbgamingcore.core.module.gen;

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

public class ModuleMapGenStructureClass implements IClassTransformerModule
{
	@Override
	public byte[] transform(final String name, final String transformedName, final byte[] basicClass)
	{
		if (ASMUtils.doesClassEqualOrExtend(transformedName, ObfNameHelper.Classes.MAPGENSTRUCTURE, basicClass))
		{
			boolean modified = false;
			final ClassNode classNode = ASMHelper.readClassFromBytes(basicClass);
			String methodName = ObfNameHelper.Methods.GENERATESTRUCTURE.getName();
			String methodDesc = ObfNameHelper.Methods.GENERATESTRUCTURE.getDescriptor();
			MethodNode method = ASMHelper.findMethodNodeOfClass(classNode, methodName, methodDesc);

			if (method != null)
			{
				ASMDebugHelper.logTransforming(methodName, transformedName);
				modified = modified || this.injectGenerateStructureDisableHook(method, transformedName);
			}
			else
			{
				ASMDebugHelper.logNotFound(methodName, transformedName);
			}
			
			methodName = ObfNameHelper.Methods.ISINSIDESTRUCTURE.getName();
			methodDesc = ObfNameHelper.Methods.ISINSIDESTRUCTURE.getDescriptor();
			method = ASMHelper.findMethodNodeOfClass(classNode, methodName, methodDesc);
			
			if (method != null)
			{
				ASMDebugHelper.logTransforming(methodName, transformedName);
				modified = modified || this.injectisInsideStructureDisableHook(method, transformedName);
			}
			else
			{
				ASMDebugHelper.logNotFound(methodName, transformedName);
			}
			
			methodName = ObfNameHelper.Methods.ISPOSITIONINSTRUCTURE.getName();
			methodDesc = ObfNameHelper.Methods.ISPOSITIONINSTRUCTURE.getDescriptor();
			method = ASMHelper.findMethodNodeOfClass(classNode, methodName, methodDesc);
			
			if (method != null)
			{
				ASMDebugHelper.logTransforming(methodName, transformedName);
				modified = modified || this.injectIsPositionInStructureDisableHook(method, transformedName);
			}
			else
			{
				ASMDebugHelper.logNotFound(methodName, transformedName);
			}
			
			methodName = ObfNameHelper.Methods.GETCLOSESTSTRONGHOLDPOS.getName();
			methodDesc = ObfNameHelper.Methods.GETCLOSESTSTRONGHOLDPOS.getDescriptor();
			method = ASMHelper.findMethodNodeOfClass(classNode, methodName, methodDesc);
			
			if (method != null)
			{
				ASMDebugHelper.logTransforming(methodName, transformedName);
				modified = modified || this.injectGetClosestStrongholdPosDisableHook(method, transformedName);
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
		return new String[]{"*"};
	}

	@Override
	public String getModuleName()
	{
		return "transformMapGenStructure";
	}

	@Override
	public void registerAdditions(final ASMAdditionRegistry arg0)
	{
	}
	
	private boolean injectisInsideStructureDisableHook(final MethodNode method, final String transformedName)
	{
		final AbstractInsnNode target = ASMHelper.findFirstInstruction(method);
		
		if (target == null)
		{
			ASMDebugHelper.unexpectedMethodInstructionPattern(method.name, transformedName);
			return false;
		}
		
		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(ObfNameHelper.Fields.MAPGENSTRUCTURE_WORLD.toInsnNode(Opcodes.GETFIELD));
		toInject.add(ObfNameHelper.Methods.ONISINSIDESTRUCTURE.toInsnNode(Opcodes.INVOKESTATIC));
		final LabelNode label = new LabelNode();
		toInject.add(new JumpInsnNode(Opcodes.IFEQ, label));
		toInject.add(new InsnNode(Opcodes.ICONST_0));
		toInject.add(new InsnNode(Opcodes.IRETURN));
		toInject.add(label);
		
		method.instructions.insertBefore(target, toInject);
		return true;
	}
	
	private boolean injectGenerateStructureDisableHook(final MethodNode method, final String transformedName)
	{
		final AbstractInsnNode target = ASMHelper.findFirstInstruction(method);
		
		if (target == null)
		{
			ASMDebugHelper.unexpectedMethodInstructionPattern(method.name, transformedName);
			return false;
		}
		
		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
		toInject.add(ObfNameHelper.Methods.ONGENERATESTRUCTURE.toInsnNode(Opcodes.INVOKESTATIC));
		final LabelNode label = new LabelNode();
		toInject.add(new JumpInsnNode(Opcodes.IFEQ, label));
		toInject.add(new InsnNode(Opcodes.ICONST_0));
		toInject.add(new InsnNode(Opcodes.IRETURN));
		toInject.add(label);
		
		method.instructions.insertBefore(target, toInject);
		return true;
	}
	
	private boolean injectIsPositionInStructureDisableHook(final MethodNode method, final String transformedName)
	{
		final AbstractInsnNode target = ASMHelper.findFirstInstruction(method);
		
		if (target == null)
		{
			ASMDebugHelper.unexpectedMethodInstructionPattern(method.name, transformedName);
			return false;
		}
		
		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
		toInject.add(ObfNameHelper.Methods.ONISPOSITIONINSTRUCTURE.toInsnNode(Opcodes.INVOKESTATIC));
		final LabelNode label = new LabelNode();
		toInject.add(new JumpInsnNode(Opcodes.IFEQ, label));
		toInject.add(new InsnNode(Opcodes.ICONST_0));
		toInject.add(new InsnNode(Opcodes.IRETURN));
		toInject.add(label);
		
		method.instructions.insertBefore(target, toInject);
		return true;
	}
	
	private boolean injectGetClosestStrongholdPosDisableHook(final MethodNode method, final String transformedName)
	{
		final AbstractInsnNode target = ASMHelper.findFirstInstruction(method);
		
		if (target == null)
		{
			ASMDebugHelper.unexpectedMethodInstructionPattern(method.name, transformedName);
			return false;
		}
		
		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
		toInject.add(ObfNameHelper.Methods.ONGETCLOSESTSTRONGHOLDPOS.toInsnNode(Opcodes.INVOKESTATIC));
		final LabelNode label = new LabelNode();
		toInject.add(new JumpInsnNode(Opcodes.IFEQ, label));
		toInject.add(new InsnNode(Opcodes.ACONST_NULL));
		toInject.add(new InsnNode(Opcodes.ARETURN));
		toInject.add(label);
		
		method.instructions.insertBefore(target, toInject);
		return true;
	}
}