package servlets;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.UserManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@WebServlet(name = "BattleFieldServlet", urlPatterns = "/battlefield-list")
public class BattleFieldListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            Set<String> battleFieldNames = userManager.getBattleFieldNames();
            String json = gson.toJson(battleFieldNames);
            out.println(json);
            out.flush();
        }
    }

 /*   private void printGuestList(PrintWriter out) {
//        out.println("Guest List so far:");
        for (int i = 0; i < getGuestList().size(); i++) {
            out.println( *//*(i+1) + ". " +*//* getGuestList().get(i) );
        }
    }

    private List<String> getGuestList() {

        //option #2 - save the guest list in the servlet context
        //The Servlet Context is shared between all servlets in the web application
        List<String> guestsList = (List<String>) getServletContext().getAttribute(BATTLEFIELD_LIST_PROPERTY_NAME);
        if (guestsList == null) {
            guestsList = new ArrayList<>();

            getServletContext().setAttribute(BATTLEFIELD_LIST_PROPERTY_NAME, guestsList);
        }

        return guestsList;
    }*/
}
