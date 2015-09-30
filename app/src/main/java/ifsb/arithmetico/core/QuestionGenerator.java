package ifsb.arithmetico.core;

import java.util.Random;

/**
 * Created by sufian on 20/8/15.
 */
public abstract class QuestionGenerator {
    private Random random = new Random();
    abstract public Question getNextQuestion();
    protected int generateNumber(int min, int max) {
        int delta = (min > 0) ? min : 0;
        return generateNumber(max-delta)+delta;
    }
    protected int generateNumber(int max) {
        return random.nextInt(max+1);
    }
}
