
package servlets;

import com.google.gson.Gson;
import dtos.DecodedStringAndConfigurationDTO;
import dtos.EngineFullDetailsDTO;
import exceptions.invalidInputException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import logic.enigma.Engine;

import utils.ServletUtils;

import java.io.IOException;

//import static controllers.UBoatController.makeCodeForm;
import static logic.enigma.Engine.makeCodeForm;
//import static util.Constants.GSON_INSTANCE;


@WebServlet(name = "DecodeString", urlPatterns = "/decode-string")
public class DecodedStringServlet extends HttpServlet {
//    public final static OkHttpClient HTTP_CLIENT = new OkHttpClient(); //?? not sure how to use this object.
//    public final String BASE_URL = "http://localhost:8080";
//    public final String ADD_UPLOAD_STRING = "upload-string";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Engine engine = ServletUtils.getEngine(getServletContext());
        String decodedString = null;
        resp.setContentType("text/plain");
        try {
            decodedString = engine.decodeStr(req.getParameter("toDecode")).getDecodedString();


        } catch (invalidInputException e) {
            throw new RuntimeException("couldn't decode the string.");
        }

        EngineFullDetailsDTO engineFullDetailsDTO = engine.getEngineFullDetails();
        String configuration = makeCodeForm(engineFullDetailsDTO.getNotchesCurrentPlaces(), engineFullDetailsDTO.getUsedRotorsOrganization(),
                engineFullDetailsDTO.getRotorsCurrentPositions(), engineFullDetailsDTO.getChosenReflector());

        DecodedStringAndConfigurationDTO dto = new DecodedStringAndConfigurationDTO(decodedString, configuration);

        String json = new Gson().toJson(dto, DecodedStringAndConfigurationDTO.class);

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