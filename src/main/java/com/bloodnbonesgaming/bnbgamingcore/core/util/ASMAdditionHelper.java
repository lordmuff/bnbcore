package com.bloodnbonesgaming.bnbgamingcore.core.util;

/**
 * Methods in this class are populated at runtime via ASM. They are completely replaced, so it actually doesn't matter if they are empty or not.
 * This allows for interaction with fields and methods added via ASM without the need for dummy classes and/or reflection.
 * Note that submods are provided with a wrapper that appends a String to the beginning of all field and method names, to help alleviate inter-mod conflictions regarding naming.
 */
public abstract class ASMAdditionHelper {

	static{
		for(final BNBGamingClassTransformer transformer:BNBGamingClassTransformer.getTransformers()) {
			transformer.setAdditionHelper(new ASMAdditionHelperWrapper(transformer.getName()));
		}
	}

	public Object get(final Object obj, final String fieldName){
		return null;
	}

	public void set(final Object obj, final String fieldName, final Object value){}

	public Object invoke(final Object obj, final String methodName, final Object ... parameters){
		return null;
	}

	public static class ASMAdditionHelperWrapper extends ASMAdditionHelper{

		private final String wrap;

		public ASMAdditionHelperWrapper(final String wrap) {
			this.wrap = wrap;
		}

		@Override
		public Object get(final Object obj, final String fieldName) {
			return super.get(obj, this.wrap+fieldName);
		}

		@Override
		public void set(final Object obj, final String fieldName, final Object value) {
			super.set(obj, this.wrap+fieldName, value);
		}

		@Override
		public Object invoke(final Object obj, final String methodName, final Object... parameters) {
			return super.invoke(obj, this.wrap+methodName, parameters);
		}

	}

}
