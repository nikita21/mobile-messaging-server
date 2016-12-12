package com.app.mobile.messaging.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
    private String serverAddress;

    public MessagingClient(int port, String originId) {
	this.port = port;
	this.originId = originId;
	
	Properties properties = PropertyReaderUtils.getInstance().getProperties();
	serverAddress = properties.getProperty("messaging.server.connection.host", "127.0.0.1");
    }

    public void run()
    {
	Socket socket = null;
	try
	{
	    socket = new Socket(serverAddress, port);
	    System.out.println("Client " + originId + " is connected to server.");
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
	//take input from standard input
	BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
	
	//write input taken from stdIn to the output stream of the socket
	ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
	
	//receive response message sent by other clients to the server
	ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
	String responseMessage;
	String[] inputMessage;
	while(true)
	{
	    inputMessage = stdIn.readLine().split(",", 2);
	    System.out.println(inputMessage);
	    if(inputMessage != null)
	    {
		String destinationId = inputMessage[0];
		String message = inputMessage[1];
		ClientRequestObject requestObject = new ClientRequestObject(originId, destinationId, message);
		oos.writeObject(requestObject);
		oos.flush();
	    }
	    responseMessage = (String) ois.readObject();
	    if(responseMessage != null)
	    {
		System.out.println(responseMessage);
	    }
	}
	//oos.close();
    }

}
