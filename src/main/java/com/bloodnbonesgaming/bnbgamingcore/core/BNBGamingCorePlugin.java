package com.bloodnbonesgaming.bnbgamingcore.core;

import java.io.File;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bloodnbonesgaming.bnbgamingcore.ModBNBGamingCore;
import com.bloodnbonesgaming.bnbgamingcore.ModInfo;
import com.bloodnbonesgaming.bnbgamingcore.core.module.IClassTransformerModule;
import com.bloodnbonesgaming.bnbgamingcore.core.util.BNBGamingClassTransformer;
import com.bloodnbonesgaming.bnbgamingcore.core.util.BNBGamingCoreConfig;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ModuleDisableHandler;
import com.bloodnbonesgaming.bnbgamingcore.util.ReflectionHelper;

import lombok.Getter;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.Name;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.SortingIndex;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;
import squeek.asmhelper.com.bloodnbonesgaming.bnbgamingcore.ObfHelper;

@Name("BNBGamingCore")
@SortingIndex(1001)
@TransformerExclusions({"com.bloodnbonesgaming.bnbgamingcore.core", "squeek.asmhelper.com.bloodnbonesgaming.bnbgamingcore"})
public class BNBGamingCorePlugin implements IFMLLoadingPlugin{

	public static final Logger log = LogManager.getLogger(ModInfo.MOD_NAME);
	private static BNBGamingCorePlugin INSTANCE;
	private static String[] acceptableMinecraftVersions = new String[]{"1.12,"};
	/**
	 * The EventBus on which CoreModEvents are distributed. You should register your listeners when your IFMLLoadingPlugin is instantiated.
	 */
	@Getter
	private File mcLocation;

	public BNBGamingCorePlugin() {
		if(BNBGamingCorePlugin.INSTANCE != null) {
			throw new IllegalStateException("BNBGamingCore has already been instantiated! Use getInstance()");
		}
		BNBGamingCorePlugin.INSTANCE = this;
	}

	@Override
	public String[] getASMTransformerClass() {
		String versions = "";
		for (int i = 0; i < BNBGamingCorePlugin.acceptableMinecraftVersions.length; i++)
		{
			versions = versions.concat(BNBGamingCorePlugin.acceptableMinecraftVersions[i] + " ");
		}
		
		if (BNBGamingCorePlugin.isMinecraftVersionAcceptable())
		{			
			BNBGamingCorePlugin.log.info("Minecraft version is " + ForgeVersion.mcVersion + " and BNBGamingCore accepts versions " + versions + ". It will be registered.");
			return new String[] {BNBGamingCoreClassTransformer.class.getName()};
		}
		else
		{
			BNBGamingCorePlugin.log.error("Minecraft version is " + ForgeVersion.mcVersion + " and BNBGamingCore accepts versions " + versions + ". It will not be registered.");
			return null;
		}
	}

	@Override
	public String getModContainerClass() {
		if (BNBGamingCorePlugin.isMinecraftVersionAcceptable())
		{
			return ModBNBGamingCore.class.getName();
		}
		else
		{
			return null;
		}
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(final Map<String, Object> data) {
		if (BNBGamingCorePlugin.isMinecraftVersionAcceptable())
		{
			ObfHelper.setObfuscated((Boolean) data.get("runtimeDeobfuscationEnabled"));
			this.mcLocation = (File) data.get("mcLocation");
			this.init();
		}
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

	public void init(){
		BNBGamingCoreConfig.init(new File(this.mcLocation, "/config/BNBGamingCore.cfg"));
		for(final IClassTransformerModule module:BNBGamingClassTransformer.getTransformerModules()) {
			if(module.canBeDisabled() && BNBGamingCoreConfig.disabledModules.contains(module.getModuleName())){
				BNBGamingClassTransformer.disableTransformerModule(module.getModuleName());
				BNBGamingCorePlugin.log.debug("Disabled ASM module "+module.getModuleName());
			}
		}
		ModuleDisableHandler.init(this.mcLocation);
	}

	public static BNBGamingCorePlugin getInstance(){
		if(BNBGamingCorePlugin.INSTANCE == null){
			final StackTraceElement st = ReflectionHelper.retrieveCallingStackTraceElement();
			throw new IllegalStateException("BNBGamingCore has not been instantiated yet! The method "+st.getClassName()+"."+st.getMethodName()+" needs to fix its load order!");
		}
		return BNBGamingCorePlugin.INSTANCE;
	}
	
	public static boolean isMinecraftVersionAcceptable()
	{
		for (int i = 0; i < BNBGamingCorePlugin.acceptableMinecraftVersions.length; i++)
		{
			if (ForgeVersion.mcVersion.equals(BNBGamingCorePlugin.acceptableMinecraftVersions[i])
					|| (BNBGamingCorePlugin.acceptableMinecraftVersions[i].endsWith(",")
							&& ForgeVersion.mcVersion.startsWith(BNBGamingCorePlugin.acceptableMinecraftVersions[i].substring(0, BNBGamingCorePlugin.acceptableMinecraftVersions[i].length() - 1))))
			{
				return true;
			}
		}
		return false;
	}

}
