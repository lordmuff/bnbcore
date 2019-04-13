package com.bloodnbonesgaming.bnbgamingcore.core.module.entityrenderer;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;

import com.bloodnbonesgaming.bnbgamingcore.core.module.IClassTransformerModule;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMAdditionRegistry;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMDebugHelper;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ObfNameHelper;

import squeek.asmhelper.com.bloodnbonesgaming.bnbgamingcore.ASMHelper;

public class ModuleEntityRenderer implements IClassTransformerModule
{
	@Override
	public byte[] transform(final String name, final String transformedName, final byte[] basicClass)
	{
		final ClassNode classNode = ASMHelper.readClassFromBytes(basicClass);
		String methodName = ObfNameHelper.Methods.HURT_CAMERA_EFFECT.getName();
		String methodDesc = ObfNameHelper.Methods.HURT_CAMERA_EFFECT.getDescriptor();
		final MethodNode method = ASMHelper.findMethodNodeOfClass(classNode, methodName, methodDesc);

		if (method != null)
		{
			ASMDebugHelper.logAttemptingTransform(methodName, transformedName);
			
			if (this.hurtCameraEffectHookAndReturn(method, transformedName))
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
		return new String[]{ObfNameHelper.Classes.ENTITY_RENDERER.getName()};
	}

	@Override
	public String getModuleName()
	{
		return "hurtCameraEffectEvent";
	}

	@Override
	public void registerAdditions(final ASMAdditionRegistry arg0)
	{
	}

	private boolean hurtCameraEffectHookAndReturn(final MethodNode method, final String transformedName)
	{
		AbstractInsnNode target = ASMHelper.findFirstInstruction(method);
		
		if (target == null)
		{
			ASMDebugHelper.unexpectedMethodInstructionPattern(method.name, transformedName);
			return false;
		}
		final InsnList toInject = new InsnList();
		
		//Call method
		toInject.add(ObfNameHelper.Methods.ON_HURT_CAMERA_EFFECT_EVENT.toInsnNode(Opcodes.INVOKESTATIC));
		//Add return
		toInject.add(new InsnNode(Opcodes.RETURN));
		
		method.instructions.insertBefore(target, toInject);
		return true;
	}
}