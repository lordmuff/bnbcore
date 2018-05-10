package com.bloodnbonesgaming.bnbgamingcore.core;

import java.util.List;

import com.bloodnbonesgaming.bnbgamingcore.core.module.IClassTransformerModule;
import com.bloodnbonesgaming.bnbgamingcore.core.module.ModuleDamageSourceClass;
import com.bloodnbonesgaming.bnbgamingcore.core.module.ModuleItemRightClickEvent;
import com.bloodnbonesgaming.bnbgamingcore.core.module.advancements.ModuleAdvancementAboutToLoadEvent;
import com.bloodnbonesgaming.bnbgamingcore.core.module.advancements.ModuleAdvancementBuildEventPost;
import com.bloodnbonesgaming.bnbgamingcore.core.module.advancements.ModuleAdvancementBuildEventPre;
import com.bloodnbonesgaming.bnbgamingcore.core.module.advancements.ModuleAdvancementCriterionCompletedEvent;
import com.bloodnbonesgaming.bnbgamingcore.core.module.advancements.ModuleAdvancementSync;
import com.bloodnbonesgaming.bnbgamingcore.core.module.advancements.ModuleGrantCriterionEvent;
import com.bloodnbonesgaming.bnbgamingcore.core.module.function.ModuleFunctionReloadEventPost;
import com.bloodnbonesgaming.bnbgamingcore.core.module.gen.ModuleMapGenBaseClass;
import com.bloodnbonesgaming.bnbgamingcore.core.module.gen.ModuleMapGenStructureClass;
import com.bloodnbonesgaming.bnbgamingcore.core.module.worldprovider.ModuleCreateChunkGeneratorEvent;
import com.bloodnbonesgaming.bnbgamingcore.core.module.worldprovider.ModuleRegisterWorldEvent;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMAdditionHelper;
import com.bloodnbonesgaming.bnbgamingcore.core.util.BNBGamingClassTransformer;
import com.google.common.collect.Lists;

import squeek.asmhelper.com.bloodnbonesgaming.bnbgamingcore.ObfHelper;


public class BNBGamingCoreClassTransformer extends BNBGamingClassTransformer{

	@Override
	public byte[] transform(final String name, final String transformedName, byte[] basicClass)
	{
		for (final IClassTransformerModule transformerModule : BNBGamingClassTransformer.getTransformerModules())
		{
			for (final String classToTransform : transformerModule.getClassesToTransform())
			{
				if (classToTransform.equals(transformedName) || classToTransform.equals(ObfHelper.getInternalClassName(transformedName)) || classToTransform.equals("*"))
				{
					basicClass = transformerModule.transform(name, transformedName, basicClass);
				}
			}
		}
		return basicClass;
	}

	@Override
	public List<IClassTransformerModule> createModules() {
		return Lists.newArrayList(new ModuleItemRightClickEvent(), new ModuleDamageSourceClass(), 
				new ModuleRegisterWorldEvent(), new ModuleMapGenBaseClass(), new ModuleMapGenStructureClass(), new ModuleCreateChunkGeneratorEvent(), new ModuleAdvancementBuildEventPre(), 
				new ModuleAdvancementBuildEventPost(), new ModuleFunctionReloadEventPost(), new ModuleAdvancementCriterionCompletedEvent(), new ModuleAdvancementAboutToLoadEvent(), 
				new ModuleAdvancementSync(), new ModuleGrantCriterionEvent());
	}

	@Override
	public String getName() {
		return "bnbcorect";
	}

	@Override
	public void setAdditionHelper(final ASMAdditionHelper helper) {}

}
