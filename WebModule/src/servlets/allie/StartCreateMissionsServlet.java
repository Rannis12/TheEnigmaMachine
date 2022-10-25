package servlets.allie;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.UserManager;

import java.io.IOException;

@WebServlet(name = "StartCreateMissionsServlet", urlPatterns = "/create-missions")
public class StartCreateMissionsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String allyUsername = req.getParameter("username");
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        userManager.startCreateMissions(allyUsername);

    }
}
