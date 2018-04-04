package com.bloodnbonesgaming.bnbgamingcore.core.module.advancements;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import com.bloodnbonesgaming.bnbgamingcore.core.module.IClassTransformerModule;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMAdditionRegistry;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMDebugHelper;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMUtils;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ObfNameHelper;

import squeek.asmhelper.com.bloodnbonesgaming.bnbgamingcore.ASMHelper;

public class ModuleAdvancementSync implements IClassTransformerModule
{
    @Override
    public byte[] transform(final String name, final String transformedName, final byte[] basicClass)
    {
        if (ASMUtils.doesClassEqual(transformedName, ObfNameHelper.Classes.PLAYER_ADVANCEMENTS))
        {
            final ClassNode classNode = ASMHelper.readClassFromBytes(basicClass);
            String methodName = ObfNameHelper.Methods.ADVANCEMENT$BUILDER_READ_FROM.getName();
            String methodDesc = ObfNameHelper.Methods.ADVANCEMENT$BUILDER_READ_FROM.getDescriptor();
            final MethodNode method = ASMHelper.findMethodNodeOfClass(classNode, methodName, methodDesc);

            if (method != null)
            {
                ASMDebugHelper.logAttemptingTransform(methodName, transformedName);
                
                if (this.redirectDisplayData(method, transformedName))
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
        return new String[]{ObfNameHelper.Classes.ADVANCEMENT$BUILDER.getName()};
    }

    @Override
    public String getModuleName()
    {
        return "advancementSync";
    }

    @Override
    public void registerAdditions(final ASMAdditionRegistry arg0)
    {
    }

    private boolean redirectDisplayData(final MethodNode method, final String transformedName)
    {
        AbstractInsnNode target = ASMHelper.find(method.instructions, ObfNameHelper.Methods.DISPLAY_INFO_READ.toInsnNode(Opcodes.INVOKESTATIC));
        
        if (target == null)
        {
            ASMDebugHelper.unexpectedMethodInstructionPattern(method.name, transformedName);
            return false;
        }
        
        final AbstractInsnNode replacement = ObfNameHelper.Methods.EXTENDED_DISPLAY_INFO_READ.toInsnNode(Opcodes.INVOKESTATIC);
        
        method.instructions.set(target, replacement);
        return true;
    }
}