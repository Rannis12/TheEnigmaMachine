package logic;

import client.http.HttpClientUtil;
import com.sun.istack.internal.NotNull;
import dtos.web.ContestProgressDTO;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import utils.Constants;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static utils.Constants.GSON_INSTANCE;

public class GetContestProgressRefresher extends TimerTask {
    private final Consumer<ContestProgressDTO> consumer;
    private final String allyName;

    public GetContestProgressRefresher(Consumer<ContestProgressDTO> consumer, String allyName) {
        this.consumer = consumer;
        this.allyName = allyName;
    }

    @Override
    public void run() {

        String finalUrl = HttpUrl
                .parse(Constants.GET_CONTEST_PROGRESS_PATH)
                .newBuilder()
                .addQueryParameter("allyName", allyName)
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
                ContestProgressDTO dto = GSON_INSTANCE.fromJson(jsonArrayOfDto, ContestProgressDTO.class);
                consumer.accept(dto);
            }
        });
    }
}
