package com.bloodnbonesgaming.bnbgamingcore.events;

import java.util.Map;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.Advancement.Builder;
import net.minecraft.advancements.AdvancementList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.Event;

public class AdvancementBuildEvent extends Event{
	
	protected AdvancementBuildEvent(){}
	
	public static class Pre extends AdvancementBuildEvent {

		private final Map<ResourceLocation, Advancement.Builder> advancementMap;
		
		public Pre(Map<ResourceLocation, Builder> advancementMap) {
			super();
			this.advancementMap = advancementMap;
		}
		
		public Map<ResourceLocation, Advancement.Builder> getAdvancementMap()
		{
			return this.advancementMap;
		}
	}
	
	public static class Post extends AdvancementBuildEvent {

		private final AdvancementList advancementList;
		
		public Post(final AdvancementList advancementList) {
			super();
			this.advancementList = advancementList;
		}
		
		public AdvancementList getAdvancementList()
		{
			return this.advancementList;
		}
	}
}