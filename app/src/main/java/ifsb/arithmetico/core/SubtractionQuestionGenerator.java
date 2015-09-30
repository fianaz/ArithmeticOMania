package ifsb.arithmetico.core;

/**
 * Created by sufian on 21/8/15.
 */
public class SubtractionQuestionGenerator extends QuestionGenerator {
    @Override
    public Question getNextQuestion() {
        if (true) {
            int firstNum = 0, secondNum = 0;
            firstNum = generateNumber(100, 9999);
            secondNum = generateNumber(100, firstNum);
            return new SubtractionQuestion(firstNum, secondNum);
        }
        else
            return new SubtractionQuestion(8599, 1170);
    }
}
