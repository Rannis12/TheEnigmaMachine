package servlets.agent;

import dtos.entities.AgentDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.Constants;
import utils.ServletUtils;
import utils.UserManager;

import java.io.IOException;
import java.io.Reader;

@WebServlet(name = "AgentPostDetailsServlet", urlPatterns = "/agent-post-details")
public class AgentPostDetailsServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String agentName = req.getParameter("agentName");
        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        String jsonManualData = "";
        Reader reader = req.getReader();
        int intValueOfChar;
        while ((intValueOfChar = reader.read()) != -1) {
            jsonManualData += (char) intValueOfChar;
        }
        reader.close();

        AgentDTO agentDTO = Constants.GSON_INSTANCE.fromJson(jsonManualData, AgentDTO.class);
        userManager.postAgentDetails(agentName, agentDTO.getAllie(), agentDTO);

    }
}
