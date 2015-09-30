package ifsb.arithmetico.core;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import ifsb.arithmetico.UserSettings;

/**
 * Created by sufian on 14/9/15.
 */
public class ComboQuestionGenerator extends QuestionGenerator {
    private static Class[] generators = new Class[] {
            AdditionQuestionGenerator.class,
            SubtractionQuestionGenerator.class,
    };
    private static Float[] portions = new Float[] {
            0.5f,
            0.5f,
    };
    private Question[] questions, temp;
    private int q_index;
    public ComboQuestionGenerator() {
        try {
            int numQuestions = UserSettings.getNumQuestions();
            temp = new Question[numQuestions];
            int index = 0, type=0;
            int numQuestionsToGenerate = 0;
            QuestionGenerator generator=null;
            for (; type < generators.length-1; type++) {
                numQuestionsToGenerate = (int) (portions[type]*numQuestions);
                generator = (QuestionGenerator) generators[type].getConstructor().newInstance();
                for (int i = 0; i < numQuestionsToGenerate; i++)
                    temp[index++] = generator.getNextQuestion();
                numQuestions -= numQuestionsToGenerate;
            }
            generator = (QuestionGenerator) generators[type].getConstructor().newInstance();
            for (int i = 0; i < numQuestions; i++)
                temp[index++] = generator.getNextQuestion();

            q_index = 0;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        shuffleQuestions();
    }

    private void shuffleQuestions() {
        questions = new Question[temp.length];
        for (int i=0; i < temp.length; i++)
            questions[i] = null;
        Random random = new Random();
        int index = random.nextInt(temp.length);
        for (int i=0; i < temp.length; i++) {
            while (questions[index] != null) {
                index++;
                if (index == temp.length)
                    index = 0;
            }
            questions[index] = temp[i];
            index += random.nextInt(temp.length);
            if (index >= temp.length)
                index -= temp.length;
        }
        ;
    }
    @Override
    public Question getNextQuestion() {
        return questions[q_index++];
    }
}
