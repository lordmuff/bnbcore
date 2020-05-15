package com.bloodnbonesgaming.bnbgamingcore.events;

import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class MobSpawningEvent extends Event {
	
	public final WorldEntitySpawner spawner;
	public final WorldServer server;
	public final boolean spawnHostileMobs;
	public final boolean spawnPeacefulMobs;
	public final boolean spawnOnSetTickRate;
	
	public MobSpawningEvent(final WorldEntitySpawner spawner, final WorldServer server, final boolean spawnHostileMobs, final boolean spawnPeacefulMobs, final boolean spawnOnSetTickRate)
	{
		this.spawner = spawner;
		this.server = server;
		this.spawnHostileMobs = spawnHostileMobs;
		this.spawnPeacefulMobs = spawnHostileMobs;
		this.spawnOnSetTickRate = spawnOnSetTickRate;
	}
}
