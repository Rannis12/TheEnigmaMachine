package servlets.agent;

import dtos.engine.EngineFullDetailsDTO;
import dtos.web.DataToAgentEngineDTO;
import entities.UBoat;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.enigma.Engine;
import utils.Constants;
import utils.ServletUtils;
import utils.UserManager;

import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name = "getFileContentServlet", urlPatterns = "/get-fileContent")
public class GetFileContentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");

        String agentName = req.getParameter("agentName");

        PrintWriter out = resp.getWriter();

        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        UBoat uBoat = userManager.getUBoatByGivenAgentName(agentName);

        Engine engine = uBoat.getEngine();
        EngineFullDetailsDTO engineFullDetailsDTO = engine.getEngineFullDetails();

        String inputStreamAsString = userManager.getInputStreamFromMap(uBoat.getBattleName());

        DataToAgentEngineDTO dto = new DataToAgentEngineDTO(engineFullDetailsDTO.getUsedRotorsOrganization(), engineFullDetailsDTO.getNotchesCurrentPlaces(),
                inputStreamAsString, engine.getSelectedReflector());

        String json = Constants.GSON_INSTANCE.toJson(dto, DataToAgentEngineDTO.class);

        out.println(json);
        out.flush();

    }
}
