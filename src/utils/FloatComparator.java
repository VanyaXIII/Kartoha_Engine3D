package utils;

import java.lang.Math;

public final class doubleComparator {

    private final static double epsilon = 0.001f;

    public static boolean equals(double a, double b) {
        return (Math.abs(a - b) < epsilon);
    }

    public static int compare(double a, double b){
        return compare((double) a, (double) b);
    }

    public static int compare(double a, double b) {
        if (Math.abs(a - b) < epsilon)
            return 0;
        else if (a - b > epsilon)
            return 1;
        else
            return -1;
    }
}
