package ifsb.arithmetico;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;

import java.util.HashMap;

import ifsb.arithmetico.R;

/**
 * Created by sufian on 31/8/15.
 */
public class SoundPlayer {
    private static int VIBRATE_LENGTH = 100;
    private static SoundPool soundPool;
    private static HashMap soundPoolMap;
    private static Vibrator vibrator;
    private static float volLevel = 1.0f;
    private static int repeats = 0;
    public static void init(Context context) {
        if (soundPool == null) {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 100);
            soundPoolMap = new HashMap();
            soundPoolMap.put(R.raw.right, soundPool.load(context, R.raw.right, 1));
            soundPoolMap.put(R.raw.wrong, soundPool.load(context, R.raw.wrong, 1));
            soundPoolMap.put(R.raw.bleep, soundPool.load(context, R.raw.bleep, 1));
            soundPoolMap.put(R.raw.start, soundPool.load(context, R.raw.start, 1));
            soundPoolMap.put(R.raw.endsession, soundPool.load(context, R.raw.endsession, 1));
            soundPoolMap.put(R.raw.keyclick, soundPool.load(context, R.raw.keyclick, 1));
            soundPoolMap.put(R.raw.buttonpress, soundPool.load(context, R.raw.buttonpress, 1));
            soundPoolMap.put(R.raw.perfectsession, soundPool.load(context, R.raw.perfectsession, 1));
        }
        initVibrator(context);
    }
    public static void initVibrator(Context context) {
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public static void setVolume(float level) {
        volLevel = level;
    }
    public static void setRepeats(int repeats) {
        SoundPlayer.repeats = repeats;
    }
    public static void play(Context context, int id) {
        if (soundPool == null || soundPoolMap == null)
            init(context);
        if (UserSettings.soundEffectsEnabled()) {
            boolean ok = true;
            switch (id) {
                case R.raw.endsession: ok = UserSettings.endSessionSoundEnabled(); break;
                case R.raw.perfectsession: ok = UserSettings.endSessionSoundEnabled(); break;
                case R.raw.keyclick: ok = UserSettings.numericKeySoundEnabled(); break;
                case R.raw.buttonpress: ok = UserSettings.buttonSoundEnabled(); break;
                case R.raw.bleep:
                case R.raw.start: ok = UserSettings.autoNextEnabled(); break;
            }
            if (ok)
                soundPool.play(((Integer) soundPoolMap.get(id)).intValue(), volLevel, volLevel, 1, 0, 1f);
        }
    }
    public static void vibrate() {
        if (UserSettings.vibrateEnabled() && vibrator != null)
            vibrator.vibrate(VIBRATE_LENGTH);
    }

}
