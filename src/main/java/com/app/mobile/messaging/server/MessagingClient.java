package com.app.mobile.messaging.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;

import com.app.mobile.messaging.server.entity.ClientRequestObject;
import com.app.mobile.messaging.server.utils.PropertyReaderUtils;

public class MessagingClient implements Runnable
{
    private int port;
    private String originId;
    private String destinationId;
    private String message;
    private String serverAddress;

    public MessagingClient(int port, String originId, String destinationId, String message) {
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
    }
    
    private void connectToServer(Socket socket) throws UnknownHostException, IOException, ClassNotFoundException
    {
	ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
	ClientRequestObject requestObject = new ClientRequestObject(originId, destinationId, message);
	oos.writeObject(requestObject);
	
	//receive response message sent by the server
	ObjectInputStream ois = null;
	try
	{
	    ois = new ObjectInputStream(socket.getInputStream());
	    String responseMessage = (String) ois.readObject();
	    System.out.println(responseMessage);
	}
	catch(EOFException e)
	{
	    System.out.println("No message to be read!!!!!!!!!!!1");
	}
	finally
	{
	    if(ois != null)
	    {
		ois.close();
	    }
	    oos.close();
	}
    }

}
