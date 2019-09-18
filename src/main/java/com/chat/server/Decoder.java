package com.chat.server;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.DecodeException;
import javax.websocket.EndpointConfig;
import java.io.StringReader;

public class Decoder implements javax.websocket.Decoder.Text<Message>
{
    @Override
    public void init(EndpointConfig config)
    {
    }

    @Override
    public void destroy()
    {
    }

    @Override
    public Message decode(String s) throws DecodeException
    {
        final JsonReader reader = Json.createReader(new StringReader(s));

        JsonObject jsonObject = reader.readObject();

        Message message = new Message();
        if (jsonObject != null)
        {
            message.setMessage(jsonObject.getString("message"));
            message.setTimestamp(jsonObject.getString("timestamp"));
            message.setUserId(jsonObject.getString("userId"));
            message.setUsername(jsonObject.getString("username"));
        }

        return message;
    }

    @Override
    public boolean willDecode(String s)
    {
        //TODO: implement actual structure checks here to avoid handling later on
        return (s != null);
    }
}
