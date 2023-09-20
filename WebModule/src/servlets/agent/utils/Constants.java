package servlets.agent.utils;

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
    public static final String GET_CONTEST_PROGRESS_PATH = FULL_SERVER_PATH + "/get-progress";
    public static final String GET_ALLIES_LIST_PATH = FULL_SERVER_PATH + "/get-allies";
    public static final String CONFIRMED_LOGOUT_PATH = FULL_SERVER_PATH + "/confirmed-logout";
    public static final String CONFIRMED_CLEAR_PATH = FULL_SERVER_PATH + "/confirm-clear";
    public final static String CONTEST_DATA_PAGE_FXML_RESOURCE_LOCATION = "/allie/client/resources/contestData.fxml";

    public final static int REFRESH_RATE = 2000;

    //UBoat
    public final static String JHON_DOE = "<Anonymous>";
    public final static String GET_CANDIDATES_PATH = FULL_SERVER_PATH + "/get-candidates";
    public final static String IS_THERE_A_WINNER_PATH = FULL_SERVER_PATH + "/is-contest-end";
    public final static String LOGOUT_UBOAT_PATH = FULL_SERVER_PATH + "/logout";

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
    public final static String START_POLL_MISSIONS_PATH = FULL_SERVER_PATH + "/agent-polling-missions";
    public final static String POTENTIALLY_DECRYPT_DATA_PAGE_FXML_RESOURCE_LOCATION = "/agent/client/resources/potentially-decrypt.fxml";
    public final static String AGENT_POST_CANDIDATES_PATH = FULL_SERVER_PATH + "/agent-post-candidates";
    public final static String GET_CONTEST_DETAILS_FOR_AGENT_PATH = FULL_SERVER_PATH + "/details-for-agent";
    public final static String AGENT_POST_DETAILS_PATH = FULL_SERVER_PATH + "/agent-post-details";
    public static final String DID_FINISH_BUTTON_CLICKED = FULL_SERVER_PATH + "/did-finishBtn-clicked";
    public final static String DID_UBOAT_LOGGED_OUT_PATH = FULL_SERVER_PATH + "/did-uboat-logged-out";
    public static final String DID_CLEAR_BTN_CLICKED = FULL_SERVER_PATH + "/did-clearBtn-clicked";

    //GSON instance
    public final static Gson GSON_INSTANCE = new Gson();

}
