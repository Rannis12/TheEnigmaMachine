package utils;

import com.google.gson.Gson;

public class Constants {

    //Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/app";

    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;


    public final static String PARENT_NAME_PARAMETER = "parent_name";
    public static final String BATTLE_FIELD_LIST = BASE_URL + "/battlefields-list";

    //GSON instance
    public final static Gson GSON_INSTANCE = new Gson();

}
