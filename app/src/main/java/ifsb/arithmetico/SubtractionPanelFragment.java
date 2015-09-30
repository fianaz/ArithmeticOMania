package ifsb.arithmetico;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import ifsb.arithmetico.core.Question;
import ifsb.arithmetico.core.SubtractionQuestion;
import ifsb.arithmetico.R;


public class SubtractionPanelFragment extends QuestionPanelFragment {
    private final static int OP_TEXTCOLOR = Color.rgb(255, 255, 255);
    private final static int OP_BGCOLOR = Color.rgb(119, 255, 199);
    class SubtractionNumberTextView extends TextView {
        private Rect rect;
        private Paint paint;
        private boolean borrowed;
        public SubtractionNumberTextView(Context context) {
            super(context);
            rect = new Rect();
            paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(10f);
            borrowed = false;
        }
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (borrowed) {
                canvas.drawLine(getWidth(), 0, 0, getHeight(), paint);
            }
        }
        public void setBorrowed(boolean status) {
            borrowed = status;
        }
    }
    private GridLayout borrowRow;
    private GridLayout numberTable;
    private GridLayout answerRow;
    private TextView tv_subtop;
//    private Button btn_enter;
    private ImageView iv_borrow;
    private EditText et_answer;
//    private TextView tv_borrows, tv_firstNum, tv_secondNum, tv_answer;
    private Session session;
    private SubtractionQuestion q;
    private int col;
    private int borrow;
    private OnQuestionPanelListener mListener;

    public static SubtractionPanelFragment newInstance() {
        SubtractionPanelFragment fragment = new SubtractionPanelFragment();
        return fragment;
    }

    public SubtractionPanelFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subtraction_panel, container, false);

        borrowRow = (GridLayout) view.findViewById(R.id.borrowRow);
        numberTable = (GridLayout) view.findViewById(R.id.numberTable);
        answerRow = (GridLayout) view.findViewById(R.id.answerRow);
        tv_subtop = (TextView) view.findViewById(R.id.tv_subtop);
        iv_borrow = (ImageView) view.findViewById(R.id.iv_borrow);
        iv_borrow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        iv_borrow.setImageDrawable(getResources().getDrawable(R.drawable.borrowdn));
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                    case MotionEvent.ACTION_UP:
                        iv_borrow.setImageDrawable(getResources().getDrawable(R.drawable.borrowup));
                        if (q.getBorrows(borrow) == null)
                            SoundPlayer.play(getActivity(), R.raw.wrong);
                        else
                            updateBorrows(q.getBorrows(borrow));
                        int numCols = borrowRow.getColumnCount();
                        for (int i = 0; i < numCols; i++) {
                            SubtractionNumberTextView sntv = getNumberTextView(0, i);
                            sntv.setBorrowed(!getBorrowTextView(i).getText().toString().equals(""));
                            sntv.invalidate();
                        }
                        break;
                }
                return true;
            }
        });

        if (UserSettings.leftKeypadEnabled()) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) borrowRow.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            borrowRow.setLayoutParams(params);

            params = (RelativeLayout.LayoutParams) numberTable.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            numberTable.setLayoutParams(params);

            params = (RelativeLayout.LayoutParams) answerRow.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            answerRow.setLayoutParams(params);

            params = (RelativeLayout.LayoutParams) iv_borrow.getLayoutParams();
            params.addRule(RelativeLayout.RIGHT_OF, 0);
            params.addRule(RelativeLayout.LEFT_OF, R.id.borrowRow);
            iv_borrow.setLayoutParams(params);
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

    public void postQuestion(Question question) {
        borrowRow.removeAllViews();
        answerRow.removeAllViews();
        numberTable.removeAllViews();


        q = (SubtractionQuestion) question;
        col = 0;
        borrow = 0;

        int stdTextSize = UserSettings.getQuestionTextSize();
        Paint paint = new Paint();
        paint.setTextSize(stdTextSize);
        float stdMinWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, paint.measureText("0"), getResources().getDisplayMetrics());
        paint.setTextSize(stdTextSize/2);
        float borrowMinWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, paint.measureText("000"), getResources().getDisplayMetrics());
        float tvMinWidth = (stdMinWidth > borrowMinWidth) ? stdMinWidth : borrowMinWidth;

        int numCols = q.getAnswerLength();
        borrowRow.setRowCount(1);
        borrowRow.setColumnCount(numCols);
        TextView tv = null;
        int i=0;
        for (; i < numCols; i++) {
            tv = addCell(borrowRow, 0, i, (int) tvMinWidth, stdTextSize / 2);
            tv.setTextColor(getResources().getColor(R.color.questionpanel_normal_text));
            tv.setBackgroundColor(getResources().getColor(R.color.questionpanel_normal_bg));
        }

        answerRow.setRowCount(1);
        answerRow.setColumnCount(numCols);
        for (i=0; i < numCols; i++) {
            tv = addCell(answerRow, 0, i, (int) tvMinWidth, stdTextSize);
            tv.setTextColor(getResources().getColor(R.color.questionpanel_answer_text));
            tv.setBackgroundColor(getResources().getColor(R.color.questionpanel_answer_bg));
        }

        numberTable.setRowCount(q.getNumberCount());
        numberTable.setColumnCount(numCols+1);

        int j;
        SubtractionNumberTextView sntv;
        for (int k=0; k < q.getNumberCount(); k++) {
            ArrayList<Integer> num = q.getDigitsNum(k);
            GridLayout.Spec rowSpec = GridLayout.spec(k);
            for (i=0, j=numCols-1; i < numCols; i++,j--) {
                sntv = new SubtractionNumberTextView(getActivity());
                sntv.setMinimumWidth((int) tvMinWidth);
                sntv.setTextSize(stdTextSize);
                sntv.setTextColor(Color.BLACK);
                if (i < num.size())
                    sntv.setText("" + num.get(i));
                GridLayout.Spec colSpec = GridLayout.spec(j);
                numberTable.addView(sntv, new GridLayout.LayoutParams(rowSpec, colSpec));
            }
        }
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tv_subtop.getLayoutParams();
        params.addRule((UserSettings.leftKeypadEnabled()) ? RelativeLayout.LEFT_OF : RelativeLayout.RIGHT_OF, R.id.numberTable);
        params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.numberTable);
        tv_subtop.setTextSize(UserSettings.getQuestionTextSize());
        tv_subtop.setLayoutParams(params);

        highlightColumn(0);

        Hints.show(getActivity(), Hints.NUMERICPAD, Hints.BORROW, Hints.SKIPQUESTION, Hints.BACKHOME1);
    }

    private void highlightColumn(int col) {
        TextView tv;
        for (int j=0; j < q.getAnswerLength(); j++) {
            (tv = getBorrowTextView(j)).setTextColor((j == col) ? getResources().getColor(R.color.questionpanel_highlight_text) : getResources().getColor(R.color.questionpanel_normal_text));
            tv.setBackgroundColor((j == col) ? getResources().getColor(R.color.questionpanel_highlight_bg) : getResources().getColor(R.color.questionpanel_normal_bg));
        }
        for (int k=0; k < q.getNumberCount(); k++)
            for (int j=0; j < q.getAnswerLength(); j++) {
                (tv = getNumberTextView(k, j)).setTextColor((j == col) ? getResources().getColor(R.color.questionpanel_highlight_text) : getResources().getColor(R.color.questionpanel_normal_text));
                tv.setBackgroundColor((j == col) ? getResources().getColor(R.color.questionpanel_highlight_bg) : getResources().getColor(R.color.questionpanel_normal_bg));
            }
    }

    private SubtractionNumberTextView getNumberTextView(int row, int col) {
        return (SubtractionNumberTextView) numberTable.getChildAt(row*q.getAnswerLength()+col);
    }
    private TextView getBorrowTextView(int col) {
        return (TextView) borrowRow.getChildAt(borrowRow.getColumnCount()-col-1);
    }
    private TextView getAnswerTextView(int col) {
        return (TextView) answerRow.getChildAt(answerRow.getColumnCount()-col-1);
    }


    public void endSession() {
    }

    public void correctResponse() {
        getAnswerTextView(col).setText(""+q.getDigitAns(col));

        col++;
        if (!q.done(col)) {
            ArrayList<Integer> b = q.getBorrows(borrow);
            updateBorrows(b);
            borrow++;
        }
        highlightColumn(col);
    }

    private void updateBorrows(ArrayList<Integer> b) {
        if (b == null)
            return;

        for (int i=0; i < b.size(); i++) {
            String str = "";
            if (b.get(i) != null)
                str = str+b.get(i);
            getBorrowTextView(i).setText(str);
        }
    }

    @Override
    public void wrongResponse() {
       // col = -1;
    }

    public boolean checkAnswer(int answer) {
        return answer == q.getAnswer(col);
    }

    public boolean questionDone() {
        return q.done(col);
    }

}
