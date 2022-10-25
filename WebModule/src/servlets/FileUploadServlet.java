package servlets;

//taken from: http://www.servletworld.com/servlet-tutorials/servlet3/multipartconfig-file-upload-example.html
// and http://docs.oracle.com/javaee/6/tutorial/doc/glraq.html

import com.google.gson.Gson;
import dtos.engine.EngineMinimalDetailsDTO;
import entities.UBoat;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import logic.enigma.Engine;
import logic.enigma.EngineLoader;
import utils.ServletUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Scanner;

@WebServlet("/upload-file")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class FileUploadServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Engine engine = null;
        Collection<Part> parts = request.getParts();

        String userName = request.getParameter("username");

        StringBuilder fileContent = new StringBuilder();

        for (Part part : parts) {

            EngineLoader engineLoader = new EngineLoader();
            try {

                InputStream inputStream = part.getInputStream();
                engine = engineLoader.loadEngineFromInputStream(inputStream);

                inputStream.reset();

                fileContent.append(readFromInputStream(inputStream));

//                ServletUtils.getUserManager(getServletContext()).addInputStreamToMap(userName, fileContent.toString());
//                ServletUtils.getUserManager(getServletContext()).getInputStreamFromMap(userName);

//                engine = engineLoader.loadEngineFromInputStream(new ByteArrayInputStream(fileContent.toString().getBytes()));

                UBoat uBoat = ServletUtils.getUserManager(getServletContext()).getUBoat(userName);
                uBoat.setEngine(engine);
                ServletUtils.getUserManager(getServletContext()).addInputStreamToMap(userName, fileContent.toString());


            } catch (exceptions.invalidXMLfileException e) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.getOutputStream().println("Error: " + e.getMessage());
                return;
            }

            //overloading engine details on the response, in order to update uboat.
            response.setContentType("application/json");
            Gson gson = new Gson();
            EngineMinimalDetailsDTO engineMinimalDetailsDTO = engine.getEngineMinimalDetails();
            engineMinimalDetailsDTO.setUBoatDTO(engine.getUBoat().getBattleName(), engine.getUBoat().getDifficulty());
            String json = gson.toJson(engineMinimalDetailsDTO);

            try(PrintWriter out = response.getWriter()) {
                out.println(json);
                out.flush();
            }

        }
    }


    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }

}
