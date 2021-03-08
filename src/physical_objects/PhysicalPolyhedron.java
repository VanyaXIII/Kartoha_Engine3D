package physical_objects;

import exceptions.ImpossibleObjectException;
import geometry.PhysicalPolyhedronBuilder;
import geometry.Triangle;
import geometry.objects3D.Line3D;
import geometry.objects3D.Point3D;
import geometry.objects3D.Polygon3D;
import geometry.objects3D.Vector3D;
import geometry.polygonal.Polyhedron;
import graph.CanvasPanel;
import limiters.Collisional;
import limiters.Intersectional;
import physics.Material;
import physics.Space;
import utils.Tools;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class PhysicalPolyhedron extends AbstractBody implements Collisional, Intersectional {

    private Polyhedron drawableInterpretation;
    private ArrayList<Point3D> points;
    private ArrayList<Triangle> triangles;



    public PhysicalPolyhedron(Space space, Vector3D v, Vector3D w, PhysicalPolyhedronBuilder builder, Material material) throws ImpossibleObjectException{

        super(space,
                builder.getCentreOfMass().x, builder.getCentreOfMass().y, builder.getCentreOfMass().z,
                v, w, material, builder.getVolume() * material.p);

        this.points = builder.getPoints();
        this.triangles = builder.getTriangles();
        pushToCanvas(space.getCanvas());
    }


    @Override
    public synchronized void update() {
        super.update();
        Vector3D movement = new Vector3D(v.x * space.getDT(),
                v.y * space.getDT(),
                v.z * space.getDT() + space.getDT()*space.getDT() * space.getG() / 2d);

        for (int i = 0; i < points.size(); i++)
            points.set(i, movement.addToPoint(points.get(i)));

        for (Triangle triangle : triangles)
            triangle.move(movement);
    }

    public double getJ(Line3D line){
        ArrayList<Point3D> intersectionPoints = new ArrayList<>();
        return 0d;
    }

    @Override
    public void pushToCanvas(CanvasPanel canvas) {
        ArrayList<Polygon3D> polygons = new ArrayList<>();
        triangles.forEach(triangle -> polygons.add(triangle.toPolygon(Tools.getRandomColor())));
        drawableInterpretation = new Polyhedron(Point3D.ZERO, polygons);
        canvas.getPolygonals().add(drawableInterpretation);
    }

    @Override
    public void updateDrawingInterpretation() {
        drawableInterpretation.rotate(w.multiply(space.getDT()), getPositionOfCentre(false));
        Point3D oldZero = drawableInterpretation.getZero();
        Vector3D movement = new Vector3D(v.x * space.getDT(),
                v.y * space.getDT(),
                v.z * space.getDT() + space.getDT()*space.getDT() * space.getG() / 2d);
        drawableInterpretation.setZero(movement.addToPoint(oldZero));

    }
}
