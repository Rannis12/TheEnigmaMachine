package servlets;/*
package uboat.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import okhttp3.*;

import java.io.IOException;

@WebServlet(name = "UploadString", urlPatterns = "upload-string")
public class DecodedStringServlet extends HttpServlet {
    public final static OkHttpClient HTTP_CLIENT = new OkHttpClient(); //?? not sure how to use this object.
    public final String BASE_URL = "http://localhost:8080";
    public final String ADD_UPLOAD_STRING = "upload-string";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

//        String toEncodeString = "abc";
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
        Response response = call.execute();

    }
}
*/
