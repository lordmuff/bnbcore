package com.bloodnbonesgaming.bnbgamingcore.core.util;

import java.io.File;

import com.bloodnbonesgaming.bnbgamingcore.util.ReflectionHelper;

import lombok.RequiredArgsConstructor;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.Name;

public abstract class BNBCoreModEvent{

	@RequiredArgsConstructor
	public static class InitEvent extends BNBCoreModEvent{

		public final File mcLocation;

		public File getSuggestedConfigLocation(final String name){
			return new File(this.mcLocation, "/config/"+name+".cfg");
		}

		/**
		 * Will attempt to retrieve the name of the calling class annotated with {@link cpw.mods.fml.relauncher.IFMLLoadingPlugin.Name}
		 * @return
		 */
		public File getSuggestedConfigLocation(){
			try{
				final Class<?> clazz = Class.forName(ReflectionHelper.retrieveCallingStackTraceElement().getClassName());
				final Name name = clazz.getAnnotation(Name.class);
				return this.getSuggestedConfigLocation(name.value());
			}catch(final Throwable e){
				throw new RuntimeException("Failed to retrieve name! This method should be called by this class!", e);
			}

		}

	}

}
