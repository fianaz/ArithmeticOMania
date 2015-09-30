package ifsb.arithmetico.core;

import java.util.ArrayList;

/**
 * Created by sufian on 20/8/15.
 */
public class AdditionQuestion extends Question {
    private int[] number;
    private ArrayList<Integer> digitAns;
    private ArrayList<Integer> carries;
    private ArrayList<Integer>[] digitsNum;
    public AdditionQuestion(int ... number) {
        this.number = new int[number.length];
        for (int i=0; i < number.length; i++)
            this.number[i] = number[i];
        setupAnswer();
    }
    private void setupAnswer() {
        digitsNum = new ArrayList[number.length];
        int sum = 0;
        for (int i=0; i < number.length; i++) {
            sum += number[i];
            digitsNum[i] = Utilities.digitize(number[i]);
        }
        digitAns = new ArrayList<Integer>();
        carries = new ArrayList<Integer>();
        carries.add(0);

        int s = 0;
        int answerLength = (""+sum).length();
        for (int i=0; i < answerLength; i++) {
            s = carries.get(i);
            for (int j=0; j < number.length; j++)
                if (i < digitsNum[j].size())
                    s += digitsNum[j].get(i);
            digitAns.add(s % 10);
            carries.add(s / 10);
        }
    }
    public int getAnswerLength() {
        return digitAns.size();
    }
    public int getNumber(int i) {
        return number[i];
    }

    public int getNumberCount() {
        return number.length;
    }
    public ArrayList<Integer> getDigitsNum(int i) {
        return digitsNum[i];
    }

    public boolean checkAnswer(int col, int answer) {
        return getAnswer(col) == answer;
    }

    private int getAnswer(int col) {
        int answer = 0;
        if (col+1 < carries.size())
            answer = carries.get(col+1)*10;
        else
            answer = carries.get(col);
        if (col < digitAns.size())
            answer += digitAns.get(col);
        return answer;
    }

    public int getDigitAns(int col) {
        return (col < digitAns.size()) ? digitAns.get(col) : -1;
    }

    public int getCarry(int col) {
        return (col < carries.size()) ? carries.get(col) : -1;
    }

    public boolean done(int col) {
        if (col == -1)
            return true;
        if (col < digitAns.size())
            return false;
        if (col >= carries.size())
            return true;
        return (carries.get(col) == 0);
    }

    @Override
    public char getOpSymbol() {
        return '+';
    }
}
