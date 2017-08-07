package com.bloodnbonesgaming.bnbgamingcore.events;

import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraftforge.fml.common.eventhandler.Event;

public class WorldProviderEvent extends Event {
	
	private final WorldProvider worldProvider;
	
	protected WorldProviderEvent(final WorldProvider worldProvider)
	{
		this.worldProvider = worldProvider;
	}
	
	public WorldProvider getWorldProvider()
	{
		return this.worldProvider;
	}
	
	@Override
	public boolean isCancelable() {
		return false;
	}
	
	public static class CreateChunkGenerator extends WorldProviderEvent {

		protected IChunkGenerator chunkGenerator;
		
		public CreateChunkGenerator(final IChunkGenerator chunkGenerator, final WorldProvider worldProvider) {
			super(worldProvider);
			this.chunkGenerator = chunkGenerator;
		}
		
		public void setChunkGenerator(final IChunkGenerator chunkGenerator)
		{
			this.chunkGenerator = chunkGenerator;
		}
		
		public IChunkGenerator getChunkGenerator()
		{
			return this.chunkGenerator;
		}
	}
	
	public static class RegisterWorld extends WorldProviderEvent {
		
		public RegisterWorld(final WorldProvider worldProvider)
		{
			super(worldProvider);
		}
		
		public static class Pre extends RegisterWorld {

			final World world;
			
			public Pre(WorldProvider worldProvider, final World world) {
				super(worldProvider);
				this.world = world;
			}
			
			public World getWorld()
			{
				return this.world;
			}
		}
		
		public static class Post extends RegisterWorld {
			
			public Post(WorldProvider worldProvider) {
				super(worldProvider);
			}
		}
	}
}