package com.bloodnbonesgaming.bnbgamingcore.core.module.worldprovider;

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
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMUtils;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ObfNameHelper;

import squeek.asmhelper.com.bloodnbonesgaming.bnbgamingcore.ASMHelper;

public class ModuleRegisterWorldEvent implements IClassTransformerModule
{
	@Override
	public byte[] transform(final String name, final String transformedName, final byte[] basicClass)
	{
		if (ASMUtils.doesClassEqual(transformedName, ObfNameHelper.Classes.WORLDPROVIDER))
		{
			final ClassNode classNode = ASMHelper.readClassFromBytes(basicClass);
			String methodName = ObfNameHelper.Methods.SETWORLD.getName();
			String methodDesc = ObfNameHelper.Methods.SETWORLD.getDescriptor();
			final MethodNode method = ASMHelper.findMethodNodeOfClass(classNode, methodName, methodDesc);

			if (method != null)
			{
				ASMDebugHelper.logTransforming(methodName, transformedName);
				
				if (this.addRegisterWorldPostHook(method, transformedName) && this.addRegisterWorldPreHook(method, transformedName))
				{
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
		return new String[]{ObfNameHelper.Classes.WORLDPROVIDER.getName()};
	}

	@Override
	public String getModuleName()
	{
		return "registerWorldEvent";
	}

	@Override
	public void registerAdditions(final ASMAdditionRegistry arg0)
	{
	}

	private boolean addRegisterWorldPostHook(final MethodNode method, final String transformedName)
	{
		final AbstractInsnNode target = ASMHelper.find(method.instructions, new InsnNode(Opcodes.RETURN));

		if (target == null)
		{
			ASMDebugHelper.unexpectedMethodInstructionPattern(method.name + " post", transformedName);
			return false;
		}
		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(ObfNameHelper.Methods.ONREGISTERWORLDPOST.toInsnNode(Opcodes.INVOKESTATIC));

		method.instructions.insertBefore(target, toInject);
		return true;
	}

	private boolean addRegisterWorldPreHook(final MethodNode method, final String transformedName)
	{
		final AbstractInsnNode target = ASMHelper.findFirstInstruction(method);

		if (target == null)
		{
			ASMDebugHelper.unexpectedMethodInstructionPattern(method.name + " pre", transformedName);
			return false;
		}
		final InsnList toInject = new InsnList();
		//Call hook
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
		toInject.add(ObfNameHelper.Methods.ONREGISTERWORLDPRE.toInsnNode(Opcodes.INVOKESTATIC));
		//Check hook return value
		final LabelNode label = new LabelNode();
		toInject.add(new JumpInsnNode(Opcodes.IFEQ, label));
		//If true, return
		toInject.add(new InsnNode(Opcodes.RETURN));
		//If false, continue
		toInject.add(label);

		method.instructions.insertBefore(target, toInject);
		return true;
	}
}