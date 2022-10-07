package uboat.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "NewBattleFieldServlet", urlPatterns = "/battleFieldList/new")
public class BattleFieldIntroductionServlet extends HttpServlet {

    private static final String BATTLEFIELD_NAME_PARAMETER = "battleField";
    public static final String BATTLEFIELD_LIST_PROPERTY_NAME = "battleFieldList";

//    private List<String> guestsList;

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain;charset=UTF-8");

        try (PrintWriter out = resp.getWriter()) {
            String guestName = req.getParameter(BATTLEFIELD_NAME_PARAMETER);

            if (shouldAddGuest(guestName)) {
                getGuestList().add(guestName);
            }

            out.println("Guest [" + guestName + "] was added successfully to the guest list");

        }
    }

    private boolean shouldAddGuest (String guestName){
        return guestName != null && !guestName.isEmpty();
    }

    private List<String> getGuestList() {
      
        //option #1 - save the guest list as a data member of the servlet - not that good - servlet should be state less !
        // though you can on destroy handle persistence of the data
//        if (guestsList == null) {
//            guestsList = new ArrayList<>();
//        }

        //option #2 - save the guest list in the servlet context
        //The Servlet Context is shared between all servlets in the web application
        List<String> guestsList = (List<String>) getServletContext().getAttribute(BATTLEFIELD_LIST_PROPERTY_NAME);
        if (guestsList == null) {
            guestsList = new ArrayList<>();

            getServletContext().setAttribute(BATTLEFIELD_LIST_PROPERTY_NAME, guestsList);
        }
        //option #3 - save the guest list in the session context
        //The Session Context is unique for each browser that is connected to the server
//        HttpSession session = request.getSession();
//        List<String> guestsList = (List<String>) session.getAttribute(GUESTS_LIST_PROPERTY_NAME);
//        if (guestsList == null) {
//            guestsList = new ArrayList<>();
//            session.setAttribute(GUESTS_LIST_PROPERTY_NAME, guestsList);
//        }
        return guestsList;
    }
}
