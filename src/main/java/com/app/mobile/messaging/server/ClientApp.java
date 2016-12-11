package com.app.mobile.messaging.server;

import java.util.Properties;
import java.util.concurrent.ThreadPoolExecutor;

import com.app.mobile.messaging.server.utils.ExecutorUtils;
import com.app.mobile.messaging.server.utils.PropertyReaderUtils;

public class ClientApp
{
    private static ThreadPoolExecutor executor;
    private static int port;

    public static void main(String[] args)
    {
	Properties properties = PropertyReaderUtils.getInstance().getProperties();
	port = Integer.parseInt(properties.getProperty("messaging.server.connection.port", "7777"));
	int numberOfClients = Integer.parseInt(properties.getProperty("messaging.server.client.threads", "1"));
	executor = ExecutorUtils.getNewThreadPoolExecutor(numberOfClients);

	String originId = args[0];
	String destId = args[1];
	String message = args[2];
	MessagingClient client = new MessagingClient(port, originId, destId, message);
	executor.submit(client);
    }

}
