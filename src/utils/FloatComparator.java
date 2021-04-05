package utils;

import java.lang.Math;

/**
 * Сранитель чисел с плавающей точкой
 */
public final class FloatComparator {

    private final static double epsilon = 0.01d;

    /**
     * Метод, проверяющий числа на равенства
     * @param a число 1
     * @param b число 2
     * @return Равны ои числа
     */
    public static boolean equals(double a, double b) {
        return (Math.abs(a - b) < epsilon);
    }


    /**
     * Метод, сравнивающий два числа
     * @param a число 1
     * @param b число 2
     * @return Результат сравнения данных чисел
     */
    public static int compare(double a, double b) {
        if (Math.abs(a - b) < epsilon)
            return 0;
        else if (a - b > epsilon)
            return 1;
        else
            return -1;
    }
}
