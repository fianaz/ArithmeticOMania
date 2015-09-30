package ifsb.arithmetico;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

/**
 * Created by sufian on 30/9/15.
 */
public class BugReporter {

    public static void generate(Activity activity) {
        new BugReporter().composeEmail(activity);
    }
    private void composeEmail(Activity activity) {
        Resources resources = activity.getResources();
        String[] addresses = new String[] { resources.getString(R.string.bugreport_email) };
        String subject = resources.getString(R.string.bugreport_subject);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("message/rfc822");
        PackageInfo pInfo = null;
        try {
            pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String str = "mailto:";
        if (addresses.length > 0) {
            int i=0;
            for (; i < addresses.length - 1; i++)
                str = str + addresses[i] + ",";
            str = str+addresses[i];
        }
        str = str+"?subject="+subject;
        str = str+"&body="+composeBody(resources, pInfo);
        intent.setData(Uri.parse(str));
        try {
            activity.startActivity(Intent.createChooser(intent, "Send email..."));
        }
        catch (ActivityNotFoundException exception) {
            Toast.makeText(activity, "No email clients found", Toast.LENGTH_SHORT);
        }
    }
    private String composeBody(Resources resources, PackageInfo pInfo) {
        String appVersion = (pInfo != null) ? pInfo.versionName : "?";
        return resources.getString(R.string.bugreport_phonemodel) + " " + Build.MODEL + "\n"
                + resources.getString(R.string.bugreport_osversion) + " " + Build.VERSION.SDK_INT + "\n"
                + resources.getString(R.string.bugreport_appversion) + " " + appVersion + "\n"
//                + resources.getString(R.string.bugreport_appbuild) + " " + "" + "\n"
                + "\n"
                + resources.getString(R.string.bugreport_bugdescription) + "\n";
    }
}
