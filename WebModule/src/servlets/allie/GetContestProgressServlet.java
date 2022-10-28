package servlets.allie;

import dtos.web.ContestProgressDTO;
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

@WebServlet(name = "GetContestProgressServlet", urlPatterns = "/get-progress")
public class GetContestProgressServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        String allyName = req.getParameter("allyName");

        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        ContestProgressDTO contestProgressDTO = userManager.getContestDetailsForAlly(allyName);

        String json = Constants.GSON_INSTANCE.toJson(contestProgressDTO, ContestProgressDTO.class);
        out.println(json);
        out.flush();

    }
}
