package com.bloodnbonesgaming.bnbgamingcore.core.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;

import squeek.asmhelper.com.bloodnbonesgaming.bnbgamingcore.ASMHelper;
import squeek.asmhelper.com.bloodnbonesgaming.bnbgamingcore.ObfHelper;

public class ASMUtils {
	
	public static boolean doesClassEqualOrExtend(final String className, final ObfNameHelper.Classes clazz, final byte[] basicClass)
	{
		return ASMUtils.doesClassEqual(className, clazz) || ClassExtensionHelper.doesClassExtend(className, clazz.getInternalName(), basicClass);
	}
	
	public static boolean doesClassEqual(final String className, final ObfNameHelper.Classes clazz)
	{
		return className.equals(clazz.getName());
	}

	public static MethodNode findMethodNodeOfClass(final ClassNode classNode, final String methodName, final String methodDesc)
	{
		for (final MethodNode method : classNode.methods)
		{
			if (method.name.equals(methodName) && (methodDesc == null || method.desc.equals(methodDesc)))
			{
				return method;
			}
		}
		return null;
	}

	public static FieldNode findFieldNodeOfClass(final ClassNode classNode, final String fieldName, final String fieldDesc)
	{
		for (final FieldNode field : classNode.fields)
		{
			if ((fieldName == null || field.name.equals(fieldName)) && (fieldDesc == null || field.desc.equals(fieldDesc)))
			{
				return field;
			}
		}
		return null;
	}

	/**
	 * @param classNode
	 *            - ClassNode the field should be found in
	 * @param fieldName
	 *            - Name of field to add annotation to
	 * @param fieldDesc
	 *            - Description of field to add annotation to
	 * @param annotationClass
	 *            - Class of annotation
	 * @param annotationFields
	 *            - Name Value pairs for the annotation. Each Name Value pair
	 *            must be consecutive
	 */
	public static boolean addAnnotationToField(final ClassNode classNode, final String fieldName, final String fieldDesc, final Class annotationClass, final Object... annotationFields)
	{
		// Find the field
		final FieldNode fieldNode = ASMUtils.findFieldNodeOfClass(classNode, fieldName, fieldDesc);

		// Check that the field was found correctly
		if (fieldNode == null)
		{
			return false;
		}

		// Create a new annotation node
		final AnnotationNode annotation = new AnnotationNode(Type.getDescriptor(annotationClass));

		// Create a List of values for the annotation, as the annotation.values
		// list WILL BE NULL. Consecutive value pairs (name, value)
		final List<Object> values = new ArrayList<Object>();

		// Add fields to the values list we just created
		for (int i = 0; i < annotationFields.length; i++)
		{
			values.add(i, annotationFields[i]);
		}

		// Add values list to the annotation
		annotation.values = values;

		// Check if the FieldNode's annotation list is null, this will be true
		// if the field has no annotations
		if (fieldNode.visibleAnnotations == null)
		{
			// Create a new List of type AnnotationNode and add our
			// AnnotationNode to it
			final List<AnnotationNode> annotationList = new ArrayList<AnnotationNode>();
			annotationList.add(annotation);

			// Add the annotationList to the FieldNode
			fieldNode.visibleAnnotations = annotationList;
		}
		else
		{
			// Add our AnnotationNode to the FieldNode's annotations list
			fieldNode.visibleAnnotations.add(annotation);
		}
		return true;
	}

	public static boolean makeFieldTransient(final ClassNode classNode, final String fieldName, final String fieldDesc)
	{
		final FieldNode field = ASMUtils.findFieldNodeOfClass(classNode, fieldName, fieldDesc);

		if (field == null)
		{
			return false;
		}

		field.access = field.access | Opcodes.ACC_TRANSIENT;
		return true;
	}

	public static boolean doesClassImplement(final ClassReader classReader, final String targetInterfaceInternalClassName)
	{
		final List<String> immediateInterfaces = Arrays.asList(classReader.getInterfaces());
		for (final String immediateInterface : immediateInterfaces)
		{
			if (immediateInterface.equals(targetInterfaceInternalClassName))
			{
				return true;
			}
		}

		try
		{
			if (ASMHelper.classHasSuper(classReader))
			{
				return ASMUtils.doesClassImplement(ASMHelper.getClassReaderForClassName(ObfHelper.getInternalClassName(classReader.getSuperName())), targetInterfaceInternalClassName);
			}
		}
		catch (final IOException e)
		{
			//DCCore.log.error("Errored while trying to check if class implemented interface: raw = " + classReader.getSuperName() + ", obf = " + ObfHelper.getInternalClassName(classReader.getSuperName()), e);
			//e.printStackTrace();
			//throw new RuntimeException("raw = " + classReader.getSuperName() + ", obf = " + ObfHelper.getInternalClassName(classReader.getSuperName()), e);
		}
		return false;
	}
	
	public static LabelNode findLabelNode(final InsnList instructions, final int count)
	{
	    final Iterator<AbstractInsnNode> iterator = instructions.iterator();
	    int i = 0;
	    
	    while (iterator.hasNext())
        {
            final AbstractInsnNode node = iterator.next();
            
            if (node.getType() == AbstractInsnNode.LABEL)
            {
                if (i++ == count)
                {
                    return (LabelNode) node;
                }
            }
        }
	    
	    return null;
	}
	
	public static LabelNode findPreviousLabelNode(final AbstractInsnNode start)
	{
	    AbstractInsnNode node = start;
	    
	    while (node != null)
	    {
	        if (node.getType() == AbstractInsnNode.LABEL)
	        {
	            return (LabelNode) node;
	        }
	        else
	        {
	            node = node.getPrevious();
	        }
	    }
	    return null;
	}
}