package geometry;

import geometry.objects3D.Line3D;
import geometry.objects3D.Point3D;
import geometry.objects3D.Vector3D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class FlatShape {

    private final Set<Segment> segments;
    private final Set<Triangle> triangles;

    {
        triangles = new HashSet<>();
    }

    public FlatShape(Set<Segment> segments){
        this.segments = segments;
        triangulate();
    }

    public void triangulate(){
        Point3D zeroPoint = segments.iterator().next().point1;
        for (Segment segment : segments) triangles.add(new Triangle(zeroPoint, segment));
    }

    public Point3D getCentreOfMass() {
        Vector3D rC = new Vector3D(0,0,0);
        double s = 0d;

        for (Triangle triangle : triangles) {
            Vector3D rCOfTriangle = new Vector3D(Point3D.ZERO, triangle.getCentroid());
            rCOfTriangle = rCOfTriangle.multiply(triangle.getSquare());

            s += triangle.getSquare();
            rC = rC.add(rCOfTriangle);
        }

        if (s == 0.0) s = 0.01;

        rC = rC.multiply(1d / s);

        return new Point3D(rC.x, rC.y, rC.z);
    }

    public double getSquare() {
        double square = 0f;
        for (Triangle triangle : triangles) {
            square += triangle.getSquare();
        }
        return square;
    }

    public double getJDivDensity() {
        Point3D c = getCentreOfMass();
        double J = 0d;

        for (Triangle triangle : triangles){
            double d = new Vector3D(c, triangle.getCentroid()).getLength();
            J += triangle.getJDivDensity() + triangle.getSquare() * d * d;
        }

        return J;
    }

    public double getRelativeJ(Line3D line){
        double d = line.distance(getCentreOfMass());
        return getJDivDensity() + getSquare() * d * d;
    }

    @Override
    public String toString() {
        return "FlatShape{" +
                "segments=" + segments +
                ", triangles=" + triangles +
                '}';
    }
}



