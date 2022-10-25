package logic;

import client.http.HttpClientUtil;
import com.sun.istack.internal.NotNull;
import dtos.web.ShouldStartContestDTO;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import utils.Constants;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static utils.Constants.GSON_INSTANCE;

public class ContestStatusRefresher extends TimerTask {
    private final Consumer<ShouldStartContestDTO> consumer;
    private String battleName;
    private String agentName;
    private String clientName;


    public ContestStatusRefresher(Consumer<ShouldStartContestDTO> consumer, String battleName, String agentName, String clientName) {
        this.consumer = consumer;
        this.battleName = battleName;
        this.agentName = agentName;
        this.clientName = clientName;
    }

    @Override
    public void run() {

        String finalUrl = HttpUrl
                .parse(Constants.SHOULD_START_CONTEST_PATH)
                .newBuilder()
                .addQueryParameter("battleName", battleName)
                .addQueryParameter("client", clientName)
                .addQueryParameter("agentName", agentName)
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
                ShouldStartContestDTO dto = GSON_INSTANCE.fromJson(jsonDto, ShouldStartContestDTO.class);
                consumer.accept(dto);
            }
        });
    }
}