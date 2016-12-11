package com.app.mobile.messaging.server.entity;

import java.io.Serializable;

public class ClientRequestObject implements Serializable
{
    private static final long serialVersionUID = -1155248333546760079L;
    private String originId;
    private String destinationId;
    private String message;
    
    public ClientRequestObject(String originId, String destinationId, String message)
    {
	this.originId = originId;
	this.destinationId = destinationId;
	this.message = message;
    }

    public String getOriginId()
    {
        return originId;
    }

    public void setOriginId(String originId)
    {
        this.originId = originId;
    }

    public String getDestinationId()
    {
        return destinationId;
    }

    public void setDestinationId(String destinationId)
    {
        this.destinationId = destinationId;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    @Override
    public String toString()
    {
	return "ClientRequestObject [originId=" + originId + ", destinationId=" + destinationId + ", message=" + message + "]";
    }
    
}
