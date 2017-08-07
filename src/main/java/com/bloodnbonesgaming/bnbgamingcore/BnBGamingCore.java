package com.bloodnbonesgaming.bnbgamingcore;

import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMAdditionHelper;
import com.bloodnbonesgaming.bnbgamingcore.util.LogHelper;

import lombok.Getter;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ModInfo.MODID, name = ModInfo.MOD_NAME, version = ModInfo.VERSION, dependencies = "required-after:bnbgaminglib@[2.6.0,)")//TODO has to be changed to 2.7.0
public class BnBGamingCore
{
	@Instance(ModInfo.MODID)
	public static BnBGamingCore instance;
	
	@Getter
	protected LogHelper log = new LogHelper(ModInfo.MODID);
	
	@EventHandler
	public void onPreInit(final FMLPreInitializationEvent e){
		this.log.info("Forcing distribution of ASMAdditionHelperWrappers...");
		new ASMAdditionHelper.ASMAdditionHelperWrapper("");
	}
}