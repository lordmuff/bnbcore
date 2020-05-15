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
	ADVANCEMENTBUILDEVENT_POST("AdvancementBuildEvent$Post", new String[]{"advancementBuildEventPost"}),
	FUNCTION_RELOAD_EVENT("FunctionReloadEvent", new String[]{"functionReloadEventPost"}),
	FUNCTION_RELOAD_EVENT_POST("FunctionReloadEvent$Post", new String[]{"functionReloadEventPost"}),
	ADVANCEMENT_CRITERION_COMPLETED_EVENT("AdvancementCriterionCompletedEvent", new String[]{"advancementCriterionCompletedEvent"}),
	ADVANCEMENT_ABOUT_TO_LOAD_EVENT("AdvancementAboutToLoadEvent", new String[]{"advancementAboutToLoadEvent"}),
	ADVANCEMENT_SYNC("AdvancementSync", new String[]{"advancementSync"}),
	GRANT_CRITERION_EVENT("GrantCriterionEvent", new String[]{"grantCriterionEvent"}),
	WORLD_SERVER_EVENTS("WorldServerEvents", new String[]{"worldServerEvents"}),
	MINECRAFT_SERVER_POST_TICK_EVENT("MinecraftServerPostTickEvent", new String[]{"minecraftServerPostTickEvent"}),
	ADVANCEMENT_VISIBILITY_EVENT("AdvancementVisibilityEvent", new String[]{"advancementVisibilityEvent"}),
	ADVANCEMENT_COMPLETION_EVENT("AdvancementCompletionEvent", new String[]{"advancementCompletionEvent"}),
	HURT_CAMERA_EFFECT_EVENT("HurtCameraEffectEvent", new String[]{"hurtCameraEffectEvent"}),
	MOB_SPAWNING_EVENT("MobSpawningEvent", new String[]{"mobSpawningEvent"});
	
	
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