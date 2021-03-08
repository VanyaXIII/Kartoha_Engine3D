package geometry;

import geometry.objects3D.Plane3D;
import geometry.objects3D.Point3D;
import geometry.objects3D.Polygon3D;
import geometry.objects3D.Vector3D;
import utils.FloatComparator;

import java.awt.*;

public class Triangle {
    public Point3D A, B, C;

    public Triangle(Point3D a, Point3D b, Point3D c) {
        A = a;
        B = b;
        C = c;
    }

    public Triangle(Polygon3D polygon) {
        A = polygon.a1;
        B = polygon.a2;
        C = polygon.a3;
    }

    public double getSquare() {
        double side1 = new Vector3D(A, B).getLength();
        double side2 = new Vector3D(A, C).getLength();
        double side3 = new Vector3D(B, C).getLength();
        double p = (side1 + side3 + side2) / 2.0f;
        return Math.sqrt(p * (p - side1) * (p - side2) * (p - side3));
    }

    public Point3D getCentroid() {
        Vector3D rC = new Vector3D(0, 0, 0);

        rC.add(new Vector3D(Point3D.ZERO, A).multiply(1d / 3d));
        rC.add(new Vector3D(Point3D.ZERO, B).multiply(1d / 3d));
        rC.add(new Vector3D(Point3D.ZERO, B).multiply(1d / 3d));

        return new Point3D(rC.x, rC.y, rC.z);
    }

    public double getJDivDensity() {
        double side1 = new Vector3D(A, B).getLength();
        double side2 = new Vector3D(A, C).getLength();
        double side3 = new Vector3D(B, C).getLength();
        return (getSquare() / 36d) * (side1 * side1 + side2 * side2 + side3 * side3);
    }

    public Polygon3D toPolygon(Color color) {
        return new Polygon3D(A, B, C, color);
    }



    public Triangle move(Vector3D movement){
        Point3D a = movement.addToPoint(A),
                b = movement.addToPoint(B),
                c = movement.addToPoint(C);

        return new Triangle(a,b,c);
    }

    public boolean contains(Point3D point){
        double s1 = new Triangle(point, A, B).getSquare();
        double s2 = new Triangle(point, A, C).getSquare();
        double s3 = new Triangle(point, B, C).getSquare();

        return FloatComparator.equals(getSquare(), s1 + s2 + s3) &&
                FloatComparator.equals(new Plane3D(A, B, C).distance(point), 0d);
    }

    @Override
    public String toString() {
        return "Triangle{" +
                "A=" + A +
                ", B=" + B +
                ", C=" + C +
                '}';
    }
}
