package logic;
import client.http.HttpClientUtil;
import com.sun.istack.internal.NotNull;
import dtos.web.ContestDetailsDTO;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import servlets.agent.utils.Constants;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static servlets.agent.utils.Constants.GSON_INSTANCE;

public class BattleFieldRefresher extends TimerTask {
    private final Consumer<List<ContestDetailsDTO>> usersListConsumer;
    public BattleFieldRefresher(Consumer<List<ContestDetailsDTO>> usersListConsumer) {
        this.usersListConsumer = usersListConsumer;
    }

    @Override
    public void run() {

        HttpClientUtil.runAsync(Constants.BATTLE_FIELD_LIST, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("bad");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonArrayOfDtos = response.body().string();
                ContestDetailsDTO[] dtos = GSON_INSTANCE.fromJson(jsonArrayOfDtos, ContestDetailsDTO[].class);
                usersListConsumer.accept(Arrays.asList(dtos));
            }
        });
    }
}
