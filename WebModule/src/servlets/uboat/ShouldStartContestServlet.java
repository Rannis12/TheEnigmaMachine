package servlets.uboat;

import dtos.web.ShouldStartContestDTO;
import entities.UBoat;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.Constants;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ShouldStartContestServlet", urlPatterns = "/wait-for-uboat-confirmation")
public class ShouldStartContestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");

        String battleName = req.getParameter("battleName");
        String client = req.getParameter("client");
        String agentName = req.getParameter("agentName");
        ShouldStartContestDTO Dto = null;

        if(client.equals("Ally")) {
            Dto = ServletUtils.getUserManager(getServletContext())
                    .doesEveryOneReady(battleName);
        }else{ //he is probably agent.
            UBoat uBoat = ServletUtils.getUserManager(getServletContext()).getUBoatByGivenAgentName(agentName);
            Dto = ServletUtils.getUserManager(getServletContext()).
                    doesEveryOneReady(uBoat.getBattleName());
        }

        if(Dto.isShouldStart()){
//            ServletUtils.getUserManager(getServletContext()).startCreateMissions(uBoat.getBattleName()); //start create all the missions. - need to check how to make this method occur only one time. todo
        }

        String json = Constants.GSON_INSTANCE.toJson(Dto, ShouldStartContestDTO.class);
        try(PrintWriter out = resp.getWriter()){
            out.println(json);
            out.flush();
        }
    }
}
