package ifsb.arithmetico;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;

import ifsb.arithmetico.R;
import ifsb.arithmetico.core.AdditionQuestion;
import ifsb.arithmetico.core.Question;
import ifsb.arithmetico.core.QuestionGenerator;


public class QuestionActivity extends AppCompatActivity implements QuestionActivityListener,KeypadFragment.KeypadListener,AdditionPanelFragment.OnQuestionPanelListener {
    private static BackgroundMusicPlayer bgMusicPlayer = null;
    private KeypadFragment keypadFragment;
    protected QuestionActivityListener questionActivityListener = null;
    private Button btn_nextquestion;
    private Button btn_skipquestion;
    private Button btn_viewreport;
    private Session session;
    private TextView tv_status;
    private TextView tv_timerstatus;
    private TextView tv_autonext;
    private TextView tv_sessionended;
    private ImageView iv_perfect, iv_right;
    private Handler theHandler;
    private QuestionPanelFragment addpf, subtpf, current=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        try {
            session = new Session(this, getIntent().getIntExtra("numquestions", 0),
                    (QuestionGenerator) (Class.forName(getIntent().getStringExtra("generator")).getConstructor().newInstance()));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        addpf = setQuestionPanelFragment("ifsb.arithmetico.AdditionPanelFragment");
        subtpf = setQuestionPanelFragment("ifsb.arithmetico.SubtractionPanelFragment");

        btn_nextquestion = (Button) findViewById(R.id.btn_nextquestion);
        btn_nextquestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (session != null) {
                    keypadFragment.setDisabled(false);
                    if (!session.hasEnded()) {
                        session.stopTimer();
                        session.stopAutoNextTimer();
                        updateAutoNextStatus(0);
                        nextTask();
                    }
                }
            }
        });
        btn_nextquestion.setVisibility(View.INVISIBLE);

        btn_skipquestion = (Button) findViewById(R.id.btn_skipquestion);
        btn_skipquestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (session != null) {
                    keypadFragment.setDisabled(false);
                    if (!session.hasEnded())
                        nextTask();
                }
            }
        });
        btn_skipquestion.setVisibility(View.INVISIBLE);

        btn_viewreport = (Button) findViewById(R.id.btn_viewreport);
        btn_viewreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (session != null) {
                    keypadFragment.setDisabled(false);
                    if (session.hasEnded()) {
                        Intent intent = new Intent(getApplicationContext(), SessionReportActivity.class);
                        intent.putExtra("numquestions", session.getNumQuestions());
                        intent.putExtra("numattempted", session.getQuestionsAttempted());
                        intent.putExtra("numperfect", session.getPerfectAttempts());
                        intent.putExtra("skipped", session.getSkippedQuestions());
                        intent.putExtra("totaltime", session.getTotalTime());
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
        btn_viewreport.setVisibility(View.INVISIBLE);

        tv_status = (TextView) findViewById(R.id.tv_status);
        tv_timerstatus = (TextView) findViewById(R.id.tv_timerstatus);
        tv_timerstatus.setTextSize(40);
        tv_autonext = (TextView) findViewById(R.id.tv_autonext);
        tv_sessionended = (TextView) findViewById(R.id.tv_sessionended);
        tv_sessionended.setVisibility(View.INVISIBLE);

        final FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame_question);
        iv_perfect = (ImageView) findViewById(R.id.iv_perfect);
        iv_right = (ImageView) findViewById(R.id.iv_right);
        frameLayout.post(new Runnable() {
            @Override
            public void run() {
                int size = frameLayout.getWidth() / 2;
                iv_perfect.setAdjustViewBounds(true);
                iv_perfect.setMaxWidth(size);
                iv_perfect.setMaxHeight(size);
                iv_perfect.setScaleType(ImageView.ScaleType.FIT_CENTER);
                iv_perfect.setImageResource(R.drawable.perfect);

                iv_right.setAdjustViewBounds(true);
                iv_right.setMaxWidth(size);
                iv_right.setMaxHeight(size);
                iv_right.setScaleType(ImageView.ScaleType.FIT_CENTER);
                iv_right.setImageResource(R.drawable.right);
            }
        });
        updateStatus();

        keypadFragment = (KeypadFragment) getFragmentManager().findFragmentById(R.id.frgmt_keypad);
        if (UserSettings.leftKeypadEnabled()) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) keypadFragment.getView().getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            keypadFragment.getView().setLayoutParams(params);

            params = (RelativeLayout.LayoutParams) frameLayout.getLayoutParams();
            params.addRule(RelativeLayout.LEFT_OF, 0);
            params.addRule(RelativeLayout.RIGHT_OF, R.id.frgmt_keypad);
            frameLayout.setLayoutParams(params);

            params = (RelativeLayout.LayoutParams) tv_timerstatus.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            tv_timerstatus.setLayoutParams(params);

            params = (RelativeLayout.LayoutParams) iv_perfect.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            iv_perfect.setLayoutParams(params);

            params = (RelativeLayout.LayoutParams) iv_right.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            iv_right.setLayoutParams(params);

            params = (RelativeLayout.LayoutParams) btn_nextquestion.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            btn_nextquestion.setLayoutParams(params);

            params = (RelativeLayout.LayoutParams) btn_skipquestion.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            btn_skipquestion.setLayoutParams(params);

            params = (RelativeLayout.LayoutParams) tv_status.getLayoutParams();
            params.addRule(RelativeLayout.RIGHT_OF, 0);
            params.addRule(RelativeLayout.LEFT_OF, R.id.btn_nextquestion);
            tv_status.setLayoutParams(params);

            RelativeLayout layout = (RelativeLayout) findViewById(R.id.relativeLayout3);
            params = (RelativeLayout.LayoutParams) layout.getLayoutParams();
            params.addRule(RelativeLayout.LEFT_OF, 0);
            params.addRule(RelativeLayout.RIGHT_OF, R.id.frgmt_keypad);
            layout.setLayoutParams(params);

        }

        BackgroundMusicPlayer.resumePlayer();

        (theHandler = new Handler()).post(session);
    }

    protected void showQuestionPanelFragment(QuestionPanelFragment fragment) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (current != null)
            ft.hide(current);
        ft.show(current = fragment);
        ft.commit();
        questionActivityListener = fragment;
    }

    protected QuestionPanelFragment setQuestionPanelFragment(String fragmentClassName) {
        QuestionPanelFragment questionPanelFragment = QuestionPanelFragment.create(fragmentClassName);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.frame_question, questionPanelFragment);
        ft.hide(questionPanelFragment);
        ft.commit();
        questionActivityListener = questionPanelFragment;
        return questionPanelFragment;
    }

    @Override
    public void onBackPressed() {
        if (session.hasEnded())
            finish();
        else
            session.endSession();
    }

    public void nextTask() {
        if (questionDone()) {
            if (session.hasEnded())
                session.endSession();
            else
                session.postNextQuestion();
        }
        else
            session.skipQuestion();
    }

    public void updateAutoNextStatus(int timeLeft) {
        if (timeLeft == 0) {
            tv_autonext.setVisibility(View.INVISIBLE);
            SoundPlayer.play(this, R.raw.start);
            keypadFragment.setDisabled(false);
        }
        else if (tv_autonext.getVisibility() != View.VISIBLE) {
            keypadFragment.setDisabled(true);
            tv_autonext.setVisibility(View.VISIBLE);
        }
        else if (!tv_autonext.getText().equals(""+timeLeft)) {
            tv_autonext.setText("" + timeLeft);
            SoundPlayer.play(this, R.raw.bleep);
        }
    }

    public Session getSession() {
        return session;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public void onKeypadInteraction(int number) {
        keypadFragment.setDisabled(true);
        session.process(number);
        keypadFragment.setDisabled(false);
        if (session.hasEnded()) {
            keypadFragment.setDisabled(true);
            //btn_nextquestion.setText("View Report");
            session.endSession();
        }
        else if (questionDone()) {
            keypadFragment.setDisabled(true);
//            btn_nextquestion.setText("Next");
            btn_skipquestion.setVisibility(View.INVISIBLE);
            btn_nextquestion.setVisibility(View.VISIBLE);
            Hints.show(this, Hints.NEXTQUESTION);
        }
    }

    public void onQuestionPanelInteraction() {

    }

    public void updateStatus() {
        String str = "";
        if (!session.hasEnded())
            str = str+session.getNumRemainingQuestions();
        tv_status.setText(str);
    }

    @Override
    public void updateTimerStatus(int secondsRemaining) {
        if (secondsRemaining == -1) {
            tv_timerstatus.setTextColor(Color.RED);
            Animation animation = new AlphaAnimation(0.0f, 1.0f);
            animation.setDuration(250L);
            animation.setRepeatMode(Animation.REVERSE);
            animation.setRepeatCount(Animation.INFINITE);
            animation.startNow();
            tv_timerstatus.startAnimation(animation);
        }
        else {
            if (tv_timerstatus.getAnimation() != null)
                tv_timerstatus.clearAnimation();
            if (!tv_timerstatus.getText().equals("" + secondsRemaining)) {
                tv_timerstatus.setTextColor(Color.GRAY);
                tv_timerstatus.setText("" + secondsRemaining);
            }
        }
    }

    @Override
    public void postQuestion(Question q) {
        if (q instanceof AdditionQuestion)
            showQuestionPanelFragment(addpf);
        else
            showQuestionPanelFragment(subtpf);

        btn_nextquestion.setVisibility(View.INVISIBLE);
        btn_skipquestion.setVisibility(View.VISIBLE);
        iv_perfect.setAlpha(0f);
        iv_right.setAlpha(0f);
        questionActivityListener.postQuestion(q);
    }

    @Override
    public void endSession() {
        tv_sessionended.setText((session.getPerfectAttempts() == session.getNumQuestions()) ? "PERFECT!" : "End of Session");
        tv_sessionended.setVisibility(View.VISIBLE);
        btn_skipquestion.setVisibility(View.INVISIBLE);
        btn_viewreport.setVisibility(View.VISIBLE);
        Hints.show(this, Hints.VIEWREPORT);

        BackgroundMusicPlayer.pausePlayer();
    }

    @Override
    public void correctResponse() {
        keypadFragment.reset();
        questionActivityListener.correctResponse();
        if (questionActivityListener.questionDone()) {
            SoundPlayer.play(this, R.raw.right);
            if (session.isPerfectAttempt())
                iv_perfect.setAlpha(1.0f);
            else
                iv_right.setAlpha(1.0f);
        }
    }

    @Override
    public void wrongResponse() {
        keypadFragment.reset();
        questionActivityListener.wrongResponse();
        SoundPlayer.play(this, R.raw.wrong);
        Toast.makeText(this,"Incorrect. Try again.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean checkAnswer(int answer) {
        return questionActivityListener.checkAnswer(answer);
    }

    @Override
    public boolean questionDone() {
        return questionActivityListener.questionDone();
    }

    private int getDisplayWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, displayMetrics.widthPixels, getResources().getDisplayMetrics());
    }

    private int getDisplayHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, displayMetrics.heightPixels, getResources().getDisplayMetrics());
        return height;
    }

    @Override
    public void onResume() {
        if (!ScreenReceiver.wasScreenOn) {
            // Screen about to be turned ON
        }
        if (!session.hasEnded()) {
            BackgroundMusicPlayer.resumePlayer();
            session.startTimer();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (session.timerRunning())
            session.stopTimer();
        if (ScreenReceiver.wasScreenOn) {
            // Screen about to be turned OFF
        }
        BackgroundMusicPlayer.pausePlayer();
        super.onPause();
    }
}
