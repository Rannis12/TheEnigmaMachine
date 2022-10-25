package servlets.allie;

import dtos.entities.AllieDTO;
import dtos.entities.UBoatDTO;
import entities.UBoat;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.UserManager;

import java.io.IOException;
import java.io.Reader;

import static utils.Constants.GSON_INSTANCE;
import static utils.Constants.PARENT_NAME_PARAMETER;

@WebServlet(name = "JoinToUBoatServlet", urlPatterns = "/join-uboat")
public class JoiningToUBoatServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String usernameFromParameter = req.getParameter("username");
        String uBoatName = req.getParameter(PARENT_NAME_PARAMETER);

        String jsonManualData = "";
        Reader reader = req.getReader();
        int intValueOfChar;
        while ((intValueOfChar = reader.read()) != -1) {
            jsonManualData += (char) intValueOfChar;
        }
        reader.close();

        AllieDTO allieDto = GSON_INSTANCE.fromJson(jsonManualData, AllieDTO.class);

        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        userManager.setAllie(usernameFromParameter, uBoatName, allieDto);
        UBoat uBoat = userManager.getUBoatByName(uBoatName);
        UBoatDTO uBoatDTO = new UBoatDTO(uBoat.getBattleName(), uBoat.getDifficulty(), uBoat.getGameStatus(),
                uBoat.getMaxAmountOfAllies(), uBoat.getCurrentAmountOfAllies());

        String json = GSON_INSTANCE.toJson(uBoatDTO, UBoatDTO.class);

        resp.getWriter().println(json);
    }
}
