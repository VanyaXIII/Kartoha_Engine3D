package geometry;

import geometry.objects3D.Point3D;

import java.util.ArrayList;

public class ShapeBuilder {

    private ArrayList<Point3D> points;
    private ArrayList<Tetrahedron> tetrahedrons;

    {
        points = new ArrayList<>();
        tetrahedrons = new ArrayList<>();
    }

    public ShapeBuilder(ArrayList<Point3D> points){
        this.points = points;
    }


    public void tetrahedral(){
        
    }

    public ArrayList<Point3D> getPoints() {
        return points;
    }

}
