package servlets;

import dtos.engine.EngineFullDetailsDTO;
import dtos.InitializeEngineToJsonDTO;
import entities.UBoat;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.enigma.Engine;
import utils.ServletUtils;
import utils.UserManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;

import static logic.enigma.Engine.makeCodeForm;
import static utils.Constants.GSON_INSTANCE;

//import static controllers.UBoatController.makeCodeForm;
//import static util.Constants.GSON_INSTANCE;

@WebServlet(name = "ManualConfServlet", urlPatterns = "/manual-conf")
public class ManualConfServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/plain");
        String jsonManualData = "";
        Reader reader = req.getReader();
        int intValueOfChar;
        while ((intValueOfChar = reader.read()) != -1) {
            jsonManualData += (char) intValueOfChar;
        }
        reader.close();

        InitializeEngineToJsonDTO initializeEngineToJsonDTO = GSON_INSTANCE.fromJson(jsonManualData, InitializeEngineToJsonDTO.class);

        String username = req.getParameter("username");

        Engine engine = ServletUtils.getEngine(getServletContext(), username);

        engine.setNewMachine(initializeEngineToJsonDTO.getRotorsFirstPositionDTO(), initializeEngineToJsonDTO.getPlugBoardDTO(),
                initializeEngineToJsonDTO.getReflectorDTO(), initializeEngineToJsonDTO.getRotorsIndexesDTO());

        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        UBoat uBoat = userManager.getUBoat(username);
        uBoat.setEngine(engine);
//        userManager.setUBoat(engine.getBattleField(), username, engine);

        EngineFullDetailsDTO engineFullDetailsDTO = engine.getEngineFullDetails();

        String configuration = makeCodeForm(engineFullDetailsDTO.getNotchesCurrentPlaces(), engineFullDetailsDTO.getUsedRotorsOrganization(),
                engineFullDetailsDTO.getRotorsCurrentPositions(), engineFullDetailsDTO.getChosenReflector());

        try (PrintWriter out = resp.getWriter()) {
            out.println(configuration);
            out.flush();
        }

    }
}
