package com.app.mobile.messaging.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.app.mobile.messaging.server.entity.ClientResponseObject;
import com.app.mobile.messaging.server.utils.ExecutorUtils;

public class MessagingServer
{
    private static Map<String, ClientResponseObject> identificationMap = new HashMap<String, ClientResponseObject>();
    private ThreadPoolExecutor executor;
    private ServerSocket server;
    private volatile boolean isServerRunning = true;

    public MessagingServer(int port, int numOfThreads) {
	try
	{
	    server = new ServerSocket(port);
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
	executor = ExecutorUtils.getNewThreadPoolExecutor(numOfThreads);
    }

    public void start()
    {
	while (true)
	{
	    try
	    {
		Socket socket = server.accept();
		executor.submit(new ConnectionHandler(socket));
		if (!isServerRunning) break;
	    }
	    catch (IOException e)
	    {
		e.printStackTrace();
	    }
	}
	// shutdown executor
	ExecutorUtils.shutdownAndWait(executor, 5, TimeUnit.SECONDS);
    }

    private final class ConnectionHandler implements Runnable
    {
	private Socket socket;

	public ConnectionHandler(Socket socket) {
	    this.socket = socket;
	}

	public void run()
	{
	    try
	    {
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		while (true)
		{
		    String[] requestObj = in.readLine().split(",");
		    String originId = requestObj[0];
		    String destinationId = requestObj[1];
		    String requestMessage = requestObj[2];
		    if (requestMessage.equalsIgnoreCase("EXIT"))
		    {
			socket.close();
			isServerRunning = false;
		    }
		    else
		    {
			String message = originId + " : " + requestMessage;
			ClientResponseObject responseObj = identificationMap.get(originId);
			if (null == responseObj)
			{
			    identificationMap.put(originId, new ClientResponseObject(socket, null));
			}
			else
			{
			    responseObj.setSocket(socket);
			    List<String> originMessages = responseObj.getMessages();
			    if (originMessages != null && !originMessages.isEmpty())
			    {
				writeToOutputStream(socket, originMessages);
			    }
			}

			/* fetch destination device object from the map and save the message to the list */
			ClientResponseObject destResponseObj = identificationMap.get(destinationId);
			if (null == destResponseObj)
			{
			    List<String> messages = new ArrayList<String>();
			    messages.add(message);
			    destResponseObj = new ClientResponseObject(null, messages);
			    identificationMap.put(destinationId, destResponseObj);
			    for (Map.Entry<String, ClientResponseObject> entry : identificationMap.entrySet())
			    {
				System.out.println(entry.getKey() + " " + entry.getValue());
			    }
			}
			else
			{
			    Socket destSocket = destResponseObj.getSocket();
			    List<String> destMessages = destResponseObj.getMessages();
			    if (destSocket != null)
			    {
				if (null == destMessages)
				{
				    destMessages = new ArrayList<String>();
				}
				destMessages.add(message);
				writeToOutputStream(destSocket, destMessages);
			    }
			    else
			    {
				destMessages.add(message);
			    }
			}
		    }
		}
	    }
	    catch (IOException e)
	    {
		e.printStackTrace();
	    }
	}

    }

    private void writeToOutputStream(Socket socket, List<String> listOfMessages) throws IOException
    {
	PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	Iterator<String> iterator = listOfMessages.iterator();
	while (iterator.hasNext())
	{
	    String str = iterator.next();
	    out.println(str);
	    out.flush();
	    iterator.remove();
	}
    }
}
