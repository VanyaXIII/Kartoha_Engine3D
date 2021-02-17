package utils;

public final class FloatComparator {

    private final static float epsilon = 0.001f;

    public static boolean equals(float a, float b){
        return (java.lang.Math.abs(a - b) < epsilon);
    }
}
