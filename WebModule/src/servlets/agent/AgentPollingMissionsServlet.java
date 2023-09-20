package servlets.agent;

import dtos.web.DecryptTaskDTO;
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
import java.util.List;


@WebServlet(name = "AgentPollingMissionsServlet", urlPatterns = "/agent-polling-missions")
public class AgentPollingMissionsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        String agentName = req.getParameter("username");
        int amountOfMissions = Integer.parseInt(req.getParameter("amountOfMissions"));

        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        List<DecryptTaskDTO> dtoList = userManager.pollMissions(agentName, amountOfMissions);

        String json = Constants.GSON_INSTANCE.toJson(dtoList);
        out.println(json);
        out.flush();

    }
}
