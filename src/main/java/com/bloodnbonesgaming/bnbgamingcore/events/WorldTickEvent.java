package com.bloodnbonesgaming.bnbgamingcore.events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class WorldTickEvent extends Event
{
    protected WorldTickEvent() {}
    
    @Override
    public boolean isCancelable() {
        return true;
    }
    
    public static class Pre extends WorldTickEvent {

        public Pre() {
            super();
        }
    }
    
    public static class Post extends WorldTickEvent {
        
        public Post()
        {
            super();
        }
    }
}