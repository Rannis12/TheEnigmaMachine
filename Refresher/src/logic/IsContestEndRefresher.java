package logic;

import client.http.HttpClientUtil;
import com.sun.istack.internal.NotNull;
import dtos.MissionDTO;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import servlets.agent.utils.Constants;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static servlets.agent.utils.Constants.GSON_INSTANCE;

public class IsContestEndRefresher extends TimerTask {

    private final Consumer<MissionDTO> consumer;
    private String battleName;
    private String username;
    private String clientName;


    public IsContestEndRefresher(Consumer<MissionDTO> consumer, String battleName, String username, String clientName) {
        this.consumer = consumer;
        this.battleName = battleName;
        this.username = username;
        this.clientName = clientName;
    }

    @Override
    public void run() {

        String finalUrl = HttpUrl
                .parse(Constants.IS_THERE_A_WINNER_PATH)
                .newBuilder()
                .addQueryParameter("battleName", battleName)
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
                String jsonDto = response.body().string();
                jsonDto = jsonDto.replaceAll("(\\r|\\n|\\t)", "");

                MissionDTO dto = GSON_INSTANCE.fromJson(jsonDto, MissionDTO.class);
                consumer.accept(dto);
            }
        });
    }
}
