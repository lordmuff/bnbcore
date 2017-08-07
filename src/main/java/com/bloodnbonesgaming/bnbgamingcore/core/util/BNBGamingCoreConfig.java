package com.bloodnbonesgaming.bnbgamingcore.core.util;

import java.io.File;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraftforge.common.config.Configuration;

public class BNBGamingCoreConfig {

	private static Configuration config;
	public static List<String> disabledModules;

	public static void init(final File location){
		BNBGamingCoreConfig.config = new Configuration(location);
		BNBGamingCoreConfig.config.load();

		BNBGamingCoreConfig.disabledModules = Lists.newArrayList(BNBGamingCoreConfig.config.getStringList("disabled modules", "modules", new String[0], "Specify ASM modules here that you do not want to run."));


		BNBGamingCoreConfig.config.save();
	}

}
