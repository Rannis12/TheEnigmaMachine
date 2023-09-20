package logic;

import client.http.HttpClientUtil;
import com.sun.istack.internal.NotNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import servlets.agent.utils.Constants;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;


public class DidUBoatLoggedOutRefresher extends TimerTask {
    private final Consumer<Boolean> consumer;
    private final String username;
    private final String clientName;


    public DidUBoatLoggedOutRefresher(Consumer<Boolean> consumer, String username, String clientName) {
        this.consumer = consumer;
        this.username = username;
        this.clientName = clientName;
    }

    @Override
    public void run() {

        String finalUrl = HttpUrl
                .parse(Constants.DID_UBOAT_LOGGED_OUT_PATH)
                .newBuilder()
                .addQueryParameter("client", clientName)
                .addQueryParameter("username", username)
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("bad");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                String json = Constants.GSON_INSTANCE.fromJson(responseBody, String.class);

                if(json.equals("true")){
                    consumer.accept(true);
                }

            }
        });
    }
}
