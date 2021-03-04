package geometry;

import geometry.objects3D.Point3D;
import geometry.objects3D.Vector3D;

public class Triangle {
    private final Point3D A, B, C;

    public Triangle(Point3D a, Point3D b, Point3D c) {
        A = a;
        B = b;
        C = c;
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


}
