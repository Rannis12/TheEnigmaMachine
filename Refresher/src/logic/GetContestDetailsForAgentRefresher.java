package logic;

import client.http.HttpClientUtil;
import com.sun.istack.internal.NotNull;
import dtos.web.ContestDetailsForAgentDTO;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import servlets.agent.utils.Constants;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static servlets.agent.utils.Constants.GSON_INSTANCE;

public class GetContestDetailsForAgentRefresher extends TimerTask {

    private final Consumer<ContestDetailsForAgentDTO> consumer;
    private final String username;


    public GetContestDetailsForAgentRefresher(Consumer<ContestDetailsForAgentDTO> consumer, String username) {
        this.consumer = consumer;
        this.username = username;

    }

    @Override
    public void run() {

        String finalUrl = HttpUrl
                .parse(Constants.GET_CONTEST_DETAILS_FOR_AGENT_PATH)
                .newBuilder()
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
                String jsonArrayOfDto = response.body().string();
                jsonArrayOfDto = jsonArrayOfDto.replaceAll("(\\r|\\n|\\t)", "");
                ContestDetailsForAgentDTO dto = GSON_INSTANCE.fromJson(jsonArrayOfDto, ContestDetailsForAgentDTO.class);
                consumer.accept(dto);
            }
        });
    }
}
