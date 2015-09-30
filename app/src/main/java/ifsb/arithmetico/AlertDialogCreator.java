package ifsb.arithmetico;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;

/**
 * Created by sufian on 16/9/15.
 */
public class AlertDialogCreator {
    public static void show(Context context, String title, String msg,
                                       String posLabel,
                                       DialogInterface.OnClickListener posListener,
                                       String negLabel,
                                       DialogInterface.OnClickListener negListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(msg)
//                .setCancelable(cancelable)
                .setPositiveButton(posLabel, posListener);
        if (negLabel != null)
            builder.setNegativeButton(negLabel, negListener);
        builder.create().show();
    }
    public static void show(Context context, String title, String msg, String posLabel) {
        show(context, title, msg,
                posLabel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                       dialog.cancel();
                    }
                }, null, null);
    }
}
