package physical_objects;

import exceptions.ImpossibleObjectException;
import geometry.*;
import geometry.objects3D.*;
import geometry.polygonal.Polyhedron;
import graph.CanvasPanel;
import limiters.Collisional;
import limiters.Intersectional;
import physics.Material;
import physics.Space;
import utils.Tools;

import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;

public class PhysicalPolyhedron extends AbstractBody implements Collisional, Intersectional {

    private Polyhedron drawableInterpretation;
    private final ArrayList<Point3D> points;
    private final ArrayList<Triangle> triangles;
    private final static long depth = 30;


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
                +v.z * space.getDT() - space.getDT() * space.getDT() * space.getG() / 2d);

        for (int i = 0; i < points.size(); i++)
            points.set(i, movement.addToPoint(points.get(i)));

        for (int i = 0; i < triangles.size(); i++)
            triangles.set(i, triangles.get(i).move(movement));
    }

    public Segment getProjectionOnLine(Line3D line, boolean mode){
        ArrayList<Point3D> points = new ArrayList<>();
        getPoints(mode).forEach(point -> points.add(Tools.countProjectionOfPoint(point, line)));
        SortedMap<Double, Point3D> distances = new TreeMap<>();
        points.forEach(point -> distances.put(new Vector3D(Point3D.ZERO, point).getLength(), point));
        return new Segment(distances.get(distances.firstKey()), distances.get(distances.lastKey()));
    }


    public double getJ(Line3D line, boolean mode) {

        double J = 0d;

        Segment projection = getProjectionOnLine(line, mode);
        Vector3D projectionVector = projection.getVector();
        Point3D currentPoint = projection.point1;
        double movement = projectionVector.getLength() / (double) (depth - 1);

        projectionVector = projectionVector.multiply(movement / projectionVector.getLength());
        ArrayList<Segment> segments = new ArrayList<>();
        currentPoint = projectionVector.addToPoint(currentPoint);
        Plane3D plane = new Plane3D(projectionVector, currentPoint);

        for (int i = 0; i < depth; i++) {

            for (Triangle triangle : triangles){
                ArrayList<Point3D> intersectionPoints = triangle.getIntersectionWithPlane(plane);
                if (intersectionPoints.size() > 1)
                    try {
                        segments.add(new Segment(intersectionPoints.get(0), intersectionPoints.get(1)));
                    } catch (Exception ignored){}
            }
            if (segments.size() != 0) {
                FlatShape shape = new FlatShape(segments);
//
                J += shape.getRelativeJ(line) * material.p * movement;


//                System.out.println(shape.getRelativeJ(line) * material.p * movement);
//
//            System.out.println("+++++++++++++++++++++");

                if (i == depth - 1) J += shape.getRelativeJ(line) * material.p * movement;
            }

            segments.clear();
//            System.out.println(currentPoint);
//            System.out.println(plane);
            currentPoint = projectionVector.addToPoint(currentPoint);
            plane = new Plane3D(projectionVector, currentPoint);

        }

        return J;
    }

    public void applyImpulse(Vector3D impulse, Point3D applicationPoint, boolean mode){
        v = v.add(impulse.multiply(1d / m));
        Vector3D radVector = new Vector3D(getPositionOfCentre(mode), applicationPoint);
        Plane3D impulsePlane = new Plane3D(getPositionOfCentre(mode), applicationPoint, impulse.addToPoint(applicationPoint));
        double J = getJ(new Line3D(getPositionOfCentre(mode), impulsePlane.vector), mode);
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
                    +v.z * space.getDT() - space.getDT() * space.getDT() * space.getG() / 2d);

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
                    +v.z * space.getDT() - space.getDT() * space.getDT() * space.getG() / 2d);

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
                +v.z * space.getDT() - space.getDT() * space.getDT() * space.getG() / 2d);
        drawableInterpretation.setZero(movement.addToPoint(oldZero));

    }
}
