package com.bloodnbonesgaming.bnbgamingcore.core.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassReader;

import squeek.asmhelper.com.bloodnbonesgaming.bnbgamingcore.ASMHelper;
import squeek.asmhelper.com.bloodnbonesgaming.bnbgamingcore.ObfHelper;

public class ClassExtensionHelper
{
    private static String currentClass;
    private static final List<String> superclasses = new ArrayList<String>();
    
    public static boolean doesClassExtend(final String className, final String targetSuperInternalClassName, final byte[] basicClass)
    {
        if (!className.equals(ClassExtensionHelper.currentClass))
        {
            ClassExtensionHelper.currentClass = className;
            ClassExtensionHelper.fillSuperclassArray(basicClass);
        }
        return ClassExtensionHelper.superclasses.contains(targetSuperInternalClassName);
    }
    
    private static void fillSuperclassArray(final byte[] basicClass)
    {
        ClassExtensionHelper.superclasses.clear();
        
        ClassReader classReader = new ClassReader(basicClass);
        
        while (classReader != null && ASMHelper.classHasSuper(classReader))
        {
            String immediateSuperName = classReader.getSuperName();
            ClassExtensionHelper.superclasses.add(immediateSuperName);
            
            try
            {
                classReader = ASMHelper.getClassReaderForClassName(ObfHelper.getInternalClassName(immediateSuperName));
            }
            catch (IOException e){}
        }
    }
}