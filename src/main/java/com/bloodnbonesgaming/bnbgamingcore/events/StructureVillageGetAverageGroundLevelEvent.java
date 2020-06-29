package com.bloodnbonesgaming.bnbgamingcore.events;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.fml.common.eventhandler.Event;

public class StructureVillageGetAverageGroundLevelEvent extends Event {
	
	public final StructureVillagePieces.Village piece;
	public final World world;
	public final StructureBoundingBox boundingBox;
	public int averageGroundLevel = -1;
	
	public StructureVillageGetAverageGroundLevelEvent(StructureVillagePieces.Village piece, World world, StructureBoundingBox boundingBox) {
		this.piece = piece;
		this.world = world;
		this.boundingBox = boundingBox;
	}
}
