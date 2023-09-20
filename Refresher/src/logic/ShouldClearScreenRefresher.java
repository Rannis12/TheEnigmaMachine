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

import static servlets.agent.utils.Constants.GSON_INSTANCE;

public class ShouldClearScreenRefresher extends TimerTask {
    private final Consumer<Boolean> consumer;
    private final String allyName;
    private final String client;
    private final String uBoatName;

    public ShouldClearScreenRefresher(Consumer<Boolean> consumer, String allyName, String client, String uBoatName) {
        this.consumer = consumer;
        this.allyName = allyName;
        this.client = client;
        this.uBoatName = uBoatName;
    }

    @Override
    public void run() {

        String finalUrl = HttpUrl
                .parse(Constants.DID_CLEAR_BTN_CLICKED)
                .newBuilder()
                .addQueryParameter("allyName", allyName)
                .addQueryParameter("client", client)
                .addQueryParameter("uBoatName", uBoatName)
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("bad");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String json = response.body().string();
                Boolean dto = GSON_INSTANCE.fromJson(json, Boolean.class);
                consumer.accept(dto);
            }
        });
    }
}
