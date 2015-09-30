package ifsb.arithmetico;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.StrictMode;
import android.widget.Toast;

/**
 * Created by sufian on 23/9/15.
 */
public class Arithmetico extends Application {
    public static final boolean DEVELOPER_MODE = true;
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        if (DEVELOPER_MODE) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .build());
        }

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
//        filter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(new ScreenReceiver(), filter);
        super.onCreate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
