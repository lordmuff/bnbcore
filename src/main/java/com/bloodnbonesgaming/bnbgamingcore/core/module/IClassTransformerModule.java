package com.bloodnbonesgaming.bnbgamingcore.core.module;

import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMAdditionRegistry;
import com.bloodnbonesgaming.bnbgamingcore.core.util.BNBGamingClassTransformer;

import net.minecraft.launchwrapper.IClassTransformer;

public interface IClassTransformerModule extends IClassTransformer{

	public String[] getClassesToTransform();
	public String getModuleName();
	public boolean canBeDisabled();
	/**
	 * This is called when the parent {@link BNBGamingClassTransformer} is instantiated. All fields and methods that need to be added via ASM should be specified here.<br>
	 * DO NOT ADD THEM YOURSELF. BNBGamingLib handles it.
	 * @param registry An instance of the registry to use.
	 */
	public void registerAdditions(final ASMAdditionRegistry registry);

}
