package com.app.mobile.messaging.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import com.app.mobile.messaging.server.entity.ClientRequestObject;
import com.app.mobile.messaging.server.entity.ClientResponseObject;
import com.app.mobile.messaging.server.utils.ExecutorUtils;

public class MessagingServer
{
    private static Map<String, ClientResponseObject> identificationMap = new HashMap<String, ClientResponseObject>();
    private ThreadPoolExecutor executor;
    private ServerSocket server;

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
	int i = 0;
	while (true)
	{
	    try
	    {
		Socket socket = server.accept();
		executor.submit(new ConnectionHandler(socket));
		i++;
		if (i == 20) break;
	    }
	    catch (IOException e)
	    {
		e.printStackTrace();
	    }
	}
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
		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
		ClientRequestObject requestObj = (ClientRequestObject) ois.readObject();
		String originId = requestObj.getOriginId();
		String destinationId = requestObj.getDestinationId();
		String requestMessage = requestObj.getMessage();
		if (requestMessage.equalsIgnoreCase("EXIT"))
		{
		    socket.close();
		}
		else
		{
		    String message = originId + " : " + requestMessage;
		    ClientResponseObject responseObj = identificationMap.get(originId);
		    if (null == responseObj)
		    {
			identificationMap.put(originId, new ClientResponseObject(originId, socket, null));
		    }
		    else
		    {
			responseObj.setSocket(socket);
		    }
		    ClientResponseObject destResponseObj = identificationMap.get(destinationId);
		    if (destResponseObj != null && destResponseObj.getSocket() != null)
		    {
			List<String> messages = destResponseObj.getMessages();
			ObjectOutputStream destOos = new ObjectOutputStream(destResponseObj.getSocket().getOutputStream());
			if (messages != null)
			{
			    for (String msg : messages)
			    {
				destOos.writeObject(msg);
			    }
			}
			destOos.writeObject(message);
			destOos.close();
		    }
		    else
		    {
			if(null == destResponseObj)
			{
			    destResponseObj = new ClientResponseObject(destinationId, null, null);
			}
			List<String> messages = destResponseObj.getMessages();
			if (null == messages)
			{
			    messages = new ArrayList<String>();
			}
			messages.add(message);
			destResponseObj.setMessages(messages);
			identificationMap.put(destinationId, destResponseObj);
		    }
		}
		ois.close();
	    }
	    catch (IOException e)
	    {
		e.printStackTrace();
	    }
	    catch (ClassNotFoundException e)
	    {
		e.printStackTrace();
	    }
	}

    }
}
