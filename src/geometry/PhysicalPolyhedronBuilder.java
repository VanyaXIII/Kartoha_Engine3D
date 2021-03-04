package geometry;

import geometry.objects3D.Point3D;
import geometry.objects3D.Polygon3D;
import geometry.objects3D.Vector3D;

import java.util.ArrayList;

public class PhysicalPolyhedronBuilder {

    private ArrayList<Point3D> points;
    private final ArrayList<Tetrahedron> tetrahedrons;
    private ArrayList<Polygon3D> polygons;

    {
        points = new ArrayList<>();
        polygons = new ArrayList<>();
        tetrahedrons = new ArrayList<>();
    }

    public PhysicalPolyhedronBuilder(ArrayList<Point3D> points, ArrayList<Polygon3D> polygons){
        this.points = points;
        this.polygons = polygons;
        tetrahedral();
    }


    public void tetrahedral(){
        Point3D zeroPoint = points.get(0);
        polygons.forEach(polygon -> tetrahedrons.add(new Tetrahedron(zeroPoint, polygon)));
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
            radVector.add(new Vector3D(zeroPoint, tetrahedron.getCentreOfMass()).multiply(tetrahedron.getVolume() / volume));

        return new Point3D(radVector.x, radVector.y, radVector.z);
    }

    public ArrayList<Point3D> getPoints() {
        return points;
    }

}
