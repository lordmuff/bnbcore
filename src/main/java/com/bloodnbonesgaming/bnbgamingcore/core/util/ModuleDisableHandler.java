package com.bloodnbonesgaming.bnbgamingcore.core.util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import com.bloodnbonesgaming.bnbgamingcore.core.BNBGamingCorePlugin;
import com.google.common.collect.ObjectArrays;

import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.LoaderException;
import net.minecraftforge.fml.relauncher.FileListHelper;
import net.minecraftforge.fml.relauncher.ModListHelper;

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
        
        File[] modList = modDir.listFiles(jarFilter);
        
        File[] parentSources = ModuleDisableHandler.getParentSources();
        final List<File> parentSourcesList = new ArrayList<File>();
        
        for (int i = 0; i < parentSources.length; i++)
        {
        	if (parentSources[i].getName().endsWith(".jar"))
        	{
        		parentSourcesList.add(parentSources[i]);
        	}
        }
        modList = ObjectArrays.concat(modList, parentSourcesList.toArray(new File[]{}), File.class);
        
        File versionedModDir = new File(modDir, ForgeVersion.mcVersion);
        if (versionedModDir.isDirectory())
        {
            File[] versionedCoreMods = versionedModDir.listFiles(jarFilter);
            modList = ObjectArrays.concat(modList, versionedCoreMods, File.class);
        }

        modList = ObjectArrays.concat(modList, ModListHelper.additionalMods.values().toArray(new File[0]), File.class);

        modList = FileListHelper.sortFileList(modList);

        BNBGamingCorePlugin.log.debug("Found " + modList.length + " mods.");
        
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
        					BNBGamingCorePlugin.log.debug("Found mod with correct manifest attribute. " + modJar.getName());
        					final String string = attributes.getValue(ModuleDisableHandler.requiresEventAttribute);
        					BNBGamingCorePlugin.log.debug("Attribute string: " + string);
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
	
	public static File[] getParentSources() {
        try
        {
        	final LaunchClassLoader loader = (LaunchClassLoader) ModuleDisableHandler.class.getClassLoader();
            List<File> files=new ArrayList<File>();
            for(URL url : loader.getSources())
            {
                URI uri = url.toURI();
                if(uri.getScheme().equals("file"))
                {
                    files.add(new File(uri));
                }
            }
            return files.toArray(new File[]{});
        }
        catch (URISyntaxException e)
        {
            FMLLog.log.error("Unable to process our input to locate the minecraft code", e);
            throw new LoaderException(e);
        }
    }
}