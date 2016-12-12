package com.app.mobile.messaging.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;

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
	PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	
	//receive response message sent by other clients to the server
	BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	String responseMessage;
	String inputMessage;
	while(true)
	{
	    System.out.println("Enter message to be send in the format {destinationId,message} : ");
	    inputMessage = stdIn.readLine();
	    if(inputMessage != null)
	    {
		String requestObject = originId+","+inputMessage;
		out.println(requestObject);
		out.flush();
	    }
	    responseMessage = in.readLine();
	    if(responseMessage != null)
	    {
		System.out.println(responseMessage);
	    }
	}
    }

}
