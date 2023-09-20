package servlets.allie;

import entities.Allie;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servlets.agent.utils.Constants;
import servlets.agent.utils.ServletUtils;

import java.io.IOException;
import java.io.Reader;

@WebServlet(name = "isReadyServlet", urlPatterns = "/ready-status")
public class IsReadyServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String jsonManualData = "";
        Reader reader = req.getReader();
        int intValueOfChar;
        while ((intValueOfChar = reader.read()) != -1) {
            jsonManualData += (char) intValueOfChar;
        }
        reader.close();

        String json = Constants.GSON_INSTANCE.fromJson(jsonManualData, String.class);
        int amountOfMissions = Integer.parseInt(json);

        String isReady = req.getParameter("status"); //Ready notReady
        String username = req.getParameter("username");
        String battleField = req.getParameter("battleField");

        Allie ally = ServletUtils.getUserManager(getServletContext()).getAlly(username);
        if(ally.getAmountOfAgents() == 0){
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getOutputStream().println("please add at least 1 agent");
            return;
        }

        ServletUtils.getUserManager(getServletContext()).setAllieStatus(username, battleField, isReady, amountOfMissions);

    }
}
