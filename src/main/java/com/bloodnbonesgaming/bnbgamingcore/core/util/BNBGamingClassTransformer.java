package com.bloodnbonesgaming.bnbgamingcore.core.util;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.bloodnbonesgaming.bnbgamingcore.core.BNBGamingCorePlugin;
import com.bloodnbonesgaming.bnbgamingcore.core.module.IClassTransformerModule;
import com.bloodnbonesgaming.bnbgamingcore.core.module.ModulePerformAdditions;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMAdditionRegistry.ASMAdditionRegistryWrapper;
import com.google.common.collect.Lists;

import lombok.AccessLevel;
import lombok.Getter;
import net.minecraft.launchwrapper.IClassTransformer;

public abstract class BNBGamingClassTransformer implements IClassTransformer{

	@Getter(AccessLevel.PACKAGE)
	private static final List<BNBGamingClassTransformer> transformers = Lists.newArrayList();
	private static final Map<BNBGamingClassTransformer, List<IClassTransformerModule>> transformerModules = new IdentityHashMap<>();

	public BNBGamingClassTransformer() {
		BNBGamingClassTransformer.transformers.add(this);
		final List<IClassTransformerModule> transformerModules = this.createModules();
		if(transformerModules != null) {
			//Get submodules, and call registerAdditions on all. This happens really early so that all additions are registered by the time ASMAdditionHelper is loaded
			final ASMAdditionRegistryWrapper regWrapper = ASMAdditionRegistry.getWrapper(this.getName());
			for(final IClassTransformerModule module:transformerModules) {
				BNBGamingClassTransformer.registerTransformerModule(this, module);
				module.registerAdditions(regWrapper);
			}
		}
	}

	public abstract List<IClassTransformerModule> createModules();
	public abstract String getName();
	public abstract void setAdditionHelper(final ASMAdditionHelper helper);

	public static void registerTransformerModule(final BNBGamingClassTransformer owner, final IClassTransformerModule transformerModule)
	{
		if(!BNBGamingClassTransformer.transformerModules.containsKey(owner)) {
			BNBGamingClassTransformer.transformerModules.put(owner, new ArrayList<IClassTransformerModule>());
		}
		//Ensure performing the additions ia done first
		if(transformerModule instanceof ModulePerformAdditions) {
			BNBGamingClassTransformer.transformerModules.get(owner).add(0, transformerModule);
		} else {
			BNBGamingClassTransformer.transformerModules.get(owner).add(transformerModule);
		}
	}

	public static void disableTransformerModule(final String name)
	{
		for(final List<IClassTransformerModule> list:BNBGamingClassTransformer.transformerModules.values()){
			final Iterator<IClassTransformerModule> it = list.iterator();
			while(it.hasNext()){
				if(it.next().getModuleName().equals(name)) {
					it.remove();
				}
			}
		}
	}

	public static void disableAllTransformerModulesExcept(final List<String> names)
	{
		for(final List<IClassTransformerModule> list:BNBGamingClassTransformer.transformerModules.values()){
			final Iterator<IClassTransformerModule> it = list.iterator();
			while(it.hasNext()){
				final IClassTransformerModule module = it.next();
				if (module.canBeDisabled() && !names.contains(module.getModuleName())) {
					BNBGamingCorePlugin.log.debug("Disabled ASM module "+module.getModuleName());
					it.remove();
				}
			}
		}
	}

	public static IClassTransformerModule[] getTransformerModules()
	{
		final List<IClassTransformerModule> list = new ArrayList<>();
		for(final List<IClassTransformerModule> modules:BNBGamingClassTransformer.transformerModules.values()) {
			list.addAll(modules);
		}
		return list.toArray(new IClassTransformerModule[list.size()]);
	}

	/**
	 * Attempts to look up the owner of a submodule
	 */
	public static BNBGamingClassTransformer lookupOwner(final IClassTransformerModule module){
		for(final BNBGamingClassTransformer transformer:BNBGamingClassTransformer.transformers) {
			if(BNBGamingClassTransformer.transformerModules.get(transformer).contains(module)) {
				return transformer;
			}
		}
		return null;
	}

}
