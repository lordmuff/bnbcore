package com.bloodnbonesgaming.bnbgamingcore.events;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class GrantCriterionEvent extends Event
{
    private final PlayerAdvancements playerAdvancements;
    private final Advancement advancement;
    private final String criterionName;

    public GrantCriterionEvent(final PlayerAdvancements playerAdvancements, final Advancement advancement, final String criterionName)
    {
        this.playerAdvancements = playerAdvancements;
        this.advancement = advancement;
        this.criterionName = criterionName;
    }

    public PlayerAdvancements getPlayerAdvancements()
    {
        return this.playerAdvancements;
    }
    
    public Advancement getAdvancement()
    {
        return this.advancement;
    }
    
    public String getCriterionName()
    {
        return this.criterionName;
    }
}