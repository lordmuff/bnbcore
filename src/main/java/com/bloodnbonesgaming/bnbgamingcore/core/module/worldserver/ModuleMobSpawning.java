package com.bloodnbonesgaming.bnbgamingcore.core.module.worldserver;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import com.bloodnbonesgaming.bnbgamingcore.core.module.IClassTransformerModule;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMAdditionRegistry;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMDebugHelper;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ObfNameHelper;

import squeek.asmhelper.com.bloodnbonesgaming.bnbgamingcore.ASMHelper;

public class ModuleMobSpawning implements IClassTransformerModule
{
	@Override
	public byte[] transform(final String name, final String transformedName, final byte[] basicClass)
	{
		final ClassNode classNode = ASMHelper.readClassFromBytes(basicClass);
		String methodName = ObfNameHelper.Methods.WORLD_SERVER_TICK.getName();
		String methodDesc = ObfNameHelper.Methods.WORLD_SERVER_TICK.getDescriptor();
		final MethodNode method = ASMHelper.findMethodNodeOfClass(classNode, methodName, methodDesc);

		if (method != null)
		{
			ASMDebugHelper.logAttemptingTransform(methodName, transformedName);
			
			if (this.findChunksForSpawningReplacement(method, transformedName))
			{
				ASMDebugHelper.logSuccessfulTransform(methodName, transformedName);
				return ASMHelper.writeClassToBytes(classNode, 3);
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
		return new String[]{ObfNameHelper.Classes.WORLD_SERVER.getName()};
	}

	@Override
	public String getModuleName()
	{
		return "mobSpawningEvent";
	}

	@Override
	public void registerAdditions(final ASMAdditionRegistry arg0)
	{
	}

	private boolean findChunksForSpawningReplacement(final MethodNode method, final String transformedName)
	{
		AbstractInsnNode target = ASMHelper.find(method.instructions, ObfNameHelper.Methods.FIND_CHUNKS_FOR_SPAWNING.toInsnNode(Opcodes.INVOKEVIRTUAL));
		
		if (target == null)
		{
			ASMDebugHelper.unexpectedMethodInstructionPattern(method.name, transformedName);
			return false;
		}
		
		method.instructions.set(target, ObfNameHelper.Methods.ON_MOB_SPAWNING_EVENT.toInsnNode(Opcodes.INVOKESTATIC));
		return true;
	}
}