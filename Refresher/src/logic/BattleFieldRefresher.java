package logic;

//import chat.client.util.Constants;
//import chat.client.util.http.HttpClientUtil;
import client.http.HttpClientUtil;
import com.sun.istack.internal.NotNull;
import javafx.beans.property.BooleanProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
//import org.jetbrains.annotations.NotNull;
import utils.Constants;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

//import static chat.client.util.Constants.GSON_INSTANCE;
import static utils.Constants.GSON_INSTANCE;

public class BattleFieldRefresher extends TimerTask {

//    private final Consumer<String> httpRequestLoggerConsumer;
    private final Consumer<List<String>> usersListConsumer;
    private int requestNumber;
    private final BooleanProperty shouldUpdate;


    public BattleFieldRefresher(BooleanProperty shouldUpdate/*, Consumer<String> httpRequestLoggerConsumer*/, Consumer<List<String>> usersListConsumer) {
        this.shouldUpdate = shouldUpdate;
//        this.httpRequestLoggerConsumer = httpRequestLoggerConsumer;
        this.usersListConsumer = usersListConsumer;
        requestNumber = 0;
    }

    @Override
    public void run() {

        if (!shouldUpdate.get()) {
            return;
        }

        final int finalRequestNumber = ++requestNumber;
//        httpRequestLoggerConsumer.accept("About to invoke: " + Constants.BATTLE_FIELD_LIST + " | Users Request # " + finalRequestNumber);
        HttpClientUtil.runAsync(Constants.BATTLE_FIELD_LIST, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                httpRequestLoggerConsumer.accept("Users Request # " + finalRequestNumber + " | Ended with failure...");
                System.out.println("bad");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonArrayOfUsersNames = response.body().string();
//                httpRequestLoggerConsumer.accept("Users Request # " + finalRequestNumber + " | Response: " + jsonArrayOfUsersNames);
                String[] usersNames = GSON_INSTANCE.fromJson(jsonArrayOfUsersNames, String[].class);
                usersListConsumer.accept(Arrays.asList(usersNames));
            }
        });
    }
}
