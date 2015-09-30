package ifsb.arithmetico;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import ifsb.arithmetico.core.Question;
import ifsb.arithmetico.core.QuestionGenerator;

/**
 * Created by sufian on 21/8/15.
 */
public class Session implements Runnable {
    private int numQuestions;
    private Question q;
    private QuestionGenerator generator;
    private int remainingQuestions;
    private QuestionActivityListener activity;
    private boolean countdownMode;
    private boolean autonextMode;
    private CountDownTimer timer = null, autonextTimer = null;
    private long remainingTime;
    private int questionsAttempted, skippedQuestions;
    private long accElapsedTime = 0L, elapsedTime = 0L;
    private int failedAttempts = 0;
    private int perfectAttempts = 0;
    private boolean perfectAttempt;
    private boolean hintShowing = false;
    public Session(QuestionActivityListener activity, int numQuestions, QuestionGenerator generator) {
        this.activity = activity;
        this.generator = generator;
        this.numQuestions = remainingQuestions = numQuestions;
        this.countdownMode = UserSettings.countdownEnabled();
        this.autonextMode = UserSettings.autoNextEnabled();
        remainingTime = UserSettings.getCountdownTime()*numQuestions;
        questionsAttempted = skippedQuestions = 0;
    }
    @Override
    public void run() {
        postNextQuestion();
    }
    public void postNextQuestion() {
        perfectAttempt = true;
        q = generator.getNextQuestion();
        activity.postQuestion(q);
        stopTimer();
        startTimer();
    }
    public void startTimer() {
        if (timer == null)
            elapsedTime = SystemClock.elapsedRealtime();
        if (countdownMode && !hintShowing) {
            (timer = new CountDownTimer(remainingTime, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    activity.updateTimerStatus((int) (millisUntilFinished / 1000));
                    remainingTime = millisUntilFinished;
                }

                @Override
                public void onFinish() {
                    activity.updateTimerStatus(0);
                    endSession();
                }
            }).start();
        }
    }
    public boolean timerRunning() {
        return (timer != null);
    }
    public int getFailedAttempts() {
        return failedAttempts;
    }
    public boolean hasEnded() {
        return remainingQuestions ==0;
    }
    public void endSession() {
        if (questionsAttempted == numQuestions && skippedQuestions == 0)
            SoundPlayer.play((Context) activity, (perfectAttempts == numQuestions) ? R.raw.perfectsession : R.raw.endsession);
        skippedQuestions += remainingQuestions;
        remainingQuestions = 0;
        stopTimer();
        activity.endSession();
    }
    public boolean isPerfectAttempt() {
        return perfectAttempt;
    }
    public int getPerfectAttempts() {
        return perfectAttempts;
    }
    public void process(int answer) {
        boolean correct = activity.checkAnswer(answer);
        boolean questionDone = false;
        if (correct) {
            activity.correctResponse();
            questionDone = activity.questionDone();
        }
        else {
            perfectAttempt = false;
            activity.wrongResponse();
            failedAttempts++;
        }
        if (questionDone) {
            stopTimer();
            activity.updateTimerStatus(-1);
            if (correct)
                questionsAttempted++;
            if (perfectAttempt)
                perfectAttempts++;
            remainingQuestions--;
            activity.updateStatus();
            if (autonextMode && remainingQuestions > 0) {
                long period = UserSettings.getAutoNextTime()+1;
                final QuestionActivity questionActivity = (QuestionActivity) activity;
                questionActivity.updateAutoNextStatus((int) period);
                (autonextTimer = new CountDownTimer(period*1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        questionActivity.updateAutoNextStatus((int) (millisUntilFinished / 1000));
                    }

                    @Override
                    public void onFinish() {
                        stopAutoNextTimer();
                        questionActivity.updateAutoNextStatus(0);
                        questionActivity.nextTask();
                    }
                }).start();
            }
        }
    }
    public void skipQuestion() {
        skippedQuestions++;
        remainingQuestions--;
        activity.updateStatus();
        if (hasEnded())
            endSession();
        else
            postNextQuestion();
    }
    public int getNumRemainingQuestions() {
        return remainingQuestions;
    }
    public int getNumQuestions() {
        return numQuestions;
    }
    public void stopAutoNextTimer() {
        if (autonextMode  &&  autonextTimer != null) {
            autonextTimer.cancel();
            autonextTimer = null;
        }
    }
    public void stopTimer() {
        if (timer != null)
            accElapsedTime += (SystemClock.elapsedRealtime() - elapsedTime);

        if (countdownMode) {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        }
    }
    public void hintShowing(boolean status) {
        if (hintShowing = status) {
            stopTimer();
        }
        else if (!hasEnded()) {
            startTimer();
        }
    }
    public int getQuestionsAttempted() {
        return questionsAttempted;
    }
    public int getSkippedQuestions() {
        return skippedQuestions;
    }
    public float getTotalTime() {
        return accElapsedTime /1000F;
    }
}
