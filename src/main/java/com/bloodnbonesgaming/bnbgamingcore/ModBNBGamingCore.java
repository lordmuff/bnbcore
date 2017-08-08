package com.bloodnbonesgaming.bnbgamingcore;

import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;

import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;

public class ModBNBGamingCore extends DummyModContainer {
	
	public ModBNBGamingCore() {
		super(new ModMetadata());
		final ModMetadata meta = this.getMetadata();
		meta.modId = ModInfo.MODID;
		meta.name = ModInfo.MOD_NAME;
		meta.version = ModInfo.VERSION;
		meta.authorList = Lists.newArrayList("blargerist", "superckl");
		meta.url = "http://bloodnbonesgaming.com/";
		meta.parent = "bnbgaminglib";
	}
	
	@Override
	public boolean registerBus(final EventBus bus, final LoadController controller) {
		bus.register(this);
		return true;
	}
	
	@Override
	public Object getMod() {
		return this;
	}
}