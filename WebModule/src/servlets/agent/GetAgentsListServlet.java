package servlets.agent;

import dtos.entities.AgentDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servlets.agent.utils.ServletUtils;
import servlets.agent.utils.UserManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import static servlets.agent.utils.Constants.GSON_INSTANCE;

@WebServlet(name = "AgentListServlet", urlPatterns = "/agent-list")
public class GetAgentsListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try(PrintWriter out = resp.getWriter()) {
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            Set<AgentDTO> agentDTOS = userManager.getAgents();
            String json = GSON_INSTANCE.toJson(agentDTOS);
            resp.getWriter().println(json);
            out.flush();
        }
    }
}




