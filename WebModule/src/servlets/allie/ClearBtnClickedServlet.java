package servlets.allie;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servlets.agent.utils.ServletUtils;
import servlets.agent.utils.UserManager;

import java.io.IOException;

@WebServlet(name = "ClearBtnClickedServlet", urlPatterns = "/confirm-clear")
public class ClearBtnClickedServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String allyName = req.getParameter("allyName");
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        userManager.getAlly(allyName).setStatus(false);
        userManager.getAlly(allyName).setContestIsOver(true);

        userManager.clearAgentsPossibleCandidates(allyName);

    }

}
