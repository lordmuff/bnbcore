package com.bloodnbonesgaming.bnbgamingcore.events;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class AdvancementVisibilityEvent extends Event
{
    final Advancement advancement;
    final PlayerAdvancements advancements;
    
    public AdvancementVisibilityEvent(final Advancement advancement, final PlayerAdvancements advancements)
    {
        this.advancement = advancement;
        this.advancements = advancements;
    }

    public Advancement getAdvancement()
    {
        return this.advancement;
    }

    public PlayerAdvancements getAdvancements()
    {
        return this.advancements;
    }
}