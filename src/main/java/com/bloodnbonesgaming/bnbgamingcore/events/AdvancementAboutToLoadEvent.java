package com.bloodnbonesgaming.bnbgamingcore.events;

import com.google.gson.JsonElement;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Fired when a script file is about to be read and used to construct an Advancement$Builder. <br>
 * <br>
 * {@link #location} is the {@link ResourceLocation} for the advancement. <br>
 * <br>
 * This event is {@link Cancelable}. <br>
 * If canceled, the script file will not be read, and the Advancement$Builder will not be constructed. <br>
 * 
 * @author Blargerist
 *
 */
@Cancelable
public class AdvancementAboutToLoadEvent extends Event
{
    private final ResourceLocation location;
    private JsonElement json;

    public AdvancementAboutToLoadEvent(final ResourceLocation location, final JsonElement json)
    {
        this.location = location;
        this.json = json;
    }

    public ResourceLocation getLocation()
    {
        return this.location;
    }
    
    public JsonElement getJson()
    {
        return this.json;
    }
    
    public void setJson(final JsonElement json)
    {
        this.json = json;
    }
}
