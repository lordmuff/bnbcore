package com.bloodnbonesgaming.bnbgamingcore.events;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraftforge.fml.common.eventhandler.Event;

public class MapGenStructureEvent extends Event {
	
	private final World world;
	private final MapGenStructure structure;
	
	protected MapGenStructureEvent(final World world, final MapGenStructure structure)
	{
		this.world = world;
		this.structure = structure;
	}
	
	public World getWorld()
	{
		return this.world;
	}
	
	public MapGenStructure getStructure()
	{
		return this.structure;
	}
	
	public static class GenerateStructure extends MapGenStructureEvent {

		public GenerateStructure(World world, MapGenStructure structure) {
			super(world, structure);
		}
	}
	
	public static class IsInsideStructure extends MapGenStructureEvent {
		
		public IsInsideStructure(World world, MapGenStructure structure)
		{
			super(world, structure);
		}
	}
	
	public static class IsPositionInStructure extends MapGenStructureEvent {
		
		public IsPositionInStructure(World world, MapGenStructure structure)
		{
			super(world, structure);
		}
	}
	
	public static class GetClosestStrongholdPos extends MapGenStructureEvent {
		
		public GetClosestStrongholdPos(World world, MapGenStructure structure)
		{
			super(world, structure);
		}
	}
}