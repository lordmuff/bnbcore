package com.bloodnbonesgaming.bnbgamingcore.core.util;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import lombok.RequiredArgsConstructor;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import squeek.asmhelper.com.bloodnbonesgaming.bnbgamingcore.ASMHelper;

public final class ObfNameHelper {

	@RequiredArgsConstructor
	public static enum Classes{

		MAPGENBASE("net.minecraft.world.gen.MapGenBase"),
		WORLDPROVIDER("net.minecraft.world.WorldProvider"),
		MAPGENSTRUCTURE("net.minecraft.world.gen.structure.MapGenStructure"),
		BNBEVENTFACTORY("com.bloodnbonesgaming.bnbgamingcore.events.BNBEventFactory"),
		DAMAGESOURCE("net.minecraft.util.DamageSource"),
		ITEMSTACK("net.minecraft.item.ItemStack"),
		ITEM("net.minecraft.item.Item"),
		ASMADDITIONHELPER("com.bloodnbonesgaming.bnbgamingcore.core.util.ASMAdditionHelper"),
		ADVANCEMENTMANAGER("net.minecraft.advancements.AdvancementManager"),
		ADVANCEMENTLIST("net.minecraft.advancements.AdvancementList"),
		FUNCTIONMANAGER("net.minecraft.advancements.FunctionManager"),
		PLAYER_ADVANCEMENTS("net.minecraft.advancements.PlayerAdvancements"),
		ADVANCEMENT_ABOUT_TO_LOAD_EVENT("com.bloodnbonesgaming.bnbgamingcore.events.AdvancementAboutToLoadEvent"),
		JSON_UTILS("net.minecraft.util.JsonUtils"),
		ADVANCEMENT$BUILDER("net.minecraft.advancements.Advancement$Builder"),
		DISPLAY_INFO("net.minecraft.advancements.DisplayInfo"),
		EXTENDED_DISPLAY_INFO("com.bloodnbonesgaming.triumph.advancements.display.ExtendedDisplayInfo"),
		ICRITERIONTRIGGER$LISTENER("net.minecraft.advancements.ICriterionTrigger$Listener"),
		WORLD_SERVER("net.minecraft.world.WorldServer"),
		MINECRAFT_SERVER("net.minecraft.server.MinecraftServer"),
		FML_COMMON_HANDLER("net.minecraftforge.fml.common.FMLCommonHandler"),
		ADVANCEMENT_PROGRESS("net.minecraft.advancements.AdvancementProgress"),
		ENTITY_RENDERER("net.minecraft.client.renderer.EntityRenderer"),
		WORLD_ENTITY_SPAWNER("net.minecraft.world.WorldEntitySpawner"),
		ABSTRACT_JAVA_LINKER("jdk.internal.dynalink.beans.AbstractJavaLinker");
		
		private final String name;

		public String getInternalName(){
			return ASMHelper.toInternalClassName(this.name);
		}
		
		public String getName()
		{
			return this.name;
		}
	}

	public enum Methods{

