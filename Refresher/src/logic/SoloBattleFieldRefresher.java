package logic;

import client.http.HttpClientUtil;
import com.sun.istack.internal.NotNull;
import dtos.web.ContestDetailsDTO;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import utils.Constants;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static utils.Constants.GSON_INSTANCE;

public class SoloBattleFieldRefresher extends TimerTask {
    private final Consumer<ContestDetailsDTO> userListConsumer;
    private int requestNumber;
    private String battleName;


    public SoloBattleFieldRefresher(Consumer<ContestDetailsDTO> usersListConsumer, String battleName) {
        this.userListConsumer = usersListConsumer;
        this.battleName = battleName;
        requestNumber = 0;
    }

    @Override
    public void run() {

//        final int finalRequestNumber = ++requestNumber;
//        httpRequestLoggerConsumer.accept("About to invoke: " + Constants.BATTLE_FIELD_LIST + " | Users Request # " + finalRequestNumber);
        String finalUrl = HttpUrl
                .parse(Constants.SOLO_BATTLE_FIELD_UPDATE)
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
                ContestDetailsDTO dto = GSON_INSTANCE.fromJson(jsonArrayOfDto, ContestDetailsDTO.class);
                userListConsumer.accept(dto);
            }
        });
    }
}
