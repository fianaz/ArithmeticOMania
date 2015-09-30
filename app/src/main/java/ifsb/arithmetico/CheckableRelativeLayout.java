package ifsb.arithmetico;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sufian on 24/9/15.
 */
public class CheckableRelativeLayout extends RelativeLayout implements Checkable {
    private boolean isChecked;
    private List<Checkable> checkables;
    public CheckableRelativeLayout(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        initialize(attributeSet);
    }

    public CheckableRelativeLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initialize(attributeSet);
    }

    public CheckableRelativeLayout(Context context, int checkableID) {
        super(context);
        initialize(null);
    }

    private void initialize(AttributeSet attributeSet) {
        isChecked = false;
        checkables = new ArrayList<Checkable>();
    }

    @Override
    public void setChecked(boolean b) {
        isChecked = b;
        for (Checkable c : checkables)
            c.setChecked(isChecked);
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        isChecked = !isChecked;
        for (Checkable c : checkables)
            c.toggle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        final int childCount = getChildCount();
        for (int i=0; i < childCount; i++)
            findCheckableChildren(getChildAt(i));
    }

    private void findCheckableChildren(View v) {
        if (v instanceof Checkable)
            checkables.add((Checkable) v);
        if (v instanceof ViewGroup) {
            final ViewGroup vg = ((ViewGroup) v);
            final int childCount = vg.getChildCount();
            for (int i=0; i < childCount; i++)
                findCheckableChildren(vg.getChildAt(i));
        }
    }
}
