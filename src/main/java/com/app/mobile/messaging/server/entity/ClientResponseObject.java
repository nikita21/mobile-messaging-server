package com.app.mobile.messaging.server.entity;

import java.net.Socket;
import java.util.List;

public class ClientResponseObject
{
    private String destinationId;
    private Socket socket;
    private List<String> messages;
    
    public ClientResponseObject(String destinationId, Socket socket, List<String> messages)
    {
	this.destinationId = destinationId;
	this.socket = socket;
	this.messages = messages;
    }

    public String getDestinationId()
    {
        return destinationId;
    }

    public void setDestinationId(String destinationId)
    {
        this.destinationId = destinationId;
    }

    public Socket getSocket()
    {
        return socket;
    }

    public void setSocket(Socket socket)
    {
        this.socket = socket;
    }

    public List<String> getMessages()
    {
        return messages;
    }

    public void setMessages(List<String> messages)
    {
        this.messages = messages;
    }

    @Override
    public String toString()
    {
	return "ClientResponseObject [destinationId=" + destinationId + ", socket=" + socket + ", messages=" + messages + "]";
    }

}
