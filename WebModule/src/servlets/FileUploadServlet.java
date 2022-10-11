package servlets;

//taken from: http://www.servletworld.com/servlet-tutorials/servlet3/multipartconfig-file-upload-example.html
// and http://docs.oracle.com/javaee/6/tutorial/doc/glraq.html

import com.google.gson.Gson;
import dtos.EngineFullDetailsDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import logic.enigma.Engine;
import logic.enigma.EngineLoader;
import resources.jaxb.generated.CTEEnigma;
import utils.ServletUtils;
import utils.UserManager;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Scanner;
import java.util.Set;

@WebServlet("/upload-file")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class FileUploadServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
//        PrintWriter out = response.getWriter();

        Collection<Part> parts = request.getParts();

//        out.println("Total parts : " + parts.size());

        StringBuilder fileContent = new StringBuilder();

        for (Part part : parts) {
            /*printPart(part, out);

            //to write the content of the file to an actual file in the system (will be created at c:\samplefile)
            part.write("samplefile");


            //to write the content of the file to a string
            fileContent.append(readFromInputStream(part.getInputStream()));*/

            Engine engine = ServletUtils.getEngine(getServletContext());

            EngineLoader engineLoader = new EngineLoader();
            try {
                engine = engineLoader.loadEngineFromInputStream(part.getInputStream());
            } catch (exceptions.invalidXMLfileException e) {
                throw new RuntimeException(e);
            }

            //overloading engine details on the response, in order to update uboat.
            response.setContentType("application/json");
            Gson gson = new Gson();
            EngineFullDetailsDTO engineFullDetailsDTO = engine.getEngineFullDetails();
            String json = gson.toJson(engineFullDetailsDTO);

            try(PrintWriter out = response.getWriter()) {
                out.println(json);
                out.flush();
            }

        }

//        printFileContent(fileContent.toString(), out);
    }

    private void printPart(Part part, PrintWriter out) {
        StringBuilder sb = new StringBuilder();
        sb
            .append("Parameter Name: ").append(part.getName()).append("\n")
            .append("Content Type (of the file): ").append(part.getContentType()).append("\n")
            .append("Size (of the file): ").append(part.getSize()).append("\n")
            .append("Part Headers:").append("\n");

        for (String header : part.getHeaderNames()) {
            sb.append(header).append(" : ").append(part.getHeader(header)).append("\n");
        }

        out.println(sb.toString());
    }

    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }

    private void printFileContent(String content, PrintWriter out) {
        out.println("File content:");
        out.println(content);
    }
}
