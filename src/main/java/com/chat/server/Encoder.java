package com.chat.server;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class Encoder implements javax.websocket.Encoder.Text<Message>
{
    @Override
    public String encode(Message message) throws EncodeException
    {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder()
                .add("userId", message.getUserId())
                .add("timestamp", message.getTimestamp())
                .add("message", message.getMessage())
                .add("username", message.getUsername())
                .add("isDirect", message.isDirect());

        JsonObject jsonObject = objectBuilder.build();

        String response;
        try (Writer writer = new StringWriter())
        {
            //TODO: update this to be in a common place
            Json.createWriter(writer).write(jsonObject);
            response = writer.toString();
        } catch (IOException e)
        {
            throw new EncodeException(message, "Could not parse");
        }

        return response;
    }

    @Override
    public void init(EndpointConfig config)
    {
        System.out.println("Init");
    }

    @Override
    public void destroy()
    {
        System.out.println("Destroyed");
    }
}
