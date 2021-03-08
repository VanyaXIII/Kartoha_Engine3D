package physical_objects;

import exceptions.ImpossibleObjectException;
import geometry.PhysicalPolyhedronBuilder;
import geometry.Triangle;
import geometry.objects3D.*;
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
    private final ArrayList<Point3D> points;
    private final ArrayList<Triangle> triangles;


    public PhysicalPolyhedron(Space space, Vector3D v, Vector3D w, PhysicalPolyhedronBuilder builder, Material material) throws ImpossibleObjectException {

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
                -v.z * space.getDT() - space.getDT() * space.getDT() * space.getG() / 2d);

        for (int i = 0; i < points.size(); i++)
            points.set(i, movement.addToPoint(points.get(i)));

        for (int i = 0; i < triangles.size(); i++)
            triangles.set(i, triangles.get(i).move(movement));
    }

    public double getJ(Line3D line) {
        ArrayList<Point3D> intersectionPoints = new ArrayList<>();
        return 0d;
    }

    public void applyImpulse(Vector3D impulse, Point3D applicationPoint, boolean mode){
        v = v.add(impulse.multiply(1d / m));
        Vector3D radVector = new Vector3D(getPositionOfCentre(mode), applicationPoint);
        Plane3D impulsePlane = new Plane3D(getPositionOfCentre(mode), applicationPoint, impulse.addToPoint(applicationPoint));
        double J = getJ(new Line3D(getPositionOfCentre(mode), impulsePlane.vector));
        w = w.add(radVector.vectorProduct(impulse).multiply(1d / J));
    }

    public void applyImpulse(Vector3D impulse, Point3D applicationPoint){
        applyImpulse(impulse, applicationPoint, false);
    }

    public ArrayList<Point3D> getPoints(boolean mode) {
        if (!mode) {
            return points;
        }
        else {
            ArrayList<Point3D> newPoints = new ArrayList<>();

            Vector3D movement = new Vector3D(v.x * space.getDT(),
                    v.y * space.getDT(),
                    -v.z * space.getDT() - space.getDT() * space.getDT() * space.getG() / 2d);

            for (int i = 0; i < points.size(); i++)
                newPoints.add(i, movement.addToPoint(points.get(i)));

            return newPoints;
        }
    }

    public ArrayList<Triangle> getTriangles(boolean mode){
        if (!mode) {
            return triangles;
        }
        else {
            ArrayList<Triangle> newTriangles = new ArrayList<>();

            Vector3D movement = new Vector3D(v.x * space.getDT(),
                    v.y * space.getDT(),
                    -v.z * space.getDT() - space.getDT() * space.getDT() * space.getG() / 2d);

            for (int i = 0; i < triangles.size(); i++)
                newTriangles.add(i, triangles.get(i).move(movement));

            return newTriangles;

        }
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
                -v.z * space.getDT() - space.getDT() * space.getDT() * space.getG() / 2d);
        drawableInterpretation.setZero(movement.addToPoint(oldZero));

    }
}
