package com.bloodnbonesgaming.bnbgamingcore.core.module.advancements;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import com.bloodnbonesgaming.bnbgamingcore.core.module.IClassTransformerModule;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMAdditionRegistry;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMDebugHelper;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMUtils;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ObfNameHelper;

import squeek.asmhelper.com.bloodnbonesgaming.bnbgamingcore.ASMHelper;

public class ModuleAdvancementLinkedHashMap implements IClassTransformerModule
{
    @Override
    public byte[] transform(final String name, final String transformedName, final byte[] basicClass)
    {
        if (ASMUtils.doesClassEqual(transformedName, ObfNameHelper.Classes.ADVANCEMENTMANAGER))
        {
            final ClassNode classNode = ASMHelper.readClassFromBytes(basicClass);
            String methodName = ObfNameHelper.Methods.LOAD_CUSTOM_ADVANCEMENTS.getName();
            String methodDesc = ObfNameHelper.Methods.LOAD_CUSTOM_ADVANCEMENTS.getDescriptor();
            final MethodNode method = ASMHelper.findMethodNodeOfClass(classNode, methodName, methodDesc);

            if (method != null)
            {
                ASMDebugHelper.logAttemptingTransform(methodName, transformedName);
                
                if (this.replaceHashMapWithLinkedHashMap(method, transformedName))
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
        return new String[]{ObfNameHelper.Classes.ADVANCEMENTMANAGER.getName()};
    }

    @Override
    public String getModuleName()
    {
        return "replaceAdvancementMapWithLinkedHashMap";
    }

    @Override
    public void registerAdditions(final ASMAdditionRegistry arg0)
    {
    }

    private boolean replaceHashMapWithLinkedHashMap(final MethodNode method, final String transformedName)
    {
        final AbstractInsnNode target = new MethodInsnNode(Opcodes.INVOKESTATIC, "com/google/common/collect/Maps", "newHashMap", "()Ljava/util/HashMap;", false);
        final InsnList targetList = new InsnList();
        targetList.add(target);
        
        final AbstractInsnNode replacement = new MethodInsnNode(Opcodes.INVOKESTATIC, "com/google/common/collect/Maps", "newLinkedHashMap", "()Ljava/util/LinkedHashMap;", false);
        final InsnList replacementList = new InsnList();
        replacementList.add(replacement);
        
        ASMHelper.findAndReplaceAll(method.instructions, targetList, replacementList);
        return true;
    }
}