package utils;

import com.google.gson.Gson;

public class Constants {

    //Server resources locations
    public final static String BASE_DOMAIN = "localhost";


    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/webApp";

    public final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;


    public final static String PARENT_NAME_PARAMETER = "parent_name";
    public static final String BATTLE_FIELD_LIST = FULL_SERVER_PATH + "/battlefields-list";
    public static final String SOLO_BATTLE_FIELD_UPDATE = FULL_SERVER_PATH + "/current_battleField";

    public static final String JOIN_UBOAT = "/join-uboat";

    //Allies
    public final static String ALLIES_MAIN_PAGE_FXML_RESOURCE_LOCATION = "/allie/client/resources/allies-template.fxml";
    public final static String ALLIES_LOGIN_PAGE_FXML_RESOURCE_LOCATION = "/allie/client/login/alliesLogin.fxml";
    public final static String ALLIES_ROOM_PAGE_FXML_RESOURCE_LOCATION = "/allie/client/resources/alliesMainPage.fxml";
    public final static String TEAM_DETAILS_PAGE_FXML_RESOURCE_LOCATION = "/allie/client/resources/teamDetail.fxml";
    public final static String ALLIES_BELONGS_TO_BATTLE_FIELD = FULL_SERVER_PATH + "/allies-belongs-to-battleField";
    public final static String ALLY_POST_HIS_STATUS_PATH = FULL_SERVER_PATH + "/ready-status";
    public static final String START_CREATE_MISSIONS_PATH = FULL_SERVER_PATH + "/create-missions";
    public final static String CONTEST_DATA_PAGE_FXML_RESOURCE_LOCATION = "/allie/client/resources/contestData.fxml";

    public final static int REFRESH_RATE = 2000;

    //UBoat
    public final static String JHON_DOE = "<Anonymous>";

    // fxml locations
    public final static String UBOAT_MAIN_PAGE_FXML_RESOURCE_LOCATION = "/uboat/client/resources/uboat-template.fxml";
    public final static String UBOAT_LOGIN_PAGE_FXML_RESOURCE_LOCATION = "/uboat/client/login/login.fxml";
    public final static String UBOAT_ROOM_PAGE_FXML_RESOURCE_LOCATION = "/uboat/client/resources/MainPage.fxml";

    public final static String RANDOM_CONFIGURATION = FULL_SERVER_PATH + "/random-conf";
    public final static String DECODE_STRING_PAGE = FULL_SERVER_PATH + "/decode-string";
    public final static String SHOULD_START_CONTEST_PATH = FULL_SERVER_PATH + "/wait-for-uboat-confirmation";
    public final static String UBOAT_POST_HIS_STATUS_PATH = FULL_SERVER_PATH + "/uboat-updates-status";


    //Agent
    public final static String AGENT_LOGIN_PAGE_FXML_RESOURCE_LOCATION = "/agent/client/login/agent-login.fxml";
    public final static String AGENT_ROOM_PAGE_FXML_RESOURCE_LOCATION = "/agent/client/resources/agentMainPage.fxml";
    public final static String AGENT_MAIN_PAGE_FXML_RESOURCE_LOCATION = "/agent/client/resources/agent-template.fxml";
    public final static String POST_AGENT_SERVLET = FULL_SERVER_PATH + "/post-agent";
    public final static String AGENT_LIST = FULL_SERVER_PATH + "/agent-list";
    public static final String START_POLL_MISSIONS_PATH = FULL_SERVER_PATH + "/agent-polling-missions";

    //GSON instance
    public final static Gson GSON_INSTANCE = new Gson();

}
