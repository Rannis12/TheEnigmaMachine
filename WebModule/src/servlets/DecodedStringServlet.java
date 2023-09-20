package servlets;

import dtos.DecodedStringAndConfigurationDTO;
import dtos.engine.EngineFullDetailsDTO;
import entities.UBoat;
import exceptions.invalidInputException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import logic.enigma.Engine;

import servlets.agent.utils.Constants;
import servlets.agent.utils.ServletUtils;
import servlets.agent.utils.UserManager;

import java.io.IOException;

import static logic.enigma.Engine.makeCodeForm;

@WebServlet(name = "DecodeString", urlPatterns = "/decode-string")
public class DecodedStringServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        Engine engine = ServletUtils.getEngine(getServletContext(), username);
        String toDecode = req.getParameter("toDecode");
        String decodedString = null;

        resp.setContentType("text/plain");

        try {
            decodedString = engine.decodeStrWithoutPG(toDecode).getDecodedString();

            //setting the engine again after decoding.

            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            UBoat uBoat = userManager.getUBoat(username);
            uBoat.setEngine(engine);

            uBoat.setToDecode(toDecode);
            uBoat.setToEncode(decodedString);

//            ServletUtils.setToDecodeString(toDecode);
//            ServletUtils.setToEncodeString(decodedString);
//            ServletUtils.getUserManager(getServletContext()).setUBoatStatus(username, );

        } catch (invalidInputException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getOutputStream().println("couldn't decode the string.");
            return;
        }

        EngineFullDetailsDTO engineFullDetailsDTO = engine.getEngineFullDetails();
        String configuration = makeCodeForm(engineFullDetailsDTO.getNotchesCurrentPlaces(), engineFullDetailsDTO.getUsedRotorsOrganization(),
                engineFullDetailsDTO.getRotorsCurrentPositions(), engineFullDetailsDTO.getChosenReflector());

        DecodedStringAndConfigurationDTO dto = new DecodedStringAndConfigurationDTO(decodedString, configuration);

        String json = Constants.GSON_INSTANCE.toJson(dto, DecodedStringAndConfigurationDTO.class);

        resp.getWriter().println(json);

    }
}



/*//        String toEncodeString = "abc";
        String toEncodeString = req.getParameter("toEncode"); //getting "toEncode" String from the queryParameter.


        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + ADD_UPLOAD_STRING).newBuilder();
        urlBuilder.addQueryParameter("toEncode", toEncodeString);
        String finalUrl = urlBuilder.build().toString();
        System.out.println("Adding the String: " + toEncodeString + " (" + finalUrl + ")");

        Request request = new Request.Builder()
                .url(finalUrl)
                .put(RequestBody.create(new byte[]{}))
                .build();

        Call call = HTTP_CLIENT.newCall(request);
        Response response = call.execute();*/