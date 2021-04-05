package utils;

import geometry.objects3D.Line3D;
import geometry.objects3D.Plane3D;
import geometry.objects3D.Point3D;
import geometry.objects3D.Vector3D;

import java.awt.*;
import java.util.Random;

/**Различные полезные и не очень инструменты
 */
public final class Tools {

    /**
     * Метод, совершающий перевод числа с плавающей точкой в целое число
     * @param d число с плавающей точкой
     * @return Целое число
     */
    public static int transformDouble(double d) {
        return (int) java.lang.Math.round(d);
    }

    /**
     * Метод, совершающий расчет среднего из двух чисед
     * @param a число 1
     * @param b число 2
     * @return Их среднее значение
     */
    public static double countAverage(double a, double b) {
        return (a + b) / 2.0f;
    }

    /**
     * Метод, совершающий расчет проекции вектора на плоскость
     * @param vector вектор
     * @param plane плоскость, на которую нужно проэцировать вектор
     * @return проекция вектора на плоскость
     */
    public static Vector3D calcProjectionOfVectorOnPlane(Vector3D vector, Plane3D plane) {
        double projection = (vector.scalarProduct(plane.vector) / plane.vector.getLength());
        return vector.add(plane.vector.multiply(-projection / plane.vector.getLength()));
    }

    /**
     * @return Рандомный цвет
     */
    public static Color getRandomColor() {
        Random random = new Random(System.nanoTime());
        float r = random.nextFloat();
        float g = random.nextFloat();
        float b = random.nextFloat();
        return new Color(r,g,b);
    }

    /**
     * Метод, совершающий расчет проекции точки на прямую
     * @param point точка
     * @param line прямая, на которую нужно проэцировать
     * @return Точка - проекция
     */
    public static Point3D countProjectionOfPoint(Point3D point, Line3D line){
        Plane3D plane = new Plane3D(line.vector, point);
        return line.getIntersection(plane).get();
    }

}
