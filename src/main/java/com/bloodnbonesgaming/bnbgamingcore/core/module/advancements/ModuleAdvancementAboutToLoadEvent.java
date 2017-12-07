package com.bloodnbonesgaming.bnbgamingcore.core.module.advancements;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.bloodnbonesgaming.bnbgamingcore.core.module.IClassTransformerModule;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMAdditionRegistry;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMDebugHelper;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMUtils;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ObfNameHelper;

import squeek.asmhelper.com.bloodnbonesgaming.bnbgamingcore.ASMHelper;

public class ModuleAdvancementAboutToLoadEvent implements IClassTransformerModule
{
    @Override
    public byte[] transform(final String name, final String transformedName, final byte[] basicClass)
    {
        if (ASMUtils.doesClassEqual(transformedName, ObfNameHelper.Classes.ADVANCEMENTMANAGER))
        {
            boolean changed = false;
            final ClassNode classNode = ASMHelper.readClassFromBytes(basicClass);
            String methodName = ObfNameHelper.Methods.LOAD_CUSTOM_ADVANCEMENTS.getName();
            String methodDesc = ObfNameHelper.Methods.LOAD_CUSTOM_ADVANCEMENTS.getDescriptor();
            MethodNode method = ASMHelper.findMethodNodeOfClass(classNode, methodName, methodDesc);

            if (method != null)
            {
                ASMDebugHelper.logAttemptingTransform(methodName, transformedName);
                
//                if (this.addLoadCustomAdvancementsHook(method, transformedName))
//                {
//                    ASMDebugHelper.logSuccessfulTransform(methodName, transformedName);
//                changed = true;
//                    return ASMHelper.writeClassToBytes(classNode);
//                }
            }
            else
            {
                ASMDebugHelper.logNotFound(methodName, transformedName);
            }
            
            methodName = ObfNameHelper.Methods.LOAD_BUILTIN_ADVANCEMENTS.getName();
            methodDesc = ObfNameHelper.Methods.LOAD_BUILTIN_ADVANCEMENTS.getDescriptor();
            method = ASMHelper.findMethodNodeOfClass(classNode, methodName, methodDesc);

            if (method != null)
            {
                ASMDebugHelper.logAttemptingTransform(methodName, transformedName);
                
                if (this.addLoadBuiltInAdvancementsHook(method, transformedName))
                {
                    ASMDebugHelper.logSuccessfulTransform(methodName, transformedName);
                    changed = true;
                }
            }
            else
            {
                ASMDebugHelper.logNotFound(methodName, transformedName);
            }
            if (changed)
            {
                return ASMHelper.writeClassToBytes(classNode, 3);
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
        return "advancementAboutToLoadEvent";
    }

    @Override
    public void registerAdditions(final ASMAdditionRegistry arg0)
    {
    }
    
    private boolean addLoadCustomAdvancementsHook(final MethodNode method, final String transformedName)
    {
        AbstractInsnNode target = ASMHelper.find(method.instructions, new VarInsnNode(Opcodes.ASTORE, 6));
        
        if (target == null)
        {
            ASMDebugHelper.unexpectedMethodInstructionPattern(method.name + " target", transformedName);
            return false;
        }
        
        LabelNode label = ASMUtils.findLabelNode(method.instructions, 10);
        
        if (label == null)
        {
            ASMDebugHelper.unexpectedMethodInstructionPattern(method.name + " label", transformedName);
            return false;
        }
        
        final InsnList toInject = new InsnList();
        toInject.add(new VarInsnNode(Opcodes.ALOAD, 6));
        toInject.add(ObfNameHelper.Methods.ON_ADVANCEMENT_ABOUT_TO_LOAD.toInsnNode(Opcodes.INVOKESTATIC));
        final LabelNode label2 = new LabelNode();
        toInject.add(new JumpInsnNode(Opcodes.IFEQ, label2));
        toInject.add(new JumpInsnNode(Opcodes.GOTO, label));
        toInject.add(label2);
        
        method.instructions.insert(target, toInject);
        return true;
    }

    private boolean addLoadBuiltInAdvancementsHook(final MethodNode method, final String transformedName)
    {
//        AbstractInsnNode target = ASMHelper.find(method.instructions, new VarInsnNode(Opcodes.ASTORE, 9));
//        
//        if (target == null)
//        {
//            ASMDebugHelper.unexpectedMethodInstructionPattern(method.name + " target", transformedName);
//            return false;
//        }
//        
//        final InsnList toFind = new InsnList();
//        toFind.add(new VarInsnNode(Opcodes.ALOAD, 6));
//        toFind.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "java/util/Iterator", "hasNext", "()Z", true));
//        
//        AbstractInsnNode continueTarget = ASMHelper.find(method.instructions, toFind);
//        
//        if (continueTarget == null)
//        {
//            ASMDebugHelper.unexpectedMethodInstructionPattern(method.name + " continueTarget", transformedName);
//            return false;
//        }
//        
//        final LabelNode continueLabel = ASMUtils.findPreviousLabelNode(continueTarget);
//        
//        if (continueLabel == null)
//        {
//            ASMDebugHelper.unexpectedMethodInstructionPattern(method.name + " continueLabel", transformedName);
//            return false;
//        }
//        
//        final InsnList toInject = new InsnList();
//        toInject.add(new VarInsnNode(Opcodes.ALOAD, 9));
//        toInject.add(ObfNameHelper.Methods.ON_ADVANCEMENT_ABOUT_TO_LOAD.toInsnNode(Opcodes.INVOKESTATIC));
//        final LabelNode label = new LabelNode();
//        toInject.add(new JumpInsnNode(Opcodes.IFEQ, label));
//        toInject.add(new JumpInsnNode(Opcodes.GOTO, continueLabel));
//        toInject.add(label);
//        
//        method.instructions.insert(target, toInject);
        
//        mv.visitFieldInsn(GETSTATIC, "net/minecraft/advancements/AdvancementManager", "GSON", "Lcom/google/gson/Gson;");
//        mv.visitVarInsn(ALOAD, 11);
//        mv.visitLdcInsn(Type.getType("Lnet/minecraft/advancements/Advancement$Builder;"));
//        mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/util/JsonUtils", "fromJson", "(Lcom/google/gson/Gson;Ljava/io/Reader;Ljava/lang/Class;)Ljava/lang/Object;", false);
//        mv.visitTypeInsn(CHECKCAST, "net/minecraft/advancements/Advancement$Builder");
//        mv.visitVarInsn(ASTORE, 12);
        
//        mv.visitFieldInsn(GETSTATIC, "net/minecraft/advancements/AdvancementManager", "GSON", "Lcom/google/gson/Gson;");
//        mv.visitVarInsn(ALOAD, 11);
//        mv.visitVarInsn(ALOAD, 10);
//        mv.visitMethodInsn(INVOKESTATIC, "net/minecraftforge/event/ForgeEventFactory", "onAdvancementAboutToLoad", "(Lcom/google/gson/Gson;Ljava/io/Reader;Lnet/minecraft/util/ResourceLocation;)Lnet/minecraftforge/event/advancement/AdvancementAboutToLoadEvent;", false);
//        mv.visitVarInsn(ASTORE, 12);
//        Label l40 = new Label();
//        mv.visitLabel(l40);
//        mv.visitLineNumber(187, l40);
//        mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/common/MinecraftForge", "EVENT_BUS", "Lnet/minecraftforge/fml/common/eventhandler/EventBus;");
//        mv.visitVarInsn(ALOAD, 12);
//        mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraftforge/fml/common/eventhandler/EventBus", "post", "(Lnet/minecraftforge/fml/common/eventhandler/Event;)Z", false);
//        mv.visitJumpInsn(IFEQ, l3);
//        mv.visitLabel(l1);
//        mv.visitLineNumber(203, l1);
//        mv.visitVarInsn(ALOAD, 11);
//        mv.visitMethodInsn(INVOKESTATIC, "org/apache/commons/io/IOUtils", "closeQuietly", "(Ljava/io/Reader;)V", false);
//        Label l41 = new Label();
//        mv.visitLabel(l41);
//        mv.visitLineNumber(187, l41);
//        mv.visitJumpInsn(GOTO, l31);
//        mv.visitLabel(l3);
//        mv.visitLineNumber(188, l3);
//        mv.visitFrame(Opcodes.F_FULL, 13, new Object[] {"net/minecraft/advancements/AdvancementManager", "java/util/Map", "java/nio/file/FileSystem", "java/net/URL", "java/net/URI", "java/nio/file/Path", "java/util/Iterator", "java/nio/file/Path", "java/nio/file/Path", "java/lang/String", "net/minecraft/util/ResourceLocation", "java/io/BufferedReader", "net/minecraftforge/event/advancement/AdvancementAboutToLoadEvent"}, 0, new Object[] {});
//        mv.visitFieldInsn(GETSTATIC, "net/minecraft/advancements/AdvancementManager", "GSON", "Lcom/google/gson/Gson;");
//        mv.visitVarInsn(ALOAD, 12);
//        mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraftforge/event/advancement/AdvancementAboutToLoadEvent", "getJson", "()Lcom/google/gson/JsonElement;", false);
//        mv.visitLdcInsn(Type.getType("Lnet/minecraft/advancements/Advancement$Builder;"));
//        mv.visitMethodInsn(INVOKEVIRTUAL, "com/google/gson/Gson", "fromJson", "(Lcom/google/gson/JsonElement;Ljava/lang/Class;)Ljava/lang/Object;", false);
//        mv.visitTypeInsn(CHECKCAST, "net/minecraft/advancements/Advancement$Builder");
//        mv.visitVarInsn(ASTORE, 13);
        
//        mv.visitVarInsn(ALOAD, 6);
//        mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "hasNext", "()Z", true);
        
//        mv.visitVarInsn(ALOAD, 1);
//        mv.visitVarInsn(ALOAD, 10);
//        mv.visitVarInsn(ALOAD, 13);
//        mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true);
        
        final InsnList toFind = new InsnList();
        final InsnList replacement = new InsnList();
        final InsnList toFindAfterLoop = new InsnList();
        final InsnList toAddAfterLoop = new InsnList();
        
//        toFind.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/advancements/AdvancementManager", "GSON", "Lcom/google/gson/Gson;"));
        toFind.add(ObfNameHelper.Fields.ADVANCEMENT_MANAGER_GSON.toInsnNode(Opcodes.GETSTATIC));
        toFind.add(new VarInsnNode(Opcodes.ALOAD, 11));
        toFind.add(new LdcInsnNode(Type.getType("Lnet/minecraft/advancements/Advancement$Builder;")));
        toFind.add(ObfNameHelper.Methods.JSON_UTILS_FROM_JSON.toInsnNode(Opcodes.INVOKESTATIC));
        toFind.add(new TypeInsnNode(Opcodes.CHECKCAST, "net/minecraft/advancements/Advancement$Builder"));
        toFind.add(new VarInsnNode(Opcodes.ASTORE, 12));
        
        toFind.add(new VarInsnNode(Opcodes.ALOAD, 1));
        toFind.add(new VarInsnNode(Opcodes.ALOAD, 10));
        toFind.add(new VarInsnNode(Opcodes.ALOAD, 12));
        toFind.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "java/util/Map", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true));
        
//        replacement.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/advancements/AdvancementManager", "GSON", "Lcom/google/gson/Gson;"));
//        toFind.add(ObfNameHelper.Fields.ADVANCEMENT_MANAGER_GSON.toInsnNode(Opcodes.GETSTATIC));
        replacement.add(new VarInsnNode(Opcodes.ALOAD, 11));
        replacement.add(new VarInsnNode(Opcodes.ALOAD, 10));
        replacement.add(ObfNameHelper.Methods.ON_ADVANCEMENT_ABOUT_TO_LOAD.toInsnNode(Opcodes.INVOKESTATIC));
//        replacement.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/minecraftforge/event/ForgeEventFactory", "onAdvancementAboutToLoad", "(Lcom/google/gson/Gson;Ljava/io/Reader;Lnet/minecraft/util/ResourceLocation;)Lnet/minecraftforge/event/advancement/AdvancementAboutToLoadEvent;", false));
        replacement.add(new VarInsnNode(Opcodes.ASTORE, 12));
        
        replacement.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraftforge/common/MinecraftForge", "EVENT_BUS", "Lnet/minecraftforge/fml/common/eventhandler/EventBus;"));
        replacement.add(new VarInsnNode(Opcodes.ALOAD, 12));
        replacement.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraftforge/fml/common/eventhandler/EventBus", "post", "(Lnet/minecraftforge/fml/common/eventhandler/Event;)Z", false));
        final LabelNode label1 = new LabelNode();
        replacement.add(new JumpInsnNode(Opcodes.IFEQ, label1));
        
        replacement.add(new VarInsnNode(Opcodes.ALOAD, 11));
        replacement.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "org/apache/commons/io/IOUtils", "closeQuietly", "(Ljava/io/Reader;)V", false));
        
        final LabelNode afterLoop = new LabelNode();
        replacement.add(new JumpInsnNode(Opcodes.GOTO, afterLoop));//Probably the outside of loop
        replacement.add(label1);
        
