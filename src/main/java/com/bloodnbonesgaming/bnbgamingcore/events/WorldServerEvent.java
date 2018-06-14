package com.bloodnbonesgaming.bnbgamingcore.events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class WorldServerEvent extends Event
{
    protected WorldServerEvent() {}
    
    @Override
    public boolean isCancelable() {
        return true;
    }
    
    public static class Tick extends WorldServerEvent {
        
        public static class Pre extends Tick {
            
            public Pre() {
                super();
            }
        }
    }
    
    public static class UpdateEntities extends WorldServerEvent {
        
        public static class Pre extends UpdateEntities {
            
            public Pre() {
                super();
            }
        }
    }
}