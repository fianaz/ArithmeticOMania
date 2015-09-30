package ifsb.arithmetico;

import ifsb.arithmetico.core.Question;

/**
 * Created by sufian on 21/8/15.
 */
public interface QuestionActivityListener {
    public void postQuestion(Question q);
    public void endSession();
    public void correctResponse();
    public void wrongResponse();
    public boolean checkAnswer(int answer);
    public boolean questionDone();
    public void updateStatus();
    public void updateTimerStatus(int secondsRemaining);
}
