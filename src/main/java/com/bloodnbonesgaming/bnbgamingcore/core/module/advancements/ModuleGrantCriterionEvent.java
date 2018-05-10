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

public class ModuleGrantCriterionEvent implements IClassTransformerModule
{
    @Override
    public byte[] transform(final String name, final String transformedName, final byte[] basicClass)
    {
        if (ASMUtils.doesClassEqual(transformedName, ObfNameHelper.Classes.ICRITERIONTRIGGER$LISTENER))
        {
            final ClassNode classNode = ASMHelper.readClassFromBytes(basicClass);
            String methodName = ObfNameHelper.Methods.LISTENER_GRANT_CRITERION.getName();
            String methodDesc = ObfNameHelper.Methods.LISTENER_GRANT_CRITERION.getDescriptor();
            final MethodNode method = ASMHelper.findMethodNodeOfClass(classNode, methodName, methodDesc);

            if (method != null)
            {
                ASMDebugHelper.logAttemptingTransform(methodName, transformedName);
                
                if (this.addGrantCriterionEventHook(method, transformedName))
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
        return new String[]{ObfNameHelper.Classes.ICRITERIONTRIGGER$LISTENER.getName()};
    }

    @Override
    public String getModuleName()
    {
        return "grantCriterionEvent";
    }

    @Override
    public void registerAdditions(final ASMAdditionRegistry arg0)
    {
    }

    private boolean addGrantCriterionEventHook(final MethodNode method, final String transformedName)
    {
        AbstractInsnNode target = ASMHelper.findFirstInstruction(method);
        
        if (target == null)
        {
            ASMDebugHelper.unexpectedMethodInstructionPattern(method.name, transformedName);
            return false;
        }
        
        final InsnList toInject = new InsnList();
        toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
        toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
        toInject.add(ObfNameHelper.Fields.ADVANCEMENT.toInsnNode(Opcodes.GETFIELD));
        toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
        toInject.add(ObfNameHelper.Fields.CRITERION_NAME.toInsnNode(Opcodes.GETFIELD));
        toInject.add(ObfNameHelper.Methods.ON_GRANT_CRITERION.toInsnNode(Opcodes.INVOKESTATIC));
        LabelNode label = new LabelNode();
        toInject.add(new JumpInsnNode(Opcodes.IFEQ, label));
        toInject.add(new InsnNode(Opcodes.RETURN));
        toInject.add(label);
        
        method.instructions.insertBefore(target, toInject);
        return true;
    }
}