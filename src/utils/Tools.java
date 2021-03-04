package utils;

import geometry.objects3D.Plane3D;
import geometry.objects3D.Vector3D;

public final class Tools {

    public static int transformdouble(double d) {
        return (int) java.lang.Math.round(d);
    }

    public static double countAverage(double a, double b) {
        return (a + b) / 2.0f;
    }

    public static double sign(double a){
        return a>=0f ? 1f :-1f;
    }

    public static boolean isNaN(double a){
        return a - a != 0.0f;
    }

    public static Vector3D calcProjectionOfVectorOnPlane(Vector3D vector, Plane3D plane){
        final double projection = (double) (vector.scalarProduct(plane.vector) / plane.vector.getLength());
        System.out.println(projection);
        return vector.add(plane.vector.multiply(-projection/plane.vector.getLength()));
    }

    public static boolean areVectorsSameDirected(Vector3D vector1, Vector3D vector2){
        return vector1.normalize().equals(vector2.normalize().multiply(-1));
    }

}
