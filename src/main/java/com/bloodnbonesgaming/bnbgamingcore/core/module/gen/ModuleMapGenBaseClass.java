package com.bloodnbonesgaming.bnbgamingcore.core.module.gen;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.bloodnbonesgaming.bnbgamingcore.core.module.IClassTransformerModule;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMAdditionRegistry;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMDebugHelper;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ObfNameHelper;

import squeek.asmhelper.com.bloodnbonesgaming.bnbgamingcore.ASMHelper;

public class ModuleMapGenBaseClass implements IClassTransformerModule
{
	@Override
	public byte[] transform(final String name, final String transformedName, final byte[] basicClass)
	{
		final ClassNode classNode = ASMHelper.readClassFromBytes(basicClass);
		String methodName = ObfNameHelper.Methods.GENERATE.getName();
		String methodDesc = ObfNameHelper.Methods.GENERATE.getDescriptor();
		final MethodNode method = ASMHelper.findMethodNodeOfClass(classNode, methodName, methodDesc);

		if (method != null)
		{
			ASMDebugHelper.logTransforming(methodName, transformedName);
			
			if (this.injectDisableHook(method, transformedName))
			{
				return ASMHelper.writeClassToBytes(classNode);
			}
		}
		else
		{
			ASMDebugHelper.logNotFound(methodName, transformedName);
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
		return new String[]{ObfNameHelper.Classes.MAPGENBASE.getName()};
	}

	@Override
	public String getModuleName()
	{
		return "transformMapGenBase";
	}

	@Override
	public void registerAdditions(final ASMAdditionRegistry arg0)
	{
	}
	
	private boolean injectDisableHook(final MethodNode method, final String transformedName)
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
		toInject.add(ObfNameHelper.Methods.ONMAPGENGENERATE.toInsnNode(Opcodes.INVOKESTATIC));
		final LabelNode label = new LabelNode();
		toInject.add(new JumpInsnNode(Opcodes.IFEQ, label));
		toInject.add(new InsnNode(Opcodes.RETURN));
		toInject.add(label);
		
		method.instructions.insertBefore(target, toInject);
		return true;
	}
}