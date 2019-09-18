package com.chat.server;

import javax.servlet.http.HttpSession;
import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@ServerEndpoint(value = "/socket/{userId}",
        configurator = WebsocketConfigurator.class,
        decoders = {Decoder.class},
        encoders = {Encoder.class})
public class WebsocketEndpoint
{
    private static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());
    private ArrayList<String> users = new ArrayList<>();

    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig)
    {
        peers.add(session);

        System.out.println("Connection established. Session count: " + peers.size());

        HttpSession httpSession = (HttpSession) endpointConfig.getUserProperties().get(HttpSession.class.getName());

        String userId = getUserId(session);
        users.add(userId);

        Message message = new Message();
        message.setUserId(userId);
        message.setTimestamp(String.valueOf(new Date().toInstant().getEpochSecond()));
        message.setMessage(httpSession.getAttribute("username").toString() + " joined");
        message.setUsername("System");

        broadcast(session, message);
    }

    @OnMessage
    public void onMessage(Message message, Session session)
    {
        if (message.getMessage().startsWith("/direct"))
        {
            //TODO: This logic needs revisted.. and implemented in the client app.
            String[] splitMessage = message.getMessage().split(" ");

            String user = splitMessage[1];
            String directMessage = message
                    .getMessage()
                    .substring(message.getMessage().lastIndexOf(" "))
                    .trim();

            Message direct = new Message();
            direct.setMessage(directMessage);
            direct.setUserId(message.getUserId());
            direct.setDirect(true);
            direct.setTimestamp(message.getTimestamp());
            direct.setUsername(user);

            directMessage(user, direct);
        } else
        {
            broadcast(session, message);
        }
    }

    private void directMessage(String user, Message directMessage)
    {
        for (Session localSession : peers)
        {
            Map<String, Object> map = localSession.getUserProperties();
            HttpSession httpSession = (HttpSession) map.get(HttpSession.class.getName());

            if (httpSession.getAttribute("username").equals(user))
            {
                if (localSession.isOpen())
                {
                    try
                    {
                        localSession.getBasicRemote().sendObject(directMessage);
                    } catch (IOException | EncodeException e)
                    {
                        System.out.println("Error Occured during direct message");
                        e.printStackTrace();
                    }

                    break;
                } else
                {
                    peers.remove(localSession);
                    break;
                }
            }
        }
    }

    @OnClose
    public void onClose(Session session)
    {
        peers.remove(session);

        String userId = getUserId(session);
        users.remove(userId);

        Message message = new Message();
        message.setUserId(userId);
        message.setTimestamp(String.valueOf(new Date().toInstant().getEpochSecond()));
        message.setMessage("left");

        broadcast(session, message);
    }

    @OnError
    public void onError(Session session, Throwable throwable)
    {
        System.out.println("Error occurred: " + throwable.getMessage() + " | Type: " + throwable.getCause() + " | toString: " + throwable.toString());
    }

    private String getUserId(Session session)
    {
        Map<String, String> pathParameters = session.getPathParameters();
        return pathParameters.get("userId");
    }

    private void broadcast(Session session, Message message)
    {
        for (Session localSession : peers)
        {
            send(localSession, message);
        }
    }

    private void send(Session session, Message message)
    {
        try
        {
            if (session.isOpen())
            {
                session.getBasicRemote().sendObject(message);
            } else
            {
                peers.remove(session);
            }
        } catch (IOException | EncodeException e)
        {
            e.printStackTrace();
        }
    }
}
