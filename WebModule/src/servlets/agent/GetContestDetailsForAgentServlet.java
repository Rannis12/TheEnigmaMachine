package servlets.agent;

import dtos.web.ContestDetailsForAgentDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.Constants;
import utils.ServletUtils;
import utils.UserManager;

import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name = "ContestDetailsForAgent", urlPatterns = "/details-for-agent")
public class GetContestDetailsForAgentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        String agentName = req.getParameter("username");
        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        ContestDetailsForAgentDTO dto = userManager.getContestDetailsForAgent(agentName);
        String json = Constants.GSON_INSTANCE.toJson(dto, ContestDetailsForAgentDTO.class);

        out.println(json);
        out.flush();

    }
}
