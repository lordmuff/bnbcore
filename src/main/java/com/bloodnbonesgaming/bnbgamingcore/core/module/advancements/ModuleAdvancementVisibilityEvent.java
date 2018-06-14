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

public class ModuleAdvancementVisibilityEvent implements IClassTransformerModule
{
    @Override
    public byte[] transform(final String name, final String transformedName, final byte[] basicClass)
    {
        if (ASMUtils.doesClassEqual(transformedName, ObfNameHelper.Classes.PLAYER_ADVANCEMENTS))
        {
            final ClassNode classNode = ASMHelper.readClassFromBytes(basicClass);
            String methodName = ObfNameHelper.Methods.SHOULD_BE_VISIBLE.getName();
            String methodDesc = ObfNameHelper.Methods.SHOULD_BE_VISIBLE.getDescriptor();
            MethodNode method = ASMHelper.findMethodNodeOfClass(classNode, methodName, methodDesc);

            if (method != null)
            {
                ASMDebugHelper.logAttemptingTransform(methodName, transformedName);
                
                if (this.addShouldBeVisibleHook(method, transformedName))
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
        return "advancementVisibilityEvent";
    }

    @Override
    public void registerAdditions(final ASMAdditionRegistry arg0)
    {
    }
    
    private boolean addShouldBeVisibleHook(final MethodNode method, final String transformedName)
    {
        final AbstractInsnNode target = ASMHelper.findFirstInstruction(method);
        
        if (target == null)
        {
            ASMDebugHelper.unexpectedMethodInstructionPattern(method.name + " target", transformedName);
            return false;
        }
        
        final InsnList toInject = new InsnList();
        toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
        toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
        final LabelNode label = new LabelNode();
        toInject.add(ObfNameHelper.Methods.ON_ADVANCEMENT_VISIBILITY_EVENT.toInsnNode(Opcodes.INVOKESTATIC));
        toInject.add(new JumpInsnNode(Opcodes.IFEQ, label));
        toInject.add(new InsnNode(Opcodes.ICONST_1));
        toInject.add(new InsnNode(Opcodes.IRETURN));
        toInject.add(label);
        
        method.instructions.insert(target, toInject);
        return true;
    }
}