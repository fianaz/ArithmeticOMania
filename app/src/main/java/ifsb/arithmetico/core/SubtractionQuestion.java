package ifsb.arithmetico.core;

import java.util.ArrayList;

/**
 * Created by sufian on 21/8/15.
 */
public class SubtractionQuestion extends Question {
    private int[] number;
    private ArrayList<Integer> digitAns;
    private ArrayList<ArrayList<Integer>> borrows;
    private ArrayList<Integer>[] digitsNum;
    public SubtractionQuestion(int ... number) {
        this.number = new int[number.length];
        for (int i=0; i < number.length; i++)
            this.number[i] = number[i];
        setupAnswer();
    }
    private void setupAnswer() {
        digitsNum = new ArrayList[number.length];
        digitsNum[0] = Utilities.digitize(number[0]);

        int[] sum = new int[digitsNum[0].size()];
        for (int i=0; i < digitsNum[0].size(); i++)
            sum[i] = 0;

        for (int i=1; i < number.length; i++) {
            digitsNum[i] = Utilities.digitize(number[i]);
            for (int j=0; j < digitsNum[i].size(); j++)
                sum[j] += digitsNum[i].get(j);
        }

        digitAns = new ArrayList<Integer>();
        borrows = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> temp = Utilities.makeCopy(digitsNum[0]);


        int highest = -1;
        for (int i=0; i < digitsNum[0].size(); i++)
            if (temp.get(i) >= sum[i]) {
                digitAns.add(temp.get(i) - sum[i]);
                if (i > highest)
                    temp.set(i, null);
                borrows.add(null);
            }
            else {
                int j;
                do {
                    j = doBorrowing(i, temp);
                    if (j > highest)
                        highest = j;
                } while (temp.get(i) < sum[i]);
                digitAns.add(temp.get(i) - sum[i]);
                ArrayList<Integer> b = new ArrayList<Integer>();
                for (int k=0; k <= j; k++)
                    b.add(temp.get(k));
                borrows.add(b);
            }
    }
    private int doBorrowing(int i, ArrayList<Integer> temp) {
        int j=i+1;
        for (; temp.get(j)==0; j++)
            ;
        int highest = j;
        temp.set(j, temp.get(j)-1);
        for (j--; j > i; j--)
            temp.set(j, 9);
        temp.set(i, temp.get(i) + 10);
        return highest;
    }
    public int getNumberCount() {
        return number.length;
    }


    public int getAnswerLength() {
        return digitAns.size();
    }
    public ArrayList<Integer> getDigitsNum(int i) {
        return digitsNum[i];
    }

    public boolean checkAnswer(int col, int answer) {
        return getAnswer(col) == answer;
    }

    public int getAnswer(int col) {
        return digitAns.get(col);
    }
    public boolean done(int col) {
        return col == digitAns.size();
    }
    public int getDigitAns(int col) {
        return digitAns.get(col);
    }
    public ArrayList<Integer> getBorrows(int index) {
        return borrows.get(index);
    }
    @Override
    public char getOpSymbol() {
        return '-';
    }
}
