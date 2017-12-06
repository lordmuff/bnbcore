package com.bloodnbonesgaming.bnbgamingcore.events;

import java.io.IOException;
import java.util.Map;

import com.google.gson.stream.JsonReader;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementList;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.FunctionManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraftforge.common.MinecraftForge;

public class BNBEventFactory {
	
	public static boolean onMapGenGenerate(final MapGenBase mapGen, final World world)
	{
		return MinecraftForge.EVENT_BUS.post(new MapGenEvent.Generate(mapGen, world));
	}
	
	public static boolean onStructureGenerate(final MapGenStructure structure, final World world)
	{
		return MinecraftForge.EVENT_BUS.post(new MapGenStructureEvent.GenerateStructure(world, structure));
	}
	
	public static boolean onIsInsideStructure(final MapGenStructure structure, final World world)
	{
		return MinecraftForge.EVENT_BUS.post(new MapGenStructureEvent.IsInsideStructure(world, structure));
	}
	
	public static boolean onIsPositionInStructure(final MapGenStructure structure, final World world)
	{
		return MinecraftForge.EVENT_BUS.post(new MapGenStructureEvent.IsPositionInStructure(world, structure));
	}
	
	public static boolean onGetClosestStrongholdPos(final MapGenStructure structure, final World world)
	{
		return MinecraftForge.EVENT_BUS.post(new MapGenStructureEvent.GetClosestStrongholdPos(world, structure));
	}
	
	public static IChunkGenerator onCreateChunkGenerator(final IChunkGenerator chunkGenerator, final WorldProvider worldProvider)
	{
		final WorldProviderEvent.CreateChunkGenerator event = new WorldProviderEvent.CreateChunkGenerator(chunkGenerator, worldProvider);
		MinecraftForge.EVENT_BUS.post(event);
		return event.chunkGenerator;
	}
	
	public static void onRegisterWorldPre(final WorldProvider worldProvider, final World world)
	{
		MinecraftForge.EVENT_BUS.post(new WorldProviderEvent.RegisterWorld.Pre(worldProvider, world));
	}
	
	public static void onRegisterWorldPost(final WorldProvider worldProvider)
	{
		MinecraftForge.EVENT_BUS.post(new WorldProviderEvent.RegisterWorld.Post(worldProvider));
	}
	
	public static void onInitDamageSource(final DamageSource source)
	{
		MinecraftForge.EVENT_BUS.post(new DamageSourceEvent.Init(source));
	}

	public static ActionResult<ItemStack> onUseItemRightClick(final World world, final EntityPlayer player, final EnumHand hand)
	{
		final ItemStack itemStack = player.getHeldItem(hand);
		final OnItemRightClickEvent event = new OnItemRightClickEvent(world, player, itemStack, hand);

		if (MinecraftForge.EVENT_BUS.post(event))
		{
			return new ActionResult<ItemStack>(event.getEnumResult(), itemStack);
		}
		return itemStack.getItem().onItemRightClick(world, player, hand);
	}
	
	public static void onAdvancementBuildPre(final Map<ResourceLocation, Advancement.Builder> advancementMap)
	{
		MinecraftForge.EVENT_BUS.post(new AdvancementBuildEvent.Pre(advancementMap));
	}
	
	public static void onAdvancementBuildPost(final AdvancementList advancementList)
	{
		MinecraftForge.EVENT_BUS.post(new AdvancementBuildEvent.Post(advancementList));
	}
	
	public static void onFunctionReloadPost(final FunctionManager manager)
	{
		MinecraftForge.EVENT_BUS.post(new FunctionReloadEvent.Post(manager));
	}
	
	public static void onAdvancementCriterionCompleted(final EntityPlayerMP player, final Advancement advancement, final AdvancementProgress progress)
	{
		MinecraftForge.EVENT_BUS.post(new AdvancementCriterionCompletedEvent(player, advancement, progress));
	}
    
    public static AdvancementAboutToLoadEvent onAdvancementAboutToLoad(java.io.Reader reader, ResourceLocation location) throws IOException
    {
//        final String jsonString = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
//        java.io.StringReader stringReader = new java.io.StringReader(jsonString);
        JsonReader jsonReader = new JsonReader(reader);
        jsonReader.setLenient(false);
//        json = gson.getAdapter(Advancement.Builder.class).toJsonTree(null);
        
        com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
        com.google.gson.JsonElement json = parser.parse(jsonReader);
        
        AdvancementAboutToLoadEvent event = new AdvancementAboutToLoadEvent(location, json);
        return event;
    }
}