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
		ASMADDITIONHELPER("com.bloodnbonesgaming.bnbgamingcore.core.util.ASMAdditionHelper");
		
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
		CREATECHUNKGENERATOR(Classes.WORLDPROVIDER, "func_186060_c", "()Lnet/minecraft/world/chunk/IChunkGenerator;", false),
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
		ONCREATECHUNKGENERATOR(Classes.BNBEVENTFACTORY, "onCreateChunkGenerator", "(Lnet/minecraft/world/chunk/IChunkGenerator;Lnet/minecraft/world/WorldProvider;)Lnet/minecraft/world/chunk/IChunkGenerator;", false),
		ONREGISTERWORLDPRE(Classes.BNBEVENTFACTORY, "onRegisterWorldPre", "(Lnet/minecraft/world/WorldProvider;Lnet/minecraft/world/World;)V", false),
		ONREGISTERWORLDPOST(Classes.BNBEVENTFACTORY, "onRegisterWorldPost", "(Lnet/minecraft/world/WorldProvider;)V", false),
		ONINITDAMAGESOURCE(Classes.BNBEVENTFACTORY, "onInitDamageSource", "(Lnet/minecraft/util/DamageSource;)V", false),
		ONUSEITEMRIGHTCLICK(Classes.BNBEVENTFACTORY, "onUseItemRightClick", "(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/EnumHand;)Lnet/minecraft/util/ActionResult;", false),
		DAMAGESOURCE_INIT(Classes.DAMAGESOURCE, "<init>", "(Ljava/lang/String;)V", false),
		USEITEMRIGHTCLICK(Classes.ITEMSTACK, "func_77957_a", "(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/EnumHand;)Lnet/minecraft/util/ActionResult;", false),
		ONITEMRIGHTCLICK(Classes.ITEM, "func_77659_a", "(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/EnumHand;)Lnet/minecraft/util/ActionResult;", false);

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
		
		MAPGENSTRUCTURE_WORLD(Classes.MAPGENSTRUCTURE, "field_75039_c", "Lnet/minecraft/world/World;");

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