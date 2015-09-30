package ifsb.arithmetico;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

/**
 * Created by sufian on 4/9/15.
 */
public class BackgroundMusicPlayer extends MediaPlayer {
    private static BackgroundMusicPlayer bgMusicPlayer = null;
    public static void update(Context context) {
        if (UserSettings.bgMusicEnabled())
            createPlayer(context, UserSettings.getBgMusic());
        else
            releasePlayer();
    }
    public static void createPlayer(Context context, Uri uri) {
        if (bgMusicPlayer == null)
            if (uri != null)
                bgMusicPlayer = new BackgroundMusicPlayer(context, uri);
    }
    public static void pausePlayer() {
        if (bgMusicPlayer != null && bgMusicPlayer.isPlaying()) {
            bgMusicPlayer.pause();
        }
    }
    public static void resumePlayer() {
        if (bgMusicPlayer != null && !bgMusicPlayer.isPlaying()) {
            bgMusicPlayer.start();
        }
    }
    public static void stopPlayer() {
        if (bgMusicPlayer != null && bgMusicPlayer.isPlaying()) {
            bgMusicPlayer.stop();
        }
    }
    public static void releasePlayer() {
        if (bgMusicPlayer != null) {
            if (bgMusicPlayer.isPlaying())
                bgMusicPlayer.stop();
            bgMusicPlayer.release();
            bgMusicPlayer = null;
        }
    }
    public static BackgroundMusicPlayer getPlayer() {
        return bgMusicPlayer;
    }
    private BackgroundMusicPlayer(Context context, Uri uri) {
        try {
            setDataSource(context, uri);
            prepareAsync();
            setLooping(true);
            setOnPreparedListener(new OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    mp.pause();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
