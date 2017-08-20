package com.bloodnbonesgaming.bnbgamingcore.core.util;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BNBCoreEvents {
	
	DAMAGESOUCE("DamageSourceEvent", new String[]{"buildDamageSourceList"}),
	DAMAGESOURCE_INIT("DamageSourceEvent$Init", new String[]{"buildDamageSourceList"}),
	MAPGENEVENT("MapGenEvent", new String[]{"transformMapGenBase"}),
	MAPGENEVENT_GENERATE("MapGenEvent$Generate", new String[]{"transformMapGenBase"}),
	MAPGENSTRUCTUREEVENT("MapGenStructureEvent", new String[]{"transformMapGenStructure"}),
	MAPGENSTRUCTUREEVENT_GENERATESTRUCTURE("MapGenStructureEvent$GenerateStructure", new String[]{"transformMapGenStructure"}),
	MAPGENSTRUCTUREEVENT_ISINSIDESTRUCTURE("MapGenStructureEvent$IsInsideStructure", new String[]{"transformMapGenStructure"}),
	MAPGENSTRUCTUREEVENT_ISPOSITIONINSTRUCTURE("MapGenStructureEvent$IsPositionInStructure", new String[]{"transformMapGenStructure"}),
	MAPGENSTRUCTUREEVENT_GETCLOSESTSTRONGHOLDPOS("MapGenStructureEvent$GetClosestStrongholdPos", new String[]{"transformMapGenStructure"}),
	ONITEMRIGHTCLICKEVENT("OnItemRightClickEvent", new String[]{"itemRightClickEvent"}),
	WORLDPROVIDEREVENT("WorldProviderEvent", new String[]{"createChunkGeneratorEvent", "registerWorldEvent"}),
	WORLDPROVIDEREVENT_CREATECHUNKGENERATOR("WorldProviderEvent$CreateChunkGenerator", new String[]{"createChunkGeneratorEvent"}),
	WORLDPROVIDEREVENT_REGISTERWORLD("WorldProviderEvent$RegisterWorld", new String[]{"registerWorldEvent"}),
	WORLDPROVIDEREVENT_REGISTERWORLD_PRE("WorldProviderEvent$RegisterWorld$Pre", new String[]{"registerWorldEvent"}),
	WORLDPROVIDEREVENT_REGISTERWORLD_POST("WorldProviderEvent$RegisterWorld$Post", new String[]{"registerWorldEvent"}),
	ADVANCEMENTBUILDEVENT("AdvancementBuildEvent", new String[]{"advancementBuildEventPre", "advancementBuildEventPost"}),
	ADVANCEMENTBUILDEVENT_PRE("AdvancementBuildEvent$Pre", new String[]{"advancementBuildEventPre"}),
	ADVANCEMENTBUILDEVENT_POST("AdvancementBuildEvent$Post", new String[]{"advancementBuildEventPost"});
	
	
	@Getter
	private final String eventClass;
	@Getter
	private final String[] modules;
	
	public static List<String> getModulesToEnable(final String eventClass)
	{
		for (final BNBCoreEvents event : BNBCoreEvents.values())
		{
			if (event.getEventClass().equalsIgnoreCase(eventClass.trim()))
			{
				return Arrays.asList(event.getModules());
			}
		}
		return null;
	}
}