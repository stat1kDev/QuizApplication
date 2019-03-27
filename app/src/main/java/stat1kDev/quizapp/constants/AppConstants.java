package stat1kDev.quizapp.constants;

public class AppConstants {

    public static final String APP_PREF_NAME = "quiz_app_pref";
    public static final String KEY_SOUND = "sound";

    public static final int BUNDLE_KEY_ZERO_INDEX = 0;
    public static final int BUNDLE_KEY_FIRST_INDEX = 1;
    public static final int BUNDLE_KEY_SECOND_INDEX = 2;
    public static final int BUNDLE_KEY_MAX_LIFE = 5;
    public static final int MULTIPLIER_GRADE = 10;

    public static final String COLOR_WHITE = "rectangle_white_normal";
    public static final String COLOR_GREEN = "rectangle_quiz_green_normal";
    public static final String COLOR_RED = "rectangle_quiz_red_normal";
    public static final String DIRECTORY = "drawable/" + "result_";

    public static final String EMPTY_STRING = "";
    public static final String BUNDLE_KEY_TITLE = "title";
    public static final String BUNDLE_KEY_MESSAGE = "message";
    public static final String BUNDLE_KEY_URL = "url";
    public static final long SITE_CASHE_SIZE = 10 * 1024 * 1024;

    // inAppBilling constants
    public static final String PRODUCT_ID_BOUGHT = "item_1_bought";
    public static final String PRODUCT_ID_SUBSCRIBE = "item_1_subscribe";

    // notification constants
    public static final String PREF_NOTIFICATION = "pref_notification";
    public static final String PREF_FONT_SIZE = "pref_font_size";
    public static final String NEW_NOTI = "new_notification";
    public static final String BUNDLE_KEY_ITEM = "item";
    public static final String BUNDLE_KEY_INDEX = "index";
    public static final String BUNDLE_KEY_DELETE_ALL_NOT = "delete_all_not";

    // question constants
    public static final String BUNDLE_KEY_SCORE = "score";
    public static final String BUNDLE_KEY_WRONG_ANS = "wrong_ans";
    public static final String BUNDLE_KEY_SKIP = "skip";
    public static final String BUNDLE_KEY_YES = "yes";
    public static final String BUNDLE_KEY_NO = "no";
    public static final String BUNDLE_KEY_VIEW_ID = "view_id_tex";
    public static final String BUNDLE_KEY_DIALOG_FRAGMENT = "dialog_fragment";
    public static final String BUNDLE_KEY_EXIT_OPTION = "exit";
    public static final String BUNDLE_KEY_CLOSE_OPTION = "close";
    public static final String BUNDLE_KEY_SKIP_OPTION = "skip";
    public static final String BUNDLE_KEY_REWARD_OPTION = "reward";

    // category file
    public static final String CONTENT_FILE = "json/categories/quiz_category.json";
    public static final String JSON_KEY_ITEMS = "items";
    public static final String JSON_KEY_CATEGORY_ID = "question_category";
    public static final String JSON_KEY_CATEGORY_NAME = "category_name";

    // question file
    public static final String QUESTION_FILE = "json/quiz/question_set.json";
    public static final String JSON_KEY_QUESTIONNAIRY = "questionnaires";
    public static final String JSON_KEY_QUESTION = "question";
    public static final String JSON_KEY_CORRECT_ANS = "correct_answer";
    public static final String JSON_KEY_ANSWERS = "answers";
    public static final String QUESTIONS_IN_TEST = "questions_count";

    // pie chart constants
    public static final float TRANSPARENT_CIRCLE_RADIUS = 65f;
    public static final int ANIMATION_VALUE = 2000;
}