		GENERATE(Classes.MAPGENBASE, "func_186125_a", "(Lnet/minecraft/world/World;IILnet/minecraft/world/chunk/ChunkPrimer;)V", false),
		CREATECHUNKGENERATOR(Classes.WORLDPROVIDER, "func_186060_c", "()Lnet/minecraft/world/gen/IChunkGenerator;", false),
		SETWORLD(Classes.WORLDPROVIDER, "func_76558_a", "(Lnet/minecraft/world/World;)V", false),
		GENERATESTRUCTURE(Classes.MAPGENSTRUCTURE, "func_175794_a", "(Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/util/math/ChunkPos;)Z", false),
		ISINSIDESTRUCTURE(Classes.MAPGENSTRUCTURE, "func_175795_b", "(Lnet/minecraft/util/math/BlockPos;)Z", false),
		ISPOSITIONINSTRUCTURE(Classes.MAPGENSTRUCTURE, "func_175796_a", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z", false),
		GETCLOSESTSTRONGHOLDPOS(Classes.MAPGENSTRUCTURE, "func_180706_b", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Z)Lnet/minecraft/util/math/BlockPos;", false),
		ONMAPGENGENERATE(Classes.BNBEVENTFACTORY, "onMapGenGenerate", "(Lnet/minecraft/world/gen/MapGenBase;Lnet/minecraft/world/World;)Z", false),
		ONISINSIDESTRUCTURE(Classes.BNBEVENTFACTORY, "onIsInsideStructure", "(Lnet/minecraft/world/gen/structure/MapGenStructure;Lnet/minecraft/world/World;)Z", false),
		ONGENERATESTRUCTURE(Classes.BNBEVENTFACTORY, "onStructureGenerate", "(Lnet/minecraft/world/gen/structure/MapGenStructure;Lnet/minecraft/world/World;)Z", false),
		ONISPOSITIONINSTRUCTURE(Classes.BNBEVENTFACTORY, "onIsPositionInStructure", "(Lnet/minecraft/world/gen/structure/MapGenStructure;Lnet/minecraft/world/World;)Z", false),
		ONGETCLOSESTSTRONGHOLDPOS(Classes.BNBEVENTFACTORY, "onGetClosestStrongholdPos", "(Lnet/minecraft/world/gen/structure/MapGenStructure;Lnet/minecraft/world/World;)Z", false),
		ONCREATECHUNKGENERATOR(Classes.BNBEVENTFACTORY, "onCreateChunkGenerator", "(Lnet/minecraft/world/gen/IChunkGenerator;Lnet/minecraft/world/WorldProvider;)Lnet/minecraft/world/gen/IChunkGenerator;", false),
		ONREGISTERWORLDPRE(Classes.BNBEVENTFACTORY, "onRegisterWorldPre", "(Lnet/minecraft/world/WorldProvider;Lnet/minecraft/world/World;)V", false),
		ONREGISTERWORLDPOST(Classes.BNBEVENTFACTORY, "onRegisterWorldPost", "(Lnet/minecraft/world/WorldProvider;)V", false),
		ONINITDAMAGESOURCE(Classes.BNBEVENTFACTORY, "onInitDamageSource", "(Lnet/minecraft/util/DamageSource;)V", false),
		ONUSEITEMRIGHTCLICK(Classes.BNBEVENTFACTORY, "onUseItemRightClick", "(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/EnumHand;)Lnet/minecraft/util/ActionResult;", false),
		DAMAGESOURCE_INIT(Classes.DAMAGESOURCE, "<init>", "(Ljava/lang/String;)V", false),
		USEITEMRIGHTCLICK(Classes.ITEMSTACK, "func_77957_a", "(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/EnumHand;)Lnet/minecraft/util/ActionResult;", false),
		ONITEMRIGHTCLICK(Classes.ITEM, "func_77659_a", "(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/EnumHand;)Lnet/minecraft/util/ActionResult;", false),
		ADVANCEMENTMANAGERRELOAD(Classes.ADVANCEMENTMANAGER, "func_192779_a", "()V", false),
		LOADADVANCEMENTS(Classes.ADVANCEMENTLIST, "func_192083_a", "(Ljava/util/Map;)V", false),
		ONADVANCEMENTBUILDPRE(Classes.BNBEVENTFACTORY, "onAdvancementBuildPre", "(Ljava/util/Map;)V", false),
		ONADVANCEMENTBUILDPOST(Classes.BNBEVENTFACTORY, "onAdvancementBuildPost", "(Lnet/minecraft/advancements/AdvancementList;)V", false),
		FUNCTION_MANAGER_RELOAD(Classes.FUNCTIONMANAGER, "func_193059_f", "()V", false),
		LOAD_FUNCTIONS(Classes.FUNCTIONMANAGER, "func_193061_h", "()V", false),
		ON_FUNCTION_RELOAD_POST(Classes.BNBEVENTFACTORY, "onFunctionReloadPost", "(Lnet/minecraft/advancements/FunctionManager;)V", false),
		GRANT_CRITERION(Classes.PLAYER_ADVANCEMENTS, "func_192750_a", "(Lnet/minecraft/advancements/Advancement;Ljava/lang/String;)Z", false),
		ON_ADVANCEMENT_CRITERION_COMPLETED(Classes.BNBEVENTFACTORY, "onAdvancementCriterionCompleted", "(Lnet/minecraft/entity/player/EntityPlayerMP;Lnet/minecraft/advancements/Advancement;Lnet/minecraft/advancements/AdvancementProgress;)V", false),
		LOAD_CUSTOM_ADVANCEMENTS(Classes.ADVANCEMENTMANAGER, "func_192781_c", "()Ljava/util/Map;", false),
		LOAD_BUILTIN_ADVANCEMENTS(Classes.ADVANCEMENTMANAGER, "func_192777_a", "(Ljava/util/Map;)V", false),
        ON_ADVANCEMENT_ABOUT_TO_LOAD(Classes.BNBEVENTFACTORY, "onAdvancementAboutToLoad", "(Ljava/io/Reader;Lnet/minecraft/util/ResourceLocation;)Lcom/bloodnbonesgaming/bnbgamingcore/events/AdvancementAboutToLoadEvent;", false),
        ADVANCEMENT_ABOUT_TO_LOAD_EVENT_GET_JSON(Classes.ADVANCEMENT_ABOUT_TO_LOAD_EVENT, "getJson", "()Lcom/google/gson/JsonElement;", false),
        JSON_UTILS_FROM_JSON(Classes.JSON_UTILS, "func_193839_a", "(Lcom/google/gson/Gson;Ljava/io/Reader;Ljava/lang/Class;)Ljava/lang/Object;", false),
        ADVANCEMENT$BUILDER_READ_FROM(Classes.ADVANCEMENT$BUILDER, "func_192060_b", "(Lnet/minecraft/network/PacketBuffer;)Lnet/minecraft/advancements/Advancement$Builder;", false),
        DISPLAY_INFO_READ(Classes.DISPLAY_INFO, "func_192295_b", "(Lnet/minecraft/network/PacketBuffer;)Lnet/minecraft/advancements/DisplayInfo;", false),
        EXTENDED_DISPLAY_INFO_READ(Classes.EXTENDED_DISPLAY_INFO, "read", "(Lnet/minecraft/network/PacketBuffer;)Lnet/minecraft/advancements/DisplayInfo;", false),
        LISTENER_GRANT_CRITERION(Classes.ICRITERIONTRIGGER$LISTENER, "func_192159_a", "(Lnet/minecraft/advancements/PlayerAdvancements;)V", false),
        ON_GRANT_CRITERION(Classes.BNBEVENTFACTORY, "onCriterionGranted", "(Lnet/minecraft/advancements/PlayerAdvancements;Lnet/minecraft/advancements/Advancement;Ljava/lang/String;)Z", false),
        WORLD_SERVER_TICK(Classes.WORLD_SERVER, "func_72835_b", "()V", false),
        WORLD_SERVER_UPDATE_ENTITIES(Classes.WORLD_SERVER, "func_72939_s", "()V", false),
        ON_WORLD_SERVER_TICK_PRE(Classes.BNBEVENTFACTORY, "onWorldServerTickPre", "()Z", false),
        ON_WORLD_SERVER_UPDATE_ENTITIES_PRE(Classes.BNBEVENTFACTORY, "onWorldServerUpdateEntitiesPre", "()Z", false),
        MINECRAFT_SERVER_UPDATE_TIME_LIGHT_ENTITIES(Classes.MINECRAFT_SERVER, "func_71190_q", "()V", false),
        ON_WORLD_SERVER_FORGE_POST_EVENT(Classes.BNBEVENTFACTORY, "onMinecraftServerPostTickEvent", "()Z", false),
        FML_COMMON_HANDLER_INSTANCE(Classes.FML_COMMON_HANDLER, "instance", "()Lnet/minecraftforge/fml/common/FMLCommonHandler;", false),
        FML_COMMON_HANDLER_PRE_WORLD_TICK(Classes.FML_COMMON_HANDLER, "onPreWorldTick", "(Lnet/minecraft/world/World;)V", false),
        FML_COMMON_HANDLER_POST_WORLD_TICK(Classes.FML_COMMON_HANDLER, "onPostWorldTick", "(Lnet/minecraft/world/World;)V", false),
        SHOULD_BE_VISIBLE(Classes.PLAYER_ADVANCEMENTS, "func_192738_c", "(Lnet/minecraft/advancements/Advancement;)Z", false),
        ON_ADVANCEMENT_VISIBILITY_EVENT(Classes.BNBEVENTFACTORY, "onAdvancementVisibilityEvent", "(Lnet/minecraft/advancements/Advancement;Lnet/minecraft/advancements/PlayerAdvancements;)Z", false),
        ON_ADVANCEMENT_COMPLETION_EVENT(Classes.BNBEVENTFACTORY, "onAdvancementCompletionEvent", "(Lnet/minecraft/advancements/AdvancementProgress;)Z", false),
        ADVANCEMENT_PROGRESS_IS_DONE(Classes.ADVANCEMENT_PROGRESS, "isDone", "()Z", false),
        ON_HURT_CAMERA_EFFECT_EVENT(Classes.BNBEVENTFACTORY, "onHurtCameraEffectEvent", "()V", false),
        HURT_CAMERA_EFFECT(Classes.ENTITY_RENDERER, "func_78482_e", "(F)V", false),
        ON_MOB_SPAWNING_EVENT(Classes.BNBEVENTFACTORY,  "onMobSpawningEvent", "(Lnet/minecraft/world/WorldEntitySpawner;Lnet/minecraft/world/WorldServer;ZZZ)I", false),
        FIND_CHUNKS_FOR_SPAWNING(Classes.WORLD_ENTITY_SPAWNER, "func_77192_a", "(Lnet/minecraft/world/WorldServer;ZZZ)I", false),
        ADD_MEMBER_METHOD(Classes.ABSTRACT_JAVA_LINKER, "addMember", "(Ljava/lang/String;Ljava/lang/reflect/AccessibleObject;Ljava/util/Map;)V", false);

		private final Classes clazz;
		private final String name;
		private final String descriptor;
		private final boolean isInterface;
		
		Methods(final Classes clazz, final String name, final String descriptor, final boolean isInterface)
		{
			this.clazz = clazz;
			this.name = name;
			this.descriptor = descriptor;
			this.isInterface = isInterface;
		}
		
		public String getDescriptor()
		{
			return this.descriptor;
		}

		public String getName(){
			final String internalClassName = FMLDeobfuscatingRemapper.INSTANCE.unmap(this.clazz.getInternalName());
			return FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(internalClassName, this.name, this.getDescriptor());
		}

		public MethodInsnNode toInsnNode(final int opCode){
			return new MethodInsnNode(opCode, this.clazz.getInternalName(), this.getName(), this.getDescriptor(), this.isInterface);
		}

		public boolean matches(final MethodNode node){
			return node.name.equals(this.getName()) && node.desc.equals(this.getDescriptor());
		}

		public boolean matches(final MethodInsnNode node){
			return node.name.equals(this.getName()) && node.desc.equals(this.getDescriptor()) && node.owner.equals(this.clazz.getInternalName());
		}

	}

