package com.bloodnbonesgaming.bnbgamingcore.core.module.worldserver;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;

import com.bloodnbonesgaming.bnbgamingcore.core.module.IClassTransformerModule;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMAdditionRegistry;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMDebugHelper;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMUtils;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ObfNameHelper;

import squeek.asmhelper.com.bloodnbonesgaming.bnbgamingcore.ASMHelper;

public class ModuleWorldServerEvents implements IClassTransformerModule
{
    @Override
    public byte[] transform(final String name, final String transformedName, final byte[] basicClass)
    {
        if (ASMUtils.doesClassEqual(transformedName, ObfNameHelper.Classes.WORLD_SERVER))
        {
            final ClassNode classNode = ASMHelper.readClassFromBytes(basicClass);
            String methodName = ObfNameHelper.Methods.WORLD_SERVER_TICK.getName();
            String methodDesc = ObfNameHelper.Methods.WORLD_SERVER_TICK.getDescriptor();
            MethodNode method = ASMHelper.findMethodNodeOfClass(classNode, methodName, methodDesc);

            if (method != null)
            {
                ASMDebugHelper.logAttemptingTransform(methodName, transformedName);
                
                if (this.addWorldServerTickHook(method, transformedName))
                {
                    ASMDebugHelper.logSuccessfulTransform(methodName, transformedName);
                    return ASMHelper.writeClassToBytes(classNode, 3);
                }
            }
            else
            {
                ASMDebugHelper.logNotFound(methodName, transformedName);
            }
            
            methodName = ObfNameHelper.Methods.WORLD_SERVER_UPDATE_ENTITIES.getName();
            methodDesc = ObfNameHelper.Methods.WORLD_SERVER_UPDATE_ENTITIES.getDescriptor();
            method = ASMHelper.findMethodNodeOfClass(classNode, methodName, methodDesc);

            if (method != null)
            {
                ASMDebugHelper.logAttemptingTransform(methodName, transformedName);
                
                if (this.addWorldServerUpdateEntitiesHook(method, transformedName))
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
        return new String[]{ObfNameHelper.Classes.WORLD_SERVER.getName()};
    }

    @Override
    public String getModuleName()
    {
        return "worldServerEvents";
    }

    @Override
    public void registerAdditions(final ASMAdditionRegistry arg0)
    {
    }

    private boolean addWorldServerTickHook(final MethodNode method, final String transformedName)
    {
        AbstractInsnNode target = ASMHelper.findFirstInstruction(method);
        
        if (target == null)
        {
            ASMDebugHelper.unexpectedMethodInstructionPattern(method.name, transformedName);
            return false;
        }
        
        final InsnList toInject = new InsnList();
        toInject.add(ObfNameHelper.Methods.ON_WORLD_SERVER_TICK_PRE.toInsnNode(Opcodes.INVOKESTATIC));
        LabelNode label = new LabelNode();
        toInject.add(new JumpInsnNode(Opcodes.IFEQ, label));
        toInject.add(new InsnNode(Opcodes.RETURN));
        toInject.add(label);
        
        method.instructions.insertBefore(target, toInject);
        return true;
    }

    private boolean addWorldServerUpdateEntitiesHook(final MethodNode method, final String transformedName)
    {
        AbstractInsnNode target = ASMHelper.findFirstInstruction(method);
        
        if (target == null)
        {
            ASMDebugHelper.unexpectedMethodInstructionPattern(method.name, transformedName);
            return false;
        }
        
        final InsnList toInject = new InsnList();
        toInject.add(ObfNameHelper.Methods.ON_WORLD_SERVER_UPDATE_ENTITIES_PRE.toInsnNode(Opcodes.INVOKESTATIC));
        LabelNode label = new LabelNode();
        toInject.add(new JumpInsnNode(Opcodes.IFEQ, label));
        toInject.add(new InsnNode(Opcodes.RETURN));
        toInject.add(label);
        
        method.instructions.insertBefore(target, toInject);
        return true;
    }
}