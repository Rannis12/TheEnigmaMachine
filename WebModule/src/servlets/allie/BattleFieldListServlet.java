package servlets.allie;

import com.google.gson.Gson;
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

@WebServlet(name = "BattleFieldsServlet", urlPatterns = "/battlefields-list")
public class BattleFieldListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            Set<ContestDetailsDTO> battleFields = userManager.getBattleFields();
            String json = gson.toJson(battleFields);
            out.println(json);
            out.flush();
        }
    }
}
