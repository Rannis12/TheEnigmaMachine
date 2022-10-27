package servlets.agent;

import dtos.MissionDTO;
import entities.UBoat;
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
import java.util.Arrays;
import java.util.List;


@WebServlet(name = "AgentPostCandidatesServlet", urlPatterns = "/agent-post-candidates")
public class AgentPostCandidatesServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String agentName = req.getParameter("username");
        String allieMame = req.getParameter("allieName");

        String jsonManualData = "";
        Reader reader = req.getReader();
        int intValueOfChar;
        while ((intValueOfChar = reader.read()) != -1) {
            jsonManualData += (char) intValueOfChar;
        }
        reader.close();

        MissionDTO[] newCandidates = Constants.GSON_INSTANCE.fromJson(jsonManualData, MissionDTO[].class);
        List<MissionDTO> candidatesAsList = Arrays.asList(newCandidates);

        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        String battleName = userManager.getUBoatByGivenAgentName(agentName).getBattleName();
        userManager.addNewCandidates(battleName, candidatesAsList);
        userManager.lookForAWinner(battleName);

    }
}
