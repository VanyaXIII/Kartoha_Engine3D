package geometry;

import geometry.objects3D.Line3D;
import geometry.objects3D.Point3D;
import geometry.objects3D.Vector3D;

import java.util.ArrayList;

public class FlatShape {

    private ArrayList<Point3D> points;
    private final ArrayList<Triangle> triangles;

    {
        points = new ArrayList<>();
        triangles = new ArrayList<>();
    }

    public FlatShape(ArrayList<Point3D> points){
        this.points = points;
        triangulate();
    }

    public void triangulate(){
        for (int i = 2; i < points.size(); i++) {
            triangles.add(new Triangle(points.get(0), points.get(i - 1), points.get(i)));
        }
    }

    public Point3D getCentreOfMass() {
        Vector3D rC = new Vector3D(0,0,0);
        double s = 0f;

        for (Triangle triangle : triangles) {
            Vector3D rCOfTriangle = new Vector3D(Point3D.ZERO, triangle.getCentroid());
            rCOfTriangle.multiply(triangle.getSquare());

            s += triangle.getSquare();
            rC.add(rCOfTriangle);
        }

        rC.multiply(1d / s);

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
        double J = 0f;

        for (Triangle triangle : triangles){
            double d = new Vector3D(c, triangle.getCentroid()).getLength();
            d *= d;
            J += triangle.getJDivDensity() + triangle.getSquare() * d;
        }

        return J;
    }

    public double getRelativeJ(Line3D line){
        double d = line.distance(getCentreOfMass());
        return getJDivDensity() + getSquare() * d * d;
    }
}



