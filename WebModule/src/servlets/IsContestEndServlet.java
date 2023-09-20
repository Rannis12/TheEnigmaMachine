package servlets;

import dtos.MissionDTO;
import entities.UBoat;
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

@WebServlet(name = "IsContestEndServlet", urlPatterns = "/is-contest-end")
public class IsContestEndServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        String battleName = req.getParameter("battleName");
        String client = req.getParameter("client");
        String userName = req.getParameter("username");

        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        MissionDTO missionDTO = null;

        if(client.equals("Agent")) {
            String localBattleName = userManager.getUBoatByGivenAgentName(userName).getBattleName();
            missionDTO = userManager.thereIsAWinner(localBattleName);

        }else if(client.equals("Ally")){
            UBoat uBoat = userManager.getUBoatByGivenAllyName(userName);
            missionDTO = userManager.thereIsAWinner(uBoat.getBattleName());

        }
        else{
            missionDTO = userManager.thereIsAWinner(battleName);

        }
        String json = Constants.GSON_INSTANCE.toJson(missionDTO, MissionDTO.class);

        out.println(json);
        out.flush();
    }
}
