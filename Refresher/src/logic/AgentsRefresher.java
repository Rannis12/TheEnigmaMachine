package logic;

import client.http.HttpClientUtil;
import com.sun.istack.internal.NotNull;
import dtos.entities.AgentDTO;
import dtos.web.ContestDetailsDTO;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import utils.Constants;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static utils.Constants.GSON_INSTANCE;

public class AgentsRefresher extends TimerTask {
    private final Consumer<List<AgentDTO>> agentsListConsumer;

    public AgentsRefresher(Consumer<List<AgentDTO>> agentsListConsumer) {
        this.agentsListConsumer = agentsListConsumer;
    }

    @Override
    public void run() {

        HttpClientUtil.runAsync(Constants.AGENT_LIST, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("bad");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonArrayOfDtos = response.body().string();
                AgentDTO[] dtos = GSON_INSTANCE.fromJson(jsonArrayOfDtos, AgentDTO[].class);
                agentsListConsumer.accept(Arrays.asList(dtos));
            }
        });
    }

}

