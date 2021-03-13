package geometry;

import geometry.objects3D.*;
import utils.FloatComparator;

import java.awt.*;
import java.util.ArrayList;

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

    public Triangle(Point3D point, Segment segment){
        A = point;
        B = segment.point1;
        C = segment.point2;
    }

    public double getSquare() {
        double side1 = new Vector3D(A, B).getLength();
        double side2 = new Vector3D(A, C).getLength();
        double side3 = new Vector3D(B, C).getLength();
        double p = (side1 + side3 + side2) / 2.0d;
        return Math.sqrt(Math.abs(p * (p - side1) * (p - side2) * (p - side3)));
    }

    public Point3D getCentroid() {
        Vector3D rC = new Vector3D(0, 0, 0);
        rC = rC.add(new Vector3D(Point3D.ZERO, A).multiply(1d / 3d));
        rC = rC.add(new Vector3D(Point3D.ZERO, B).multiply(1d / 3d));
        rC = rC.add(new Vector3D(Point3D.ZERO, C).multiply(1d / 3d));

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


    public Triangle move(Vector3D movement) {
        Point3D a = movement.addToPoint(A),
                b = movement.addToPoint(B),
                c = movement.addToPoint(C);

        return new Triangle(a, b, c);
    }

    public boolean contains(Point3D point) {
        double s1 = new Triangle(point, A, B).getSquare();
        double s2 = new Triangle(point, A, C).getSquare();
        double s3 = new Triangle(point, B, C).getSquare();

        return FloatComparator.equals(getSquare(), s1 + s2 + s3) &&
                FloatComparator.equals(new Plane3D(A, B, C).distance(point), 0d);
    }

    public Plane3D getPlane() {
        return new Plane3D(A, B, C);
    }

    public ArrayList<Point3D> getIntersectionWithPlane(Plane3D plane){
        ArrayList<Point3D> intersectionPoints = new ArrayList<>();

        for (Line3D triangleLine : getLines()) {
            try {
                Point3D intersectionPoint = plane.getIntersection(triangleLine).get();
                if (contains(intersectionPoint)) {
                    intersectionPoints.add(intersectionPoint);
                }
            }
            catch (Exception ignored){}
        }

        return intersectionPoints;
    }

    public ArrayList<Line3D> getLines(){
        ArrayList<Line3D> lines = new ArrayList<>();
        lines.add(new Line3D(A, B));
        lines.add(new Line3D(C, B));
        lines.add(new Line3D(A, C));
        return lines;
    }

    public Triangle rotate(Vector3D vector, Point3D point){
        Point3D a = A.rotate(vector, point);
        Point3D b = B.rotate(vector, point);
        Point3D c = C.rotate(vector, point);
        return new Triangle(a,b,c);
    }

    public boolean isIntersectedWithSegment(Segment segment){
        try {
            Point3D intersectionPoint = getPlane().getIntersection(segment).get();
            return new AABB(segment).isPointIn(intersectionPoint) && contains(intersectionPoint);
        }
        catch (Exception exception){
            return false;
        }

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
