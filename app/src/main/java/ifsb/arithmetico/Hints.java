package ifsb.arithmetico;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.util.Log;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import ifsb.arithmetico.AlertDialogCreator;
import ifsb.arithmetico.R;

/**
 * Created by sufian on 20/9/15.
 */
public class Hints {
    public static final int NUMERICPAD = 0;
    public static final int VIEWREPORT = 1;
    public static final int NEXTQUESTION = 2;
    public static final int BORROW = 3;
    public static final int SKIPQUESTION = 4;
    public static final int BACKHOME1 = 5;
    public static final int BACKHOME2 = 6;
    private static HashMap<Integer,Integer> hints;
    private String message;
    public static void init(Context context) {
        Resources resources = context.getResources();
        hints = new HashMap<Integer,Integer>();
        hints.put(NUMERICPAD, R.string.hint_numericpad);
        hints.put(VIEWREPORT, R.string.hint_viewreport);
        hints.put(NEXTQUESTION, R.string.hint_nextquestion);
        hints.put(BORROW, R.string.hint_borrow);
        hints.put(SKIPQUESTION, R.string.hint_skipquestion);
        hints.put(BACKHOME1, R.string.hint_backhome1);
        hints.put(BACKHOME2, R.string.hint_backhome2);

        if (UserSettings.getHintSet() == null) {
            HashSet<String> set = new HashSet<String>();
            for (int i=0; i < hints.size(); i++)
                set.add(""+i);
            UserSettings.putHintSet(set);
        }
    }
    public static String getHint(Context context, int id) {
        return (UserSettings.getHint(id) == -1) ? null : context.getResources().getString(hints.get(id));
    }
    public static void show(Context context, int... idx) {
        String hint = "";
        for (int i=0; i < idx.length; i++) {
            String str = getHint(context, idx[i]);
            if (str != null)
                hint = hint+str+"\n\n";
        }
        if (!hint.isEmpty()) {
            final Session session;
            if (context instanceof QuestionActivity) {
                session = ((QuestionActivity) context).getSession();
                session.hintShowing(true);
            }
            else
                session = null;
            AlertDialogCreator.show(context, "Hint", hint.trim(), "Got it!",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (session != null)
                                session.hintShowing(false);
                        }
                    }, null, null);
        }
        for (int i=0; i < idx.length; i++)
            UserSettings.removeHint(idx[i]);
    }
}