	public enum Fields {

		
		/*ACTUALFILLERBLOCKS(Classes.BIOME, "actualFillerBlocks", "[Lnet/minecraft/block/state/IBlockState;"),
		STONE(Classes.BLOCKS, "field_150348_b", "Lnet/minecraft/block/Block;"),
		BIOMEGENBASE_STONE(Classes.BIOME, "field_185365_a", "Lnet/minecraft/block/state/IBlockState;"),
		WATER(Classes.BLOCKS, "field_150355_j", "Lnet/minecraft/block/Block;"),
		GRAVEL(Classes.BLOCKS, "field_150351_n", "Lnet/minecraft/block/Block;"),
		BIOMEGENBASE_GRAVEL(Classes.BIOME, "field_185368_d", "Lnet/minecraft/block/state/IBlockState;"),
		TOPBLOCK(Classes.BIOME, "field_76752_A", "Lnet/minecraft/block/state/IBlockState;"),
		FILLERBLOCK(Classes.BIOME, "field_76753_B", "Lnet/minecraft/block/state/IBlockState;"),
		OCEANTOPBLOCK(Classes.BIOME, "oceanTopBlock", "Lnet/minecraft/block/state/IBlockState;"),
		OCEANFILLERBLOCK(Classes.BIOME, "oceanFillerBlock", "Lnet/minecraft/block/state/IBlockState;"),
		GRASSCOLOR(Classes.BIOME, "grassColor", "I"),
		FOLIAGECOLOR(Classes.BIOME, "foliageColor", "I"),
		SKYCOLOR(Classes.BIOME, "skyColor", "I"),
		BIOMENAME(Classes.BIOME, "field_76791_y", "Ljava/lang/String;"),
		BASEHEIGHT(Classes.BIOME, "field_76748_D", "F"),
		HEIGHTVARIATION(Classes.BIOME, "field_76749_E", "F"),
		TEMPERATURE(Classes.BIOME, "field_76750_F", "F"),
		RAINFALL(Classes.BIOME, "field_76751_G", "F"),
		WATERCOLOR(Classes.BIOME, "field_76759_H", "I"),
		ENABLESNOW(Classes.BIOME, "field_76766_R", "Z"),
		ENABLERAIN(Classes.BIOME, "field_76765_S", "Z")*/
		
