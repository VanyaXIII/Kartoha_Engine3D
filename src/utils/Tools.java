package utils;

import geometry.objects3D.Line3D;
import geometry.objects3D.Plane3D;
import geometry.objects3D.Point3D;
import geometry.objects3D.Vector3D;

import java.awt.*;
import java.util.Random;

public final class Tools {

    public static int transformDouble(double d) {
        return (int) java.lang.Math.round(d);
    }

    public static double countAverage(double a, double b) {
        return (a + b) / 2.0f;
    }

    public static Vector3D calcProjectionOfVectorOnPlane(Vector3D vector, Plane3D plane) {
        double projection = (vector.scalarProduct(plane.vector) / plane.vector.getLength());
        return vector.add(plane.vector.multiply(-projection / plane.vector.getLength()));
    }

    public static Color getRandomColor() {
        Random random = new Random(System.nanoTime());
        float r = random.nextFloat();
        float g = random.nextFloat();
        float b = random.nextFloat();
        return new Color(r,g,b);
    }

    public static Point3D countProjectionOfPoint(Point3D point, Line3D line){
        Plane3D plane = new Plane3D(line.vector, point);
        return line.getIntersection(plane).get();
    }

}
