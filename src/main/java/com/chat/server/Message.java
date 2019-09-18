package com.chat.server;

public class Message
{
    private String username;
    private String userId;
    private String timestamp;
    private String message;
    private boolean isDirect;

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(String timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public boolean isDirect()
    {
        return isDirect;
    }

    public void setDirect(boolean direct)
    {
        isDirect = direct;
    }
}
