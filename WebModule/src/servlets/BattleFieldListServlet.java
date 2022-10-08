package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

//import static examples.servletcontext.store.GuestIntroductionServlet.GUESTS_LIST_PROPERTY_NAME;

@WebServlet(name = "BattleFieldServlet", urlPatterns = "/battleFieldList")
public class BattleFieldListServlet extends HttpServlet {
    public static final String BATTLEFIELD_LIST_PROPERTY_NAME = "battleFieldList";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain;charset=UTF-8");

        try (PrintWriter out = resp.getWriter()) {
            printGuestList(out);
        }
    }

    private void printGuestList(PrintWriter out) {
//        out.println("Guest List so far:");
        for (int i = 0; i < getGuestList().size(); i++) {
            out.println( /*(i+1) + ". " +*/ getGuestList().get(i) );
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
    }
}
