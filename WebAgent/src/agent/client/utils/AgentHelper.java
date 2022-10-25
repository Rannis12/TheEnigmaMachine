/*
package agent.client.utils;

import client.http.HttpClientUtil;
import com.sun.istack.internal.NotNull;
import dtos.entities.AgentDTO;
import dtos.web.DecryptTaskDTO;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import utils.Constants;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static utils.Constants.GSON_INSTANCE;

public class AgentHelper {

    private final Consumer<List<DecryptTaskDTO>> agentsListConsumer;
    private final String agentName;
    private final int amountOfMissions;
    public AgentHelper(Consumer<List<DecryptTaskDTO>> agentsListConsumer, String agentName, int amountOfMissions) {
        this.agentsListConsumer = agentsListConsumer;
        this.agentName = agentName;
        this.amountOfMissions = amountOfMissions;
    }

    @Override
    public void run() {

        String finalUrl = HttpUrl
                .parse(Constants.START_POLL_MISSIONS_PATH)
                .newBuilder()
                .addQueryParameter("username", agentName)
                .addQueryParameter("amountOfMissions", String.valueOf(amountOfMissions))
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonDto = response.body().string();
                DecryptTaskDTO[] dtos = GSON_INSTANCE.fromJson(jsonDto, DecryptTaskDTO[].class);
//                putMissionsInThreadPool(dtos);
                agentsListConsumer.accept(dtos);
            }
        });
    }
}
*/
