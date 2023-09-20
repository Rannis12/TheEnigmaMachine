package servlets.agent;

import dtos.entities.AgentDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servlets.agent.utils.ServletUtils;
import servlets.agent.utils.UserManager;

import java.io.IOException;
import java.io.Reader;

import static servlets.agent.utils.Constants.GSON_INSTANCE;


@WebServlet(name = "postAgentServlet", urlPatterns = "/post-agent")
public class PostAgentServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        final String type = "Agent";
        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        String jsonManualData = "";
        Reader reader = req.getReader();
        int intValueOfChar;
        while ((intValueOfChar = reader.read()) != -1) {
            jsonManualData += (char) intValueOfChar;
        }
        reader.close();

        AgentDTO agentDTO = GSON_INSTANCE.fromJson(jsonManualData, AgentDTO.class);

        String username = agentDTO.getAgentName();

        if (username == null || username.isEmpty()) {
            //no username in session and no username in parameter - not standard situation. it's a conflict

            // stands for conflict in server state
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
        } else {
            //normalize the username value
            username = username.trim();

            synchronized (this) {
                if (userManager.isUserExists(username)) {
                    String errorMessage = "Username " + username + " already exists. Please enter a different username.";

                    // stands for unauthorized as there is already such user with this name
                    resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    resp.getOutputStream().print(errorMessage);
                }
                else {
                    //add the new user to the users list
                    userManager.addUser(username, type);
                    userManager.setAgent(username, agentDTO);

                    //set the username in a session so it will be available on each request
                    //the true parameter means that if a session object does not exists yet
                    //create a new one
                    req.getSession(true).setAttribute("username", username);

                    //redirect the request to the chat room - in order to actually change the URL
                    System.out.println("On login, request URI is: " + req.getRequestURI());
                    resp.setStatus(HttpServletResponse.SC_OK);
                }
            }
        }
    }
}
