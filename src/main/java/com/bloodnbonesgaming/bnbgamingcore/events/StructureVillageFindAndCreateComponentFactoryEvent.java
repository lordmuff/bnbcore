package com.bloodnbonesgaming.bnbgamingcore.events;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.fml.common.eventhandler.Event;

public class StructureVillageFindAndCreateComponentFactoryEvent extends Event {
	
	public StructureVillagePieces.Village component;
	public final World world;
	public final IBlockState state;
	public final int x;
	public final int y;
	public final int z;
	public final StructureBoundingBox boundingBox;
	
	public StructureVillageFindAndCreateComponentFactoryEvent(StructureVillagePieces.Village component, World world, IBlockState state, int x, int y, int z, StructureBoundingBox boundingBox) {
		this.component = component;
		this.world = world;
		this.state = state;
		this.x = x;
		this.y = y;
		this.z = z;
		this.boundingBox = boundingBox;
	}
}