//        replacement.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/advancements/AdvancementManager", "GSON", "Lcom/google/gson/Gson;"));
        replacement.add(ObfNameHelper.Fields.ADVANCEMENT_MANAGER_GSON.toInsnNode(Opcodes.GETSTATIC));
        replacement.add(new VarInsnNode(Opcodes.ALOAD, 12));
//        replacement.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraftforge/event/advancement/AdvancementAboutToLoadEvent", "getJson", "()Lcom/google/gson/JsonElement;", false));
        replacement.add(ObfNameHelper.Methods.ADVANCEMENT_ABOUT_TO_LOAD_EVENT_GET_JSON.toInsnNode(Opcodes.INVOKEVIRTUAL));
        replacement.add(new LdcInsnNode(Type.getType("Lnet/minecraft/advancements/Advancement$Builder;")));
        replacement.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "com/google/gson/Gson", "fromJson", "(Lcom/google/gson/JsonElement;Ljava/lang/Class;)Ljava/lang/Object;", false));
        replacement.add(new TypeInsnNode(Opcodes.CHECKCAST, "net/minecraft/advancements/Advancement$Builder"));
        replacement.add(new VarInsnNode(Opcodes.ASTORE, 13));
        
        replacement.add(new VarInsnNode(Opcodes.ALOAD, 1));
        replacement.add(new VarInsnNode(Opcodes.ALOAD, 10));
        replacement.add(new VarInsnNode(Opcodes.ALOAD, 13));
        replacement.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "java/util/Map", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true));
        
        final AbstractInsnNode find = ASMHelper.find(method.instructions, toFind);
        
        if (find == null)
        {
            ASMDebugHelper.unexpectedMethodInstructionPattern(method.name + " find", transformedName);
            return false;
        }
        
        final AbstractInsnNode replaced = ASMHelper.findAndReplace(method.instructions, toFind, replacement);
        
        if (replaced == null)
        {
            ASMDebugHelper.unexpectedMethodInstructionPattern(method.name + " replaced", transformedName);
            return false;
        }
        
        toAddAfterLoop.add(afterLoop);
        
        toFindAfterLoop.add(new VarInsnNode(Opcodes.ALOAD, 6));
        toFindAfterLoop.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "java/util/Iterator", "hasNext", "()Z", true));
        
        final AbstractInsnNode target = ASMHelper.find(method.instructions, toFindAfterLoop);
        
        if (target == null)
        {
            ASMDebugHelper.unexpectedMethodInstructionPattern(method.name + " target", transformedName);
            return false;
        }
        method.instructions.insertBefore(target, toAddAfterLoop);
        
        return true;
    }
}