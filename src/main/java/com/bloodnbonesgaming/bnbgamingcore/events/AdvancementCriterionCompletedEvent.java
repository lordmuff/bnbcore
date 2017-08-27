package com.bloodnbonesgaming.bnbgamingcore.events;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Event;

public class AdvancementCriterionCompletedEvent extends Event{
	
	private final EntityPlayerMP player;
	private final Advancement advancement;
	private final AdvancementProgress progress;
	
	public AdvancementCriterionCompletedEvent(final EntityPlayerMP player, final Advancement advancement, final AdvancementProgress progress)
	{
		this.player = player;
		this.advancement = advancement;
		this.progress = progress;
	}
	
	public EntityPlayerMP getPlayer()
	{
		return this.player;
	}
	
	public Advancement getAdvancement()
	{
		return this.advancement;
	}
	
	public AdvancementProgress getAdvancementProgress()
	{
		return this.progress;
	}
}