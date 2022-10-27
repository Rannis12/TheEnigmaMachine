package logic;

import client.http.HttpClientUtil;
import com.sun.istack.internal.NotNull;
import dtos.MissionDTO;
import dtos.web.ContestDetailsDTO;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import utils.Constants;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static utils.Constants.GSON_INSTANCE;

public class GetCandidatesRefresher extends TimerTask {
    private final Consumer<List<MissionDTO>> missionDTOConsumer;
    private String username;
    private String client;


    public GetCandidatesRefresher(Consumer<List<MissionDTO>> missionDTOConsumer, String username, String client) {
        this.missionDTOConsumer = missionDTOConsumer;
        this.username = username;
        this.client = client;

    }

    @Override
    public void run() {

        String finalUrl = HttpUrl
                .parse(Constants.GET_CANDIDATES_PATH)
                .newBuilder()
                .addQueryParameter("username", username)
                .addQueryParameter("client", client)
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("bad");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonArrayOfDto = response.body().string();
                MissionDTO[] dtos = GSON_INSTANCE.fromJson(jsonArrayOfDto, MissionDTO[].class);
                missionDTOConsumer.accept(Arrays.asList(dtos));
            }
        });
    }
}
