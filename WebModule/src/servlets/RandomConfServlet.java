package servlets;

import com.google.gson.Gson;
import dtos.EngineFullDetailsDTO;
import dtos.EngineMinimalDetailsDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.enigma.Engine;
import util.Constants;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;

import static controllers.UBoatController.makeCodeForm;

@WebServlet(name = "RandomConfServlet", urlPatterns = "/random-conf")
public class RandomConfServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Engine engine = ServletUtils.getEngine(getServletContext());
        engine.randomEngine();

        resp.setContentType("text/plain");

        EngineFullDetailsDTO engineFullDetailsDTO = engine.getEngineFullDetails();

        String configuration = makeCodeForm(engineFullDetailsDTO.getNotchesCurrentPlaces(), engineFullDetailsDTO.getUsedRotorsOrganization(),
                engineFullDetailsDTO.getRotorsCurrentPositions(), engineFullDetailsDTO.getChosenReflector());


        try (PrintWriter out = resp.getWriter()) {
            out.println(configuration);
            out.flush();
        }

    }

}
