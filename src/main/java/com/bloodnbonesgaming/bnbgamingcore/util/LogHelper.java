package com.bloodnbonesgaming.bnbgamingcore.util;

import org.apache.logging.log4j.Level;

import lombok.Getter;
import net.minecraftforge.fml.common.FMLLog;

public class LogHelper {

	@Getter
	private final String modID;

	public LogHelper(final String modID) {
		this.modID = modID;
	}

	public void log(final Level logLevel, final Object object)
	{
		FMLLog.log(this.modID, logLevel, String.valueOf(object));
	}

	public void all(final Object object)
	{
		this.log(Level.ALL, object);
	}

	public void debug(final Object object)
	{
		this.log(Level.DEBUG, object);
	}

	public void error(final Object object)
	{
		this.log(Level.ERROR, object);
	}

	public void fatal(final Object object)
	{
		this.log(Level.FATAL, object);
	}

	public void info(final Object object)
	{
		this.log(Level.INFO, object);
	}

	public void off(final Object object)
	{
		this.log(Level.OFF, object);
	}

	public void trace(final Object object)
	{
		this.log(Level.TRACE, object);
	}

	public void warn(final Object object)
	{
		this.log(Level.WARN, object);
	}
}