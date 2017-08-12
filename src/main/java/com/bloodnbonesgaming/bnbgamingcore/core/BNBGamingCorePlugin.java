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
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.Name;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.SortingIndex;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;
import squeek.asmhelper.com.bloodnbonesgaming.bnbgamingcore.ObfHelper;

@Name("BNBGamingCore")
@MCVersion("1.12")
@SortingIndex(1001)
@TransformerExclusions({"com.bloodnbonesgaming.bnbgamingcore.core", "squeek.asmhelper.com.bloodnbonesgaming.bnbgamingcore"})
public class BNBGamingCorePlugin implements IFMLLoadingPlugin{

	public static final Logger log = LogManager.getLogger(ModInfo.MOD_NAME);
	private static BNBGamingCorePlugin INSTANCE;
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
		return new String[] {BNBGamingCoreClassTransformer.class.getName()};
	}

	@Override
	public String getModContainerClass() {
		return ModBNBGamingCore.class.getName();
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(final Map<String, Object> data) {
		ObfHelper.setObfuscated((Boolean) data.get("runtimeDeobfuscationEnabled"));
		this.mcLocation = (File) data.get("mcLocation");
		this.init();
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

}
