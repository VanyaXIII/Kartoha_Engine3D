package geometry;

import geometry.objects3D.Point3D;
import geometry.objects3D.Vector3D;
import shapes.Shape;

import java.util.ArrayList;

public class PhysicalPolyhedronBuilder {

    private ArrayList<Point3D> points;
    private final ArrayList<Tetrahedron> tetrahedrons;
    private ArrayList<Triangle> triangles;

    {
        points = new ArrayList<>();
        triangles = new ArrayList<>();
        tetrahedrons = new ArrayList<>();
    }

    public PhysicalPolyhedronBuilder(ArrayList<Point3D> points, ArrayList<Triangle> triangles){
        this.points = points;
        this.triangles = triangles;
        tetrahedral();
    }

    public PhysicalPolyhedronBuilder(Shape shape, Point3D zero){

        shape.getPoints().forEach(point -> points.add(point.from(zero)));

        shape.getTriangles().forEach(triangle -> {
            triangles.add(new Triangle(triangle.A.from(zero),
                    triangle.B.from(zero),
                    triangle.C.from(zero)));
        });

        tetrahedral();
    }


    public void tetrahedral(){
        Point3D zeroPoint = points.get(0);
        triangles.forEach(triangle -> tetrahedrons.add(new Tetrahedron(zeroPoint, triangle)));
    }

    public double getVolume(){
        double volume = 0;
        for (Tetrahedron tetrahedron : tetrahedrons)
            volume += tetrahedron.getVolume();

        return volume;
    }

    public Point3D getCentreOfMass(){
        Point3D zeroPoint = new Point3D(0,0,0);
        Vector3D radVector = new Vector3D(0,0,0);
        double volume = getVolume();

        for (Tetrahedron tetrahedron : tetrahedrons)
            radVector = radVector.add(new Vector3D(zeroPoint, tetrahedron.getCentreOfMass()).multiply(tetrahedron.getVolume() / volume));


        return new Point3D(radVector.x, radVector.y, radVector.z);
    }

    public ArrayList<Point3D> getPoints() {
        return points;
    }

    public ArrayList<Triangle> getTriangles() {
        return triangles;
    }
}
