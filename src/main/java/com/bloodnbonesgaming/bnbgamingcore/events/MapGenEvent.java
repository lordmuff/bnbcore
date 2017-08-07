package com.bloodnbonesgaming.bnbgamingcore.events;

import net.minecraft.world.World;
import net.minecraft.world.gen.MapGenBase;
import net.minecraftforge.fml.common.eventhandler.Event;

public class MapGenEvent extends Event {
	
	private final World world;
	private final MapGenBase mapGen;
	
	protected MapGenEvent(final MapGenBase mapGen, final World world)
	{
		this.world = world;
		this.mapGen = mapGen;
	}
	
	public World getWorld()
	{
		return this.world;
	}
	
	public MapGenBase getMapGen()
	{
		return this.mapGen;
	}
	
	public static class Generate extends MapGenEvent {

		public Generate(final MapGenBase mapGen, World world) {
			super(mapGen, world);
		}
	}
}