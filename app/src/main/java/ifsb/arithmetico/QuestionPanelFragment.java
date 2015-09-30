package ifsb.arithmetico;

import android.app.Fragment;
import android.graphics.Color;
import android.widget.GridLayout;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by sufian on 26/8/15.
 */
public abstract class QuestionPanelFragment extends Fragment implements QuestionActivityListener {

    @Override
    public void updateStatus() {}

    public static QuestionPanelFragment create(String fragmentClassName) {
        try {
            return (QuestionPanelFragment) Class.forName(fragmentClassName).getMethod("newInstance").invoke(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface OnQuestionPanelListener {
        public void onQuestionPanelInteraction();
    }
    @Override
    public void updateTimerStatus(int secondsRemaining) {}

    @Override
    public void wrongResponse() {
    }

    protected TextView addCell(GridLayout gl, int row, int col, int minWidth, int textSize) {
        GridLayout.Spec rowSpec = GridLayout.spec(row);
        GridLayout.Spec colSpec = GridLayout.spec(col);
        TextView tv = new TextView(getActivity());
        tv.setMinimumWidth(minWidth);
        tv.setTextSize(textSize);
        gl.addView(tv, new GridLayout.LayoutParams(rowSpec, colSpec));
        return tv;
    }
}
