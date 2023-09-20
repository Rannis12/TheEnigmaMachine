package servlets.allie;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servlets.agent.utils.ServletUtils;
import servlets.agent.utils.UserManager;

import java.io.IOException;

@WebServlet(name = "AllyConfirmLogoutServlet", urlPatterns = "/confirmed-logout")
public class AllyConfirmLogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String allyName = req.getParameter("allyName");
        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        //reset Ally after game is over.
        userManager.getAlly(allyName).setConfirmLogout(true);
        userManager.getAlly(allyName).setStatus(false);
        userManager.clearAgentsPossibleCandidates(allyName);
        userManager.getAlly(allyName).setMissionSize(0);
    }
}
