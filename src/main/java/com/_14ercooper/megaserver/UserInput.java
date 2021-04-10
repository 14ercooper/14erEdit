package com._14ercooper.megaserver;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserInput {
    // Get an input in a numeric range
    public static int numberRange(int min, int max) {
        try {
            int i = Integer.parseInt(System.console().readLine());
            if (i >= min && i <= max)
                return i;
            else {
                System.out.print("Input outside of valid range.\n> ");
                return numberRange(min, max);
            }
        } catch (Exception e) {
            System.out.print("Input invalid.\n  > ");
            return numberRange(min, max);
        }
    }

    public static String patternMatch(String regex) {
        try {
            String s = System.console().readLine();
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(s);
            if (m.find())
                return s;
            else {
                System.out.print("Input does not match required pattern.\n> ");
                return patternMatch(regex);
            }
        } catch (Exception e) {
            System.out.print("Input invalid.\n> ");
            return patternMatch(regex);
        }
    }

    public static String fromList(List<String> choices) {
        int i = 1;
        for (String s : choices) {
            System.out.println("\t" + i + ") " + s);
            i++;
        }
        System.out.print("> ");
        return choices.get(numberRange(1, i) - 1);
    }
}
