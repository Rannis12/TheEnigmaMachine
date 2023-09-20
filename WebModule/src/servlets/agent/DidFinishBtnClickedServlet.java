package servlets.agent;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servlets.agent.utils.Constants;
import servlets.agent.utils.ServletUtils;
import servlets.agent.utils.UserManager;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "DidFinishBtnClickedServlet", urlPatterns = "/did-finishBtn-clicked")
public class DidFinishBtnClickedServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        String allyName = req.getParameter("allyName");

        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        boolean isConfirm = userManager.getAlly(allyName).isConfirmLogout();

        String json = Constants.GSON_INSTANCE.toJson(isConfirm, Boolean.class);
        out.println(json);
        out.flush();
    }
}
