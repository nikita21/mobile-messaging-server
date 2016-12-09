package com.app.mobile.messaging.server;

import java.util.Properties;
import java.util.concurrent.ThreadPoolExecutor;

import com.app.mobile.messaging.server.utils.ExecutorUtils;
import com.app.mobile.messaging.server.utils.PropertyReaderUtils;

public class Main
{
    private static int port;
    private static int numberOfServerThreads;
    private static ThreadPoolExecutor executor;
    
    public static void main(String[] args)
    {
	Properties properties = PropertyReaderUtils.getInstance().getProperties();
	port = Integer.parseInt(properties.getProperty("messaging.server.connection.port", "7777"));
	numberOfServerThreads = Integer.parseInt(properties.getProperty("messaging.server.threadpool", "5"));
	
	MessagingServer messagingServer = new MessagingServer(port, numberOfServerThreads);
	messagingServer.start();
	
	executor = ExecutorUtils.getNewThreadPoolExecutor(5);
	
	for(int i = 0; i < 5; i++)
	{
	    MessagingClient client = new MessagingClient(port, i, i+1, "blah blah");
	    executor.submit(client);
	}
    }

}
