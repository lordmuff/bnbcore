package com.bloodnbonesgaming.bnbgamingcore.core.module.minecraftserver;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
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

public class ModuleMinecraftServerTickEvent implements IClassTransformerModule
{
    @Override
    public byte[] transform(final String name, final String transformedName, final byte[] basicClass)
    {
        if (ASMUtils.doesClassEqual(transformedName, ObfNameHelper.Classes.MINECRAFT_SERVER))
        {
            final ClassNode classNode = ASMHelper.readClassFromBytes(basicClass);
            String methodName = ObfNameHelper.Methods.MINECRAFT_SERVER_UPDATE_TIME_LIGHT_ENTITIES.getName();
            String methodDesc = ObfNameHelper.Methods.MINECRAFT_SERVER_UPDATE_TIME_LIGHT_ENTITIES.getDescriptor();
            MethodNode method = ASMHelper.findMethodNodeOfClass(classNode, methodName, methodDesc);

            if (method != null)
            {
                ASMDebugHelper.logAttemptingTransform(methodName, transformedName);
                
                if (this.addMinecraftServerForgeEventWrapper(method, transformedName) && this.addMinecraftServerForgeEventWrapper2(method, transformedName))
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
        return new String[]{ObfNameHelper.Classes.MINECRAFT_SERVER.getName()};
    }

    @Override
    public String getModuleName()
    {
        return "minecraftServerPostTickEvent";
    }

    @Override
    public void registerAdditions(final ASMAdditionRegistry arg0)
    {
    }

    private boolean addMinecraftServerForgeEventWrapper(final MethodNode method, final String transformedName)
    {
        //Pre
        InsnList toFind = new InsnList();
        toFind.add(ObfNameHelper.Methods.FML_COMMON_HANDLER_INSTANCE.toInsnNode(Opcodes.INVOKESTATIC));
        toFind.add(new VarInsnNode(Opcodes.ALOAD, 6));
        toFind.add(ObfNameHelper.Methods.FML_COMMON_HANDLER_PRE_WORLD_TICK.toInsnNode(Opcodes.INVOKEVIRTUAL));
        
        AbstractInsnNode start = ASMHelper.find(method.instructions, toFind);
        
        if (start == null)
        {
            ASMDebugHelper.unexpectedMethodInstructionPattern(method.name + " 1", transformedName);
            return false;
        }
        
        InsnList toInject = new InsnList();
        toInject.add(ObfNameHelper.Methods.ON_WORLD_SERVER_FORGE_POST_EVENT.toInsnNode(Opcodes.INVOKESTATIC));
        final LabelNode label = new LabelNode();
        toInject.add(new JumpInsnNode(Opcodes.IFNE, label));
        
        method.instructions.insertBefore(start, toInject);
        
        AbstractInsnNode end = ASMHelper.move(start, 2);
        
        method.instructions.insert(end, label);
        
        return true;
    }

    private boolean addMinecraftServerForgeEventWrapper2(final MethodNode method, final String transformedName)
    {
        //Post
        InsnList toFind = new InsnList();
        toFind.add(ObfNameHelper.Methods.FML_COMMON_HANDLER_INSTANCE.toInsnNode(Opcodes.INVOKESTATIC));
        toFind.add(new VarInsnNode(Opcodes.ALOAD, 6));
        toFind.add(ObfNameHelper.Methods.FML_COMMON_HANDLER_POST_WORLD_TICK.toInsnNode(Opcodes.INVOKEVIRTUAL));
        
        AbstractInsnNode start = ASMHelper.find(method.instructions, toFind);
        
        if (start == null)
        {
            ASMDebugHelper.unexpectedMethodInstructionPattern(method.name + " 2", transformedName);
            return false;
        }
        
        InsnList toInject = new InsnList();
        toInject.add(ObfNameHelper.Methods.ON_WORLD_SERVER_FORGE_POST_EVENT.toInsnNode(Opcodes.INVOKESTATIC));
        final LabelNode label = new LabelNode();
        toInject.add(new JumpInsnNode(Opcodes.IFNE, label));
        
        method.instructions.insertBefore(start, toInject);
        
        AbstractInsnNode end = ASMHelper.move(start, 2);
        
        method.instructions.insert(end, label);
        
        return true;
    }
}