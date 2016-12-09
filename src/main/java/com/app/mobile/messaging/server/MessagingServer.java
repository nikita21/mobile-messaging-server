package com.app.mobile.messaging.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ThreadPoolExecutor;

import com.app.mobile.messaging.server.utils.ExecutorUtils;

public class MessagingServer
{
    private ThreadPoolExecutor executor;
    private ServerSocket server;
    
    public MessagingServer(int port, int numOfThreads)
    {
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
	while(true)
	{
	    try
	    {
		Socket socket = server.accept();
		executor.submit(new ConnectionHandler(socket));
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
	
	public ConnectionHandler(Socket socket)
	{
	    this.socket = socket;
	}
	
	public void run()
	{
	    try
	    {
		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
		String message = (String) ois.readObject();
		System.out.println("Message received: " + message);
		//send a response to the client
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		oos.writeObject("Message Sent.");
		
		ois.close();
		oos.close();
		socket.close();
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
