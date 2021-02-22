package utils;

import java.lang.Math;

public final class FloatComparator {

    private final static float epsilon = 0.001f;

    public static boolean equals(float a, float b) {
        return (Math.abs(a - b) < epsilon);
    }

    public static int compare(double a, double b){
        return compare((float) a, (float) b);
    }

    public static int compare(float a, float b) {
        if (Math.abs(a - b) < epsilon)
            return 0;
        else if (a - b > epsilon)
            return 1;
        else
            return -1;
    }
}
