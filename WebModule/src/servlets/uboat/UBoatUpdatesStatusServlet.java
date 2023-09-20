package servlets.uboat;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servlets.agent.utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "UBoatUpdatesStatusServlet", urlPatterns = "/uboat-updates-status")
public class UBoatUpdatesStatusServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/plain");
        String username = req.getParameter("username");
        String status = req.getParameter("status");

        ServletUtils.getUserManager(getServletContext()).setUBoatStatus(username, status);


    }
}
