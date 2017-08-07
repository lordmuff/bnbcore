package com.bloodnbonesgaming.bnbgamingcore.core.util;

import com.bloodnbonesgaming.bnbgamingcore.core.BNBGamingCorePlugin;

public class ASMDebugHelper {
	
	public static void logTransforming(final String methodName, final String className)
	{
		BNBGamingCorePlugin.log.debug("Transforming method " + methodName + " in class " + className);
	}
	
	public static void logNotFound(final String methodName, final String className)
	{
		BNBGamingCorePlugin.log.debug("Could not find method " + methodName + " in class " + className);
	}
	
	public static void unexpectedMethodInstructionPattern(final String methodName, final String className)
	{
		BNBGamingCorePlugin.log.debug("Unexpected instruction pattern in method: " + methodName + " in class: " + className);
	}
}