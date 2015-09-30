package ifsb.arithmetico;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ifsb.arithmetico.R;
import ifsb.arithmetico.core.AdditionQuestion;
import ifsb.arithmetico.core.Question;


public class AdditionPanelFragment extends QuestionPanelFragment  {
    private GridLayout carryRow;
    private GridLayout numberTable;
    private GridLayout answerRow;
    private TextView tv_addop;
    private int col;
    private AdditionQuestion q;
    private OnQuestionPanelListener mListener;

    public static AdditionPanelFragment newInstance() {
        AdditionPanelFragment fragment = new AdditionPanelFragment();
        return fragment;
    }

    public AdditionPanelFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return doit(inflater, container);
//        return view;
    }

    public View doit(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_addition_panel, container, false);
        carryRow = (GridLayout) view.findViewById(R.id.carryRow);
        numberTable = (GridLayout) view.findViewById(R.id.numberTable);
        answerRow = (GridLayout) view.findViewById(R.id.answerRow);
        tv_addop = (TextView) view.findViewById(R.id.tv_addop);

        if (UserSettings.leftKeypadEnabled()) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) carryRow.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            carryRow.setLayoutParams(params);

            params = (RelativeLayout.LayoutParams) numberTable.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            numberTable.setLayoutParams(params);

            params = (RelativeLayout.LayoutParams) answerRow.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            answerRow.setLayoutParams(params);
        }
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnQuestionPanelListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void postQuestion(Question question) {
        carryRow.removeAllViews();
        answerRow.removeAllViews();
        numberTable.removeAllViews();

        q = (AdditionQuestion) question;
        col = 0;

        int stdTextSize = UserSettings.getQuestionTextSize();
        Paint paint = new Paint();
        paint.setTextSize(stdTextSize);
        float stdMinWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, paint.measureText("0"), getResources().getDisplayMetrics());
        paint.setTextSize(stdTextSize/2);
        float carryMinWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, paint.measureText("000"), getResources().getDisplayMetrics());
        float tvMinWidth = (stdMinWidth > carryMinWidth) ? stdMinWidth : carryMinWidth;

        int numCols = q.getAnswerLength();
        carryRow.setRowCount(1);
        carryRow.setColumnCount(numCols);
        TextView tv = null;
        int i=0;
        for (; i < numCols; i++) {
            tv = addCell(carryRow, 0, i, (int) tvMinWidth, stdTextSize / 2);
            tv.setTextColor(getResources().getColor(R.color.questionpanel_normal_text));
            tv.setBackgroundColor(getResources().getColor(R.color.questionpanel_normal_bg));
        }
        tv.setText("0");

        answerRow.setRowCount(1);
        answerRow.setColumnCount(numCols);
        for (i=0; i < numCols; i++) {
            tv = addCell(answerRow, 0, i, (int) tvMinWidth, stdTextSize);
            tv.setTextColor(getResources().getColor(R.color.questionpanel_answer_text));
            tv.setBackgroundColor(getResources().getColor(R.color.questionpanel_answer_bg));
        }

        numberTable.setRowCount(q.getNumberCount());
        numberTable.setColumnCount(numCols+1);

        for (int k=0; k < q.getNumberCount(); k++) {
            ArrayList<Integer> num = q.getDigitsNum(k);
            for (i=0; i < numCols; i++) {
                tv = addCell(numberTable, k, numCols-i-1,(int) tvMinWidth, stdTextSize);
                tv.setTextColor(getResources().getColor(R.color.questionpanel_normal_text));
                tv.setBackgroundColor(getResources().getColor(R.color.questionpanel_normal_bg));
                if (i < num.size())
                    tv.setText("" + num.get(i));
            }
        }
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tv_addop.getLayoutParams();
        params.addRule((UserSettings.leftKeypadEnabled()) ? RelativeLayout.LEFT_OF : RelativeLayout.RIGHT_OF, R.id.numberTable);
        params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.numberTable);
        tv_addop.setTextSize(UserSettings.getQuestionTextSize());
        tv_addop.setLayoutParams(params);
        highlightColumn(0);

        Hints.show(getActivity(), Hints.NUMERICPAD, Hints.SKIPQUESTION, Hints.BACKHOME1);
    }

    public void highlightColumn(int col) {
        TextView tv;
        for (int j=0; j < q.getAnswerLength(); j++) {
            (tv = getCarryTextView(j)).setTextColor((j == col) ? getResources().getColor(R.color.questionpanel_highlight_text) : getResources().getColor(R.color.questionpanel_normal_text));
            tv.setBackgroundColor((j == col) ? getResources().getColor(R.color.questionpanel_highlight_bg) : getResources().getColor(R.color.questionpanel_normal_bg));
        }
        for (int k=0; k < q.getNumberCount(); k++)
            for (int j=0; j < q.getAnswerLength(); j++) {
                (tv = getNumberTextView(k, j)).setTextColor((j == col) ? getResources().getColor(R.color.questionpanel_highlight_text) : getResources().getColor(R.color.questionpanel_normal_text));
                tv.setBackgroundColor((j == col) ? getResources().getColor(R.color.questionpanel_highlight_bg) : getResources().getColor(R.color.questionpanel_normal_bg));
            }
    }

    private TextView getNumberTextView(int row, int col) {
        return (TextView) numberTable.getChildAt(row*q.getAnswerLength()+col);
    }
    private TextView getCarryTextView(int col) {
        return (TextView) carryRow.getChildAt(carryRow.getColumnCount()-col-1);
    }
    private TextView getAnswerTextView(int col) {
        return (TextView) answerRow.getChildAt(answerRow.getColumnCount()-col-1);
    }

    @Override
    public void endSession() {

    }

    @Override
    public void correctResponse() {
        getAnswerTextView(col).setText(""+q.getDigitAns(col));

        col++;
        if (!q.done(col))
            getCarryTextView(col).setText(""+q.getCarry(col));
        highlightColumn(col);
    }

    @Override
    public void wrongResponse() {
      //  col = -1;
    }

    @Override
    public boolean checkAnswer(int answer) {
        return q.checkAnswer(col, answer);
    }

    @Override
    public boolean questionDone() {
        return q.done(col);
    }

}
