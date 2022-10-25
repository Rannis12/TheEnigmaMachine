package servlets.agent;

import entities.UBoat;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.UserManager;

import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name = "getFileContentServlet", urlPatterns = "/get-fileContent")
public class GetFileContentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String agentName = req.getParameter("agentName");

        PrintWriter out = resp.getWriter();

        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        UBoat uBoat = userManager.getUBoatByGivenAgentName(agentName);

        String json = userManager.getInputStreamFromMap(uBoat.getBattleName());

        out.println(json);
        resp.setStatus(HttpServletResponse.SC_OK);
        out.flush();

    }
}
