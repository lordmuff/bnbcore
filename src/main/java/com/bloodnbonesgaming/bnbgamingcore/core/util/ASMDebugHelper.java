package com.bloodnbonesgaming.bnbgamingcore.core.util;

import com.bloodnbonesgaming.bnbgamingcore.core.BNBGamingCorePlugin;

public class ASMDebugHelper {
	
	public static void logAttemptingTransform(final String methodName, final String className)
	{
		BNBGamingCorePlugin.log.debug("Attempting to transform method " + methodName + " in class " + className);
	}
	
	public static void logSuccessfulTransform(final String methodName, final String className)
	{
		BNBGamingCorePlugin.log.debug("Successfully transformed method " + methodName + " in class " + className);
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