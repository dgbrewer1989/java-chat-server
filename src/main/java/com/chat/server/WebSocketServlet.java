package com.chat.server;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "WebSocketServlet", displayName = "Websocket connect servlet", urlPatterns = {"/connect/*"}, loadOnStartup = 1)
public class WebSocketServlet extends HttpServlet
{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        String username = req.getParameter("username");
        HttpSession httpSession = req.getSession();
        httpSession.setAttribute("username", username);

        try
        {
            //TODO: setup cors
            resp.setHeader("Access-Control-Allow-Origin", "*");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
