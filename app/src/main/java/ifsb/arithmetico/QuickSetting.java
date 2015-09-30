package ifsb.arithmetico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by sufian on 21/9/15.
 */
public class QuickSetting {
    private String description;
    private boolean soundeffects;
    private boolean autonext;
    private int autonext_period;
    private int questionTextSize;
    private boolean bgaudio;
    private String bgaudioURI;
    private boolean vibrate;
    private boolean lsKeypad;
    private int numQuestions;
    private boolean countdown;
    private int countdownTime;
    private static ArrayList<QuickSetting> list;
    private static ArrayList<String> names;
    private QuickSetting(String description, int questionTextSize, boolean soundeffects, boolean bgaudio, String bgaudioURI, boolean autonext, int autonext_period, boolean vibrate, boolean lsKeypad, int numQuestions, boolean countdown, int countdownTime) {
        this.description = description;
        this.soundeffects = soundeffects;
        this.autonext = autonext;
        this.autonext_period = autonext_period;
        this.questionTextSize = questionTextSize;
        this.bgaudio = bgaudio;
        this.bgaudioURI = bgaudioURI;
        this.vibrate = vibrate;
        this.lsKeypad = lsKeypad;
        this.numQuestions = numQuestions;
        this.countdown = countdown;
        this.countdownTime = countdownTime;
    }
    public static void init() {
        list = new ArrayList<QuickSetting>();
        names = new ArrayList<String>();
        add("Default", "App default settings", UserSettings.DEFAULT_QUESTIONTEXTSIZE, true, false, "", false, 0, false, false, UserSettings.DEFAULT_NUMQUESTIONSPERSESSION, false, 0);
        add("Greenie", "Settings for young learners", UserSettings.XLARGE_QUESTIONTEXTSIZE, true, true, "", false, 0, true, false, 3, false, 0);
        add("Basic", "Settings for basic-level users", UserSettings.LARGE_QUESTIONTEXTSIZE, true, true, "", true, 3, true, false, 5, true, 18);
        add("Intermediate", "Settings for intermediate-level users", UserSettings.NORMAL_QUESTIONTEXTSIZE, true, true, "", true, 1, true, false, 10, true, 16);
        add("Advanced", "Settings for advanced-level users", UserSettings.SMALL_QUESTIONTEXTSIZE, true, false, "", false, 0, false, false, 15, true, 14);
        add("Expert", "Settings for expert users", UserSettings.SMALL_QUESTIONTEXTSIZE, false, false, "", false, 0, false, false, 20, true, 12);
        add("Fun", "Fun settings", UserSettings.LARGE_QUESTIONTEXTSIZE, true, true, "", true, 3, true, false, 5, true, 16);
    }
    public static String getDescription(String key) {
        QuickSetting qs = getSetting(key);
        return (qs == null) ? null : qs.description;
    }
    public static QuickSetting getSetting(String key) {
        int idx = names.indexOf(key);
        return (idx < 0) ? null : list.get(idx);
    }
    public static ArrayList<String> getNames() {
        return names;
    }
    public static ArrayList<QuickSetting> getValueSet() {
        return list;
    }
    public static void add(String name, String description, int questionTextSize, boolean soundeffects, boolean bgaudio, String bgaudioURI, boolean autonext, int autonext_period, boolean vibrate, boolean lsKeypad, int numQuestions, boolean countdown, int countdownTime) {
        names.add(name);
        list.add(new QuickSetting(description, questionTextSize, soundeffects, bgaudio, bgaudioURI, autonext, autonext_period, vibrate, lsKeypad, numQuestions, countdown, countdownTime));
    }
    public static void activate(String name) {
        QuickSetting qs = getSetting(name);
        if (qs != null) {
            UserSettings.setQuestionTextSize(qs.questionTextSize);
            UserSettings.enableAutonext(qs.autonext);
            UserSettings.setAutoNextTime(qs.autonext_period);
            UserSettings.enableBgMusic(qs.bgaudio);
            UserSettings.setBgMusic(qs.bgaudioURI);
            UserSettings.setVibrate(qs.vibrate);
            UserSettings.setLSKeypad(qs.lsKeypad);
            UserSettings.setNumQuestions(qs.numQuestions);
            UserSettings.setCountDown(qs.countdown);
            UserSettings.setCountdownTime(qs.countdownTime);
        }
    }
}
