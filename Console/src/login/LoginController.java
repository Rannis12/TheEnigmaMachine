package login;

import client.http.HttpClientUtil;
import com.sun.istack.internal.NotNull;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField nameTF;

    @FXML
    private Button loginBtn;

    @FXML
    void loginBtnListener(ActionEvent event) {

        String userName = nameTF.getText();
        if (userName.isEmpty()) {
           // errorMessageProperty.set("User name is empty. You can't login with empty user name");
            return;
        }

        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse("http://localhost:8080/loginShortResponse")
                .newBuilder()
                .addQueryParameter("username", userName)
                .build()
                .toString();

//        updateHttpStatusLine("New request is launched for: " + finalUrl);

        HttpClientUtil.runAsync(finalUrl, new Callback() { //logic is working. need to change the way it looks in the application.

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        nameTF.setText("wrong")
//                        System.out.println("Something went wrong: " + e.getMessage())
//                        errorMessageProperty.set("Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            nameTF.setText("false")
//                            errorMessageProperty.set("Something went wrong: " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        nameTF.setText("ok");
//                        chatAppMainController.updateUserName(userName);
//                        chatAppMainController.switchToChatRoom();
                    });
                }
            }
        });
    }

}
