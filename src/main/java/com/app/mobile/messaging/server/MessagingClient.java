package com.app.mobile.messaging.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;

import com.app.mobile.messaging.server.utils.PropertyReaderUtils;

public class MessagingClient implements Runnable
{
    private int port;
    private int originId;
    private int destinationId;
    private String message;
    private String serverAddress;

    public MessagingClient(int port, int originId, int destinationId, String message) {
	this.port = port;
	this.originId = originId;
	this.destinationId = destinationId;
	this.message = message;
	
	Properties properties = PropertyReaderUtils.getInstance().getProperties();
	serverAddress = properties.getProperty("messaging.server.connection.host", "127.0.0.1");
    }

    public void run()
    {
	Socket socket = null;
	try
	{
	    socket = new Socket(serverAddress, port);
	    connectToServer(socket);
	}
	catch (UnknownHostException e)
	{
	    e.printStackTrace();
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
	catch (ClassNotFoundException e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    try
	    {
		socket.close();
	    }
	    catch (IOException e)
	    {
		e.printStackTrace();
	    }
	}
    }
    
    private void connectToServer(Socket socket) throws UnknownHostException, IOException, ClassNotFoundException
    {
	ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
	oos.writeObject(message + originId);
	
	//receive response message sent by the server
	ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
	String responseMessage = (String) ois.readObject();
	System.out.println("Reponse: " + responseMessage);
	
	ois.close();
	oos.close();
	
    }

}
