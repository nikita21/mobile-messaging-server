package com.app.mobile.messaging.server;

import java.util.Properties;
import com.app.mobile.messaging.server.utils.PropertyReaderUtils;

public class Main
{
    private static int port;
    private static int numberOfServerThreads;
    
    public static void main(String[] args)
    {
	Properties properties = PropertyReaderUtils.getInstance().getProperties();
	port = Integer.parseInt(properties.getProperty("messaging.server.connection.port", "7777"));
	numberOfServerThreads = Integer.parseInt(properties.getProperty("messaging.server.threadpool", "5"));
	
	MessagingServer messagingServer = new MessagingServer(port, numberOfServerThreads);
	messagingServer.start();
	
    }

}
