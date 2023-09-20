package servlets;

import dtos.engine.EngineFullDetailsDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.enigma.Engine;
import servlets.agent.utils.ServletUtils;
import servlets.agent.utils.UserManager;

import java.io.IOException;

import static logic.enigma.Engine.makeCodeForm;

//import static controllers.UBoatController.makeCodeForm;

@WebServlet(name = "ResetEngineSettingsServlet", urlPatterns = "/reset-engine")
public class ResetEngineSettingsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/plain");

        //working only when there's only one uBoat, in case there's more, we will need to request the specific uboat engine.

        String username = req.getParameter("username");

        Engine engine = ServletUtils.getEngine(getServletContext(), username);
        engine.resetEngineToUserInitChoice();

        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        userManager.getUBoat(username).setEngine(engine);

        EngineFullDetailsDTO engineFullDetailsDTO = engine.getEngineFullDetails();

        String configuration = makeCodeForm(engineFullDetailsDTO.getNotchesCurrentPlaces(), engineFullDetailsDTO.getUsedRotorsOrganization(),
                engineFullDetailsDTO.getRotorsCurrentPositions(), engineFullDetailsDTO.getChosenReflector());

        resp.getWriter().println(configuration);
    }
}
