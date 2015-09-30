package ifsb.arithmetico;

import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by sufian on 27/8/15.
 */
public class UserSettings {
    public static final int SMALL_QUESTIONTEXTSIZE = 24;
    public static final int NORMAL_QUESTIONTEXTSIZE = 30;
    public static final int LARGE_QUESTIONTEXTSIZE = 36;
    public static final int XLARGE_QUESTIONTEXTSIZE = 42;
    public static final int DEFAULT_QUESTIONTEXTSIZE = NORMAL_QUESTIONTEXTSIZE;
    public static final long DEFAULT_COUNTDOWNTIME = 60*1000L;
    public static final int DEFAULT_AUTONEXTTIME = 3;
    public static final int DEFAULT_NUMQUESTIONSPERSESSION = 5;
    public static UserSettings userSettings = null;
    public static AppCompatActivity activity;
    private SharedPreferences sharedPreferences;
    public static void init(AppCompatActivity activity) {
        if (userSettings == null) {
            UserSettings.activity = activity;
            userSettings = new UserSettings();
            Set<String> set = getHintSet();
        }
    }
    public static UserSettings getUserSettings() {
        return userSettings;
    }
    public static int getQuestionTextSize() {
/*        String str = userSettings.sharedPreferences.getString("qTextSize", null);
        if (str != null)
            return Integer.parseInt(str);
        return DEFAULT_QUESTIONTEXTSIZE;*/
        return getInt("qTextSize");
    }
    public static void setQuestionTextSize(int questionTextSize) {
        put("qTextSize", UserSettings.DEFAULT_QUESTIONTEXTSIZE);
    }
    public static Set<String> getHintSet() {
        return userSettings.sharedPreferences.getStringSet("hints", null);
    }
    public static void putHintSet(Set<String> set) {
        userSettings.sharedPreferences.edit().putStringSet("hints", set).commit();
    }
    public static int getHint(int id) {
        Set<String> set = getHintSet();
        if (set != null  &&  set.contains(""+id))
            return id;
        return -1;
    }
    public static void removeHint(int id) {
        Set<String> set = getHintSet();
        if (set != null  &&  set.contains(""+id)) {
            set.remove("" + id);
            putHintSet(set);
        }
    }
    public static Uri getBgMusic() {
        String str = userSettings.sharedPreferences.getString("bgaudiofile", null);
        if (str == null || str.equals(""))
            return null;
        return Uri.parse(str);
    }
    public static void setBgMusic(String uri) {
        SharedPreferences.Editor editor = userSettings.sharedPreferences.edit();
        editor.putString("bgaudiofile", uri);
        editor.commit();
    }
    public static void setBgMusic(Uri uri) {
        setBgMusic(uri.toString());
    }
    public static boolean bgMusicEnabled() {
        //return Boolean.valueOf(userSettings.sharedPreferences.getString("autonext", Boolean.toString(false)));
        return getBoolean("bgaudio");
    }
    public static boolean countdownEnabled() {
        //return Boolean.valueOf(userSettings.sharedPreferences.getString("autonext", Boolean.toString(false)));
        return getBoolean("countdown");
    }
    public static void setCountDown(boolean status) {
        put("countdown", status);
    }
    public static long getCountdownTime() {
        //return 1000*Long.parseLong(userSettings.sharedPreferences.getString("countdown", ""+DEFAULT_COUNTDOWNTIME));
        return 1000*getLong("countdowntime");
    }
    public static void setCountdownTime(long countdownTime) {
        put("countdowntime", countdownTime);
    }
    public static boolean autoNextEnabled() {
        //return Boolean.valueOf(userSettings.sharedPreferences.getString("autonext", Boolean.toString(false)));
        return getBoolean("autonext");
    }
    public static int getAutoNextTime() {
//        return Integer.parseInt(userSettings.sharedPreferences.getString("autonexttime", "" + DEFAULT_AUTONEXTTIME));
        return getInt("autonexttime");
    }
    public static void setAutoNextTime(int secs) {
        put("autonexttime", secs);
    }
    public static boolean vibrateEnabled() {
        return getBoolean("vibrate");
    }
    public static void setVibrate(boolean status) {
        put("vibrate", status);
    }
    public static boolean leftKeypadEnabled() {
        return getBoolean("leftkeypad");
    }
    public static void setLSKeypad(boolean status) {
        put("leftkeypad", status);
    }
    public static void update() {
        if (userSettings != null)
            userSettings.updateSharedPreferences();
    }
    public static int getNumQuestions() {
        return getInt("numquestions");
    }
    public static void setNumQuestions(int numQuestions) {
        put("numquestions", numQuestions);
    }
    private UserSettings() {
        updateSharedPreferences();
    }
    private void updateSharedPreferences() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
    }
    private static int getInt(String key) {
        return Integer.parseInt(userSettings.sharedPreferences.getString(key, "0"));
//        return Integer.parseInt(userSettings.sharedPreferences.getString(key, ""+defaultValue));
    }
    private static void put(String key, int value) {
        userSettings.sharedPreferences.edit().putString(key, ""+value).commit();
    }
    private static void put(String key, String value) {
        userSettings.sharedPreferences.edit().putString(key, value).commit();
    }
    private static void put(String key, boolean value) {
        userSettings.sharedPreferences.edit().putBoolean(key, value).commit();
    }
    private static void put(String key, long value) {
        userSettings.sharedPreferences.edit().putString(key, "" + value).commit();
    }
    private static long getLong(String key) {
        return Long.parseLong(userSettings.sharedPreferences.getString(key, "0"));
//        return Long.parseLong(userSettings.sharedPreferences.getString(key, "" + defaultValue));
    }
    private static boolean getBoolean(String key) {
        return userSettings.sharedPreferences.getBoolean(key, false);
//        return userSettings.sharedPreferences.getBoolean(key, Boolean.valueOf(defaultValue));
    }
    private static void setBoolean(String key, boolean status) {
        SharedPreferences.Editor editor = userSettings.sharedPreferences.edit();
        editor.putBoolean(key, status);
        editor.commit();
    }
    public static void enableBgMusic(boolean status) {
        setBoolean("bgaudio", status);
    }
    public static void enableAutonext(boolean status) {
        setBoolean("autonext", status);
    }

    public static boolean soundEffectsEnabled() {
        return getBoolean("soundeffects");
    }
    public static void setSoundEffects(boolean status) {
        put("soundeffects", status);
    }

    public static boolean numericKeySoundEnabled() {
        return getBoolean("numerickeysound");
    }
    public static void setNumericKeySound(boolean status) {
        put("numerickeysound", status);
    }

    public static boolean buttonSoundEnabled() {
        return getBoolean("buttonsound");
    }
    public static void setButtonSound(boolean status) {
        put("buttonsound", status);
    }

    public static boolean autonextSoundEnabled() {
        return getBoolean("autonextsound");
    }
    public static void setAutonextSound(boolean status) {
        put("autonextsound", status);
    }

    public static boolean endSessionSoundEnabled() {
        return getBoolean("endsession");
    }
    public static void setEndSessionSound(boolean status) {
        put("endsession", status);
    }

}
