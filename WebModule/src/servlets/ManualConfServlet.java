package servlets;

import com.google.gson.Gson;
import dtos.EngineFullDetailsDTO;
import dtos.InitializeEngineToJsonDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import logic.enigma.Engine;
import utils.ServletUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Collection;
import java.util.Scanner;

import static controllers.UBoatController.makeCodeForm;
import static util.Constants.GSON_INSTANCE;

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

        Engine engine = ServletUtils.getEngine(getServletContext());
        engine.setNewMachine(initializeEngineToJsonDTO.getRotorsFirstPositionDTO(), initializeEngineToJsonDTO.getPlugBoardDTO(),
                initializeEngineToJsonDTO.getReflectorDTO(), initializeEngineToJsonDTO.getRotorsIndexesDTO());

        EngineFullDetailsDTO engineFullDetailsDTO = engine.getEngineFullDetails();

        String configuration = makeCodeForm(engineFullDetailsDTO.getNotchesCurrentPlaces(), engineFullDetailsDTO.getUsedRotorsOrganization(),
                engineFullDetailsDTO.getRotorsCurrentPositions(), engineFullDetailsDTO.getChosenReflector());

        try (PrintWriter out = resp.getWriter()) {
            out.println(configuration);
            out.flush();
        }

    }
}
