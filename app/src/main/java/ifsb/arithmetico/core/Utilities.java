package ifsb.arithmetico.core;

import java.util.ArrayList;

/**
 * Created by sufian on 22/8/15.
 */
public class Utilities {
    public static ArrayList<Integer> digitize(int number) {
        ArrayList<Integer> digits = new ArrayList<Integer>();
        do {
            digits.add(number % 10);
            number = number/10;
        } while (number != 0);
        return digits;
    }
    public static ArrayList<Integer> makeCopy(ArrayList<Integer> original) {
        ArrayList<Integer> copy = new ArrayList<Integer>();
        for (int i=0; i < original.size(); i++)
            copy.add(new Integer(original.get(i)));
        return copy;
    }
}
