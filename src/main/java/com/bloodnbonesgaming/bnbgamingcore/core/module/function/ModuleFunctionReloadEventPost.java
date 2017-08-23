package com.bloodnbonesgaming.bnbgamingcore.core.module.function;

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

public class ModuleFunctionReloadEventPost implements IClassTransformerModule
{
	@Override
	public byte[] transform(final String name, final String transformedName, final byte[] basicClass)
	{
		if (ASMUtils.doesClassEqual(transformedName, ObfNameHelper.Classes.FUNCTIONMANAGER))
		{
			final ClassNode classNode = ASMHelper.readClassFromBytes(basicClass);
			String methodName = ObfNameHelper.Methods.FUNCTION_MANAGER_RELOAD.getName();
			String methodDesc = ObfNameHelper.Methods.FUNCTION_MANAGER_RELOAD.getDescriptor();
			final MethodNode method = ASMHelper.findMethodNodeOfClass(classNode, methodName, methodDesc);

			if (method != null)
			{
				ASMDebugHelper.logAttemptingTransform(methodName, transformedName);
				
				if (this.addFunctionReloadEventPostHook(method, transformedName))
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
		return new String[]{ObfNameHelper.Classes.FUNCTIONMANAGER.getName()};
	}

	@Override
	public String getModuleName()
	{
		return "functionReloadEventPost";
	}

	@Override
	public void registerAdditions(final ASMAdditionRegistry arg0)
	{
	}

	private boolean addFunctionReloadEventPostHook(final MethodNode method, final String transformedName)
	{
		AbstractInsnNode target = ASMHelper.find(method.instructions, ObfNameHelper.Methods.LOAD_FUNCTIONS.toInsnNode(Opcodes.INVOKESPECIAL));
		
		if (target == null)
		{
			ASMDebugHelper.unexpectedMethodInstructionPattern(method.name + " post", transformedName);
			return false;
		}
		final InsnList toInject = new InsnList();
		
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(ObfNameHelper.Methods.ON_FUNCTION_RELOAD_POST.toInsnNode(Opcodes.INVOKESTATIC));
		
		method.instructions.insertBefore(target, toInject);
		return true;
	}
}