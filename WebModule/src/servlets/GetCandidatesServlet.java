package servlets;

import dtos.MissionDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.Constants;
import utils.ServletUtils;
import utils.UserManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "GetCandidatesServlet", urlPatterns = "/get-candidates")
public class GetCandidatesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        String client = req.getParameter("client");
        String userName = req.getParameter("username");
        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        List<MissionDTO> dtoList = userManager.getCandidates(userName, client);

        String json = Constants.GSON_INSTANCE.toJson(dtoList);

        out.println(json);
        out.flush();
    }
}