		MAPGENSTRUCTURE_WORLD(Classes.MAPGENSTRUCTURE, "field_75039_c", "Lnet/minecraft/world/World;"),
		ADVANCEMENTMANAGER_ADVANCEMENTLIST(Classes.ADVANCEMENTMANAGER, "field_192784_c", "Lnet/minecraft/advancements/AdvancementList;"),
		PLAYER_ADVANCEMENTS_PLAYER(Classes.PLAYER_ADVANCEMENTS, "field_192762_j", "Lnet/minecraft/entity/player/EntityPlayerMP;"),
		ADVANCEMENT_MANAGER_GSON(Classes.ADVANCEMENTMANAGER, "field_192783_b", "Lcom/google/gson/Gson;"),
		ADVANCEMENT(Classes.ICRITERIONTRIGGER$LISTENER, "field_192161_b", "Lnet/minecraft/advancements/Advancement;"),
		CRITERION_NAME(Classes.ICRITERIONTRIGGER$LISTENER, "field_192162_c", "Ljava/lang/String;");

		private final Classes clazz;
		private final String name;
		private final String descriptor;
		
		Fields(final Classes clazz, final String name, final String descriptor)
		{
			this.clazz = clazz;
			this.name = name;
			this.descriptor = descriptor;
		}
		
		public String getDescriptor()
		{
			return this.descriptor;
		}

		public String getName(){
			return ObfuscationReflectionHelper.remapFieldNames(this.clazz.getName(), this.name)[0];
		}

		public FieldNode toNode(final int opCode, final Object value){
			return new FieldNode(opCode, this.getName(), this.getDescriptor(), this.getDescriptor(), value);
		}

		public FieldInsnNode toInsnNode(final int opCode){
			return new FieldInsnNode(opCode, this.clazz.getInternalName(), this.getName(), this.getDescriptor());
		}

		public boolean matches(final FieldNode node){
			return node.name.equals(this.getName()) && node.desc.equals(this.getDescriptor());
		}

		public boolean matches(final FieldInsnNode node){
			return node.name.equals(this.getName()) && node.desc.equals(this.getDescriptor()) && node.owner.equals(this.clazz.getInternalName());
		}
	}
}