package com.app.mobile.messaging.server.entity;

import java.net.Socket;
import java.util.List;

public class ClientResponseObject
{
    private Socket socket;
    private List<String> messages;
    
    public ClientResponseObject(Socket socket, List<String> messages)
    {
	this.socket = socket;
	this.messages = messages;
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
	return "ClientResponseObject [socket=" + socket + ", messages=" + messages + "]";
    }

}
