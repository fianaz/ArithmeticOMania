package ifsb.arithmetico;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by sufian on 23/9/15.
 */
public class ScreenReceiver extends BroadcastReceiver {
    public static boolean wasScreenOn = true;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                // do things here
            wasScreenOn = false;
        }
        else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                // do things here
            wasScreenOn = true;
        }
     }
}
