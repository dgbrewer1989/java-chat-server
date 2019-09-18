package com.chat.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SessionManagement
{
    private static Map<String, HttpSession> httpSessions = Collections.synchronizedMap(new HashMap<>());

    public static HttpSession getSession(String id)
    {
        return httpSessions.get(id);
    }

    public static HttpSession getSession(HttpServletRequest httpServletRequest)
    {
        HttpSession httpSession = httpServletRequest.getSession();

        httpSessions.put(httpSession.getId(), httpSession);

        return httpSession;
    }
}