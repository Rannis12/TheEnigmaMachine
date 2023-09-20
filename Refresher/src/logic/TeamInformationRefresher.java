package logic;

import client.http.HttpClientUtil;
import com.sun.istack.internal.NotNull;
import dtos.TeamInformationDTO;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import servlets.agent.utils.Constants;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static servlets.agent.utils.Constants.GSON_INSTANCE;

public class TeamInformationRefresher extends TimerTask {
    private final Consumer<List<TeamInformationDTO>> userListConsumer;
    private int requestNumber;
    private String battleName;


    public TeamInformationRefresher(Consumer<List<TeamInformationDTO>> usersListConsumer, String battleName) {
        this.userListConsumer = usersListConsumer;
        this.battleName = battleName;
        requestNumber = 0;
    }

    @Override
    public void run() {

        String finalUrl = HttpUrl
                .parse(Constants.ALLIES_BELONGS_TO_BATTLE_FIELD)
                .newBuilder()
                .addQueryParameter("battleName", battleName)
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
                TeamInformationDTO[] dtos = GSON_INSTANCE.fromJson(jsonArrayOfDto, TeamInformationDTO[].class);
                userListConsumer.accept(Arrays.asList(dtos));
            }
        });
    }
}