package servlets.agent;

import com.google.gson.Gson;
import dtos.entities.AllieDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servlets.agent.utils.ServletUtils;
import servlets.agent.utils.UserManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;


//NOT WORKING!!!!!!!!!!!!
@WebServlet(name = "GetAlliesList", urlPatterns = "/get-allies")
public class GetAlliesList extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            Set<AllieDTO> allies = userManager.getAllies();
            String json = gson.toJson(allies);
            out.println(json);
            out.flush();
        }
    }
}