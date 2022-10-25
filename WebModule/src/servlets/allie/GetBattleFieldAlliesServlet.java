package servlets.allie;

import com.google.gson.Gson;
import dtos.TeamInformationDTO;
import dtos.web.ContestDetailsDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.UserManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@WebServlet(name = "GetBattleFieldAllies", urlPatterns = "/allies-belongs-to-battleField")
public class GetBattleFieldAlliesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String battleName = req.getParameter("battleName");

        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            Set<TeamInformationDTO> battleField = userManager.getAlliesBelongsToBattleField(battleName);
            String json = gson.toJson(battleField);
            out.println(json);
            out.flush();
        }
    }
}
