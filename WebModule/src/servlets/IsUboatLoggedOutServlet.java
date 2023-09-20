package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servlets.agent.utils.Constants;
import servlets.agent.utils.ServletUtils;
import servlets.agent.utils.UserManager;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "DidUBoatLoggedOut", urlPatterns = "/did-uboat-logged-out")
public class IsUboatLoggedOutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        String username = req.getParameter("username");
        String client = req.getParameter("client");

        PrintWriter out = resp.getWriter();
        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        String answer = "false";
        boolean didUBoatLoggedOut = userManager.didUBoatLoggedOut(username, client);

        if(didUBoatLoggedOut) {
            if (client.equals("Ally")) {
                userManager.getAlly(username).initUBoatName();
            } else { //this is Agent
                userManager.getAgent(username).initUBoatName();
            }
            answer = "true";
        }

        String json = Constants.GSON_INSTANCE.toJson(answer, String.class);
        out.println(json);
        out.flush();

    }
}
