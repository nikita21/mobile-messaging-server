package com.app.mobile.messaging.server.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReaderUtils
{
    private static PropertyReaderUtils _instance = new PropertyReaderUtils();
    private Properties properties = new Properties();
    
    private PropertyReaderUtils()
    {
	properties = new Properties();
    }
    
    public static PropertyReaderUtils getInstance()
    {
	return _instance;
    }

    public Properties getProperties()
    {
	if (properties.isEmpty()){
	    properties = readProperties();
	}
	return properties;
    }

    private Properties readProperties()
    {

	InputStream inputStream = null;
	try
	{
	    String propFileName = "config.properties";

	    inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
	    if (inputStream != null)
		properties.load(inputStream);
	    else
		throw new FileNotFoundException("Unable to find properties file: " + propFileName);
	}
	catch (FileNotFoundException e)
	{
	    e.printStackTrace();
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    if (null != inputStream) try
	    {
		inputStream.close();
	    }
	    catch (IOException e)
	    {
		e.printStackTrace();
	    }
	}
	return properties;
    }

}
