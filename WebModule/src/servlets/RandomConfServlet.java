package servlets;

import dtos.engine.EngineFullDetailsDTO;
import entities.UBoat;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.enigma.Engine;
//import util.Constants;
import utils.ServletUtils;
import utils.UserManager;

import java.io.IOException;
import java.io.PrintWriter;

import static logic.enigma.Engine.makeCodeForm;

//import static controllers.UBoatController.makeCodeForm;

@WebServlet(name = "RandomConfServlet", urlPatterns = "/random-conf")
public class RandomConfServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String username = req.getParameter("username");
        Engine engine = ServletUtils.getEngine(getServletContext(), username);
        engine.randomEngine();

        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        UBoat uBoat = userManager.getUBoat(username);
        uBoat.setEngine(engine);
//        userManager.setUBoat(engine.getBattleField(), username, engine);

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

