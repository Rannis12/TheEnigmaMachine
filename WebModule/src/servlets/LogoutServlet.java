package servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servlets.agent.utils.ServletUtils;
import servlets.agent.utils.UserManager;

import java.io.IOException;

@WebServlet(name = "LogoutServlet", urlPatterns = "/logout")
public class LogoutServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String uBoatName = request.getParameter("username");

        //only uBoat can log out

        userManager.removeUBoatAndDetachEntities(uBoatName);

        userManager.removeUser(uBoatName);
        /*SessionUtils.clearSession(request);*/

    }

}