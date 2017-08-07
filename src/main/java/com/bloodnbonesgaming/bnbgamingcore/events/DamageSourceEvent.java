package com.bloodnbonesgaming.bnbgamingcore.events;

import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.common.eventhandler.Event;

public class DamageSourceEvent extends Event{
	
	final DamageSource source;
	
	protected DamageSourceEvent(final DamageSource source)
	{
		this.source = source;
	}
	
	public DamageSource getSource()
	{
		return this.source;
	}
	
	public static class Init extends DamageSourceEvent{

		protected Init(DamageSource source) {
			super(source);
		}
	}
}