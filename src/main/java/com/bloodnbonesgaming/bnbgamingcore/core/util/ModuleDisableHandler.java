package com.bloodnbonesgaming.bnbgamingcore.core.util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class ModuleDisableHandler {
	
	private static final Attributes.Name requiresEventAttribute = new Attributes.Name("RequiresBNBCoreEvent");
	
	public static void init(final File mcLocation)
	{
		final List<String> modulesToEnable = new ArrayList<String>();
		final File modDir = new File(mcLocation, "mods");
		
		FilenameFilter jarFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name)
            {
                return name.endsWith(".jar");
            }
        };
        
        final File[] modList = modDir.listFiles(jarFilter);
        
        for (final File mod : modList)
        {
        	try (final JarFile modJar = new JarFile(mod))
        	{
        		if (modJar != null)
        		{
        			final Manifest manifest = modJar.getManifest();
        			
        			if (manifest != null)
        			{
        				final Attributes attributes = manifest.getMainAttributes();
        				
        				if (attributes.containsKey(ModuleDisableHandler.requiresEventAttribute))
        				{
        					final String string = attributes.getValue(ModuleDisableHandler.requiresEventAttribute);
        					
        					final String[] events = string.split(",");
        					
        					for (int i = 0; i < events.length; i++)
        					{
        						final List<String> modules = BNBCoreEvents.getModulesToEnable(events[i]);
        						
        						if (modules != null)
        						{
        							modulesToEnable.addAll(modules);
        						}
        					}
        				}
        			}
        		}
        	} catch (IOException e) {
				e.printStackTrace();
			}
        }
        
        BNBGamingClassTransformer.disableAllTransformerModulesExcept(modulesToEnable);
	}
}