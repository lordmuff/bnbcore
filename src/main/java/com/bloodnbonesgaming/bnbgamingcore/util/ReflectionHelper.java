package com.bloodnbonesgaming.bnbgamingcore.util;

public class ReflectionHelper {

	public static StackTraceElement retrieveCallingStackTraceElement(){
		return ReflectionHelper.retrieveCallingStackTraceElement(3);
	}

	public static StackTraceElement retrieveCallingStackTraceElement(final int depth){
		return new Throwable().getStackTrace()[depth];
	}

}
