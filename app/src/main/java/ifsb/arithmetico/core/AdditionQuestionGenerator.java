package ifsb.arithmetico.core;

/**
 * Created by sufian on 21/8/15.
 */
public class AdditionQuestionGenerator extends QuestionGenerator {
    @Override
    public Question getNextQuestion() {
        return new AdditionQuestion(generateNumber(100, 999), generateNumber(100, 999));
    }
}
