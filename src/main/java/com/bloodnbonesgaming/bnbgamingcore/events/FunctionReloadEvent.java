package com.bloodnbonesgaming.bnbgamingcore.events;

import net.minecraft.advancements.FunctionManager;
import net.minecraftforge.fml.common.eventhandler.Event;

public class FunctionReloadEvent extends Event {
	
	protected FunctionReloadEvent(){}
	
	public static class Post extends FunctionReloadEvent {
				
		private final FunctionManager manager;
		
		public Post(final FunctionManager manager) {
			super();
			this.manager = manager;
		}
		
		public FunctionManager getFunctionManager()
		{
			return this.manager;
		}
	}
}