package utils;

public final class Tools {

    public static int transformFloat(float d) {
        return (int) java.lang.Math.round(d);
    }

    public static float countAverage(float a, float b) {
        return (a + b) / 2.0f;
    }

    public static float sign(float a){
        return a>=0f ? 1f :-1f;
    }

    public static boolean isNaN(float a){
        return a - a != 0.0f;
    }

}
