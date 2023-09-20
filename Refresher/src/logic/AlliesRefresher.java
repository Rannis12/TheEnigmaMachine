package logic;

import client.http.HttpClientUtil;
import com.sun.istack.internal.NotNull;
import dtos.entities.AllieDTO;
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

public class AlliesRefresher extends TimerTask {
    private final Consumer<List<AllieDTO>> alliesConsumer;

    public AlliesRefresher(Consumer<List<AllieDTO>> alliesConsumer) {
        this.alliesConsumer = alliesConsumer;
    }
    @Override
    public void run() {

        HttpClientUtil.runAsync(Constants.GET_ALLIES_LIST_PATH, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("bad");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonArrayOfDtos = response.body().string();
                AllieDTO[] dtos = GSON_INSTANCE.fromJson(jsonArrayOfDtos, AllieDTO[].class);
                alliesConsumer.accept(Arrays.asList(dtos));
            }
        });
    }
}