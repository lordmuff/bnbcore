package com.bloodnbonesgaming.bnbgamingcore.events;

import net.minecraft.advancements.AdvancementProgress;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class AdvancementCompletionEvent extends Event{
	
	private final AdvancementProgress progress;
	
	public AdvancementCompletionEvent(final AdvancementProgress progress)
	{
		this.progress = progress;
	}
	
	public AdvancementProgress getAdvancementProgress()
	{
		return this.progress;
	}
}