package physical_objects;

import exceptions.ImpossibleObjectException;
import geometry.*;
import geometry.intersections.PolyhedronToPlaneIntersection;
import geometry.objects3D.*;
import geometry.polygonal.Polyhedron;
import graph.CanvasPanel;
import limiters.Collisional;
import limiters.Intersectional;
import physics.Material;
import physics.Space;
import utils.Tools;

import java.util.*;

public class PhysicalPolyhedron extends AbstractBody implements Collisional, Intersectional {

    private Polyhedron drawableInterpretation;
    private final ArrayList<Point3D> points;
    private final ArrayList<Triangle> triangles;
    private final static long depth = 30;

    {
        triangles = new ArrayList<>();
    }


    public PhysicalPolyhedron(Space space, Vector3D v, Vector3D w, PhysicalPolyhedronBuilder builder, Material material) throws ImpossibleObjectException {

        super(space,
                builder.getCentreOfMass().x, builder.getCentreOfMass().y, builder.getCentreOfMass().z,
                v, w, material, builder.getVolume() * material.p);

        this.points = builder.getPoints();
        this.triangles.addAll(builder.getTriangles());
        pushToCanvas(space.getCanvas());
    }


    @Override
    public synchronized void update() {
        super.update();
        
        Vector3D movement = getMovement();

        for (int i =0; i < points.size(); i++)
            points.set(i, points.get(i).rotate(w.multiply(space.getDT()), getPositionOfCentre(false)));

        for (int i = 0; i < triangles.size(); i++)
            triangles.set(i, triangles.get(i).rotate(w.multiply(space.getDT()), getPositionOfCentre(false)));

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


    public synchronized double getJ(Line3D line, boolean mode) {

        double J = 0d;

        Segment projection = getProjectionOnLine(line, mode);
        Vector3D projectionVector = projection.getVector();
        Point3D currentPoint = projection.point1;
        double movement = projectionVector.getLength() / (double) (depth - 1);

        projectionVector = projectionVector.multiply(movement / projectionVector.getLength());
        Set<Segment> segments = new HashSet<>();
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


                if (i == depth - 1) J += shape.getRelativeJ(line) * material.p * movement;
            }

            segments.clear();
//            System.out.println(currentPoint);
            currentPoint = projectionVector.addToPoint(currentPoint);
            plane = new Plane3D(projectionVector, currentPoint);

        }

        return J;
    }

    public synchronized void applyImpulse(Vector3D impulse, Point3D applicationPoint, boolean mode){
        v = v.add(impulse.multiply(1d / m));
        Vector3D radVector = new Vector3D(getPositionOfCentre(mode), applicationPoint);
        Plane3D impulsePlane = new Plane3D(getPositionOfCentre(mode), applicationPoint, impulse.addToPoint(applicationPoint));
        double J = getJ(new Line3D(getPositionOfCentre(mode), impulsePlane.vector), mode);
        w = w.add(radVector.vectorProduct(impulse).multiply(1d / J));
    }

    public synchronized void applyImpulse(Vector3D impulse, Point3D applicationPoint){
        applyImpulse(impulse, applicationPoint, false);
    }

    public synchronized void pullFromPlane(PolyhedronToPlaneIntersection intersection){
        if (intersection.getValue() != 0){
            Vector3D movementVector = new Vector3D(intersection.getPointOfPolygon(), intersection.getCollisionPoint());
            if (movementVector.getLength() != 0d){
                movementVector = movementVector.normalize();
                move(movementVector.multiply(intersection.getValue()));
            }
        }
    }

    public synchronized void move(Vector3D movement){
        x0 += movement.x;
        y0 += movement.y;
        z0 += movement.z;
        for (int i = 0; i < triangles.size(); i++) triangles.set(i, triangles.get(i).move(movement));
        for (int i = 0; i < points.size(); i++) points.set(i, movement.addToPoint(points.get(i)));
        Point3D oldZero = drawableInterpretation.getZero();
        drawableInterpretation.setZero(movement.addToPoint(oldZero));
    }

    public Set<Point3D> getPoints(boolean mode) {
        if (!mode) {
            return new HashSet<>(points);
        }
        else {
            HashSet<Point3D> newPoints = new HashSet<>();

            Vector3D movement = getMovement();

            for (Point3D point : points)
                newPoints.add(movement.addToPoint(point).rotate(w.multiply(space.getDT()), getPositionOfCentre(true)));

            return newPoints;
        }
    }

    public Set<Triangle> getTriangles(boolean mode){
        if (!mode) {
            return new HashSet<>(triangles);
        }
        else {
            Set<Triangle> newTriangles = new HashSet<>();

            Vector3D movement = getMovement();

            for (Triangle triangle : triangles)
                newTriangles.add(triangle.move(movement).rotate(w.multiply(space.getDT()), getPositionOfCentre(true)));

            return newTriangles;

        }
    }


    public Set<Segment> getSegments(boolean mode){
        HashSet<Segment> segments = new HashSet<>();
        for (Triangle triangle : getTriangles(mode))
            segments.addAll(triangle.getSegments());
        return segments;
    }

    private Vector3D getMovement() {
        return new Vector3D(v.x * space.getDT() + a.x * space.getDT() * space.getDT() / 2d,
                v.y * space.getDT() + a.y * space.getDT() * space.getDT() / 2d,
                v.z * space.getDT() + a.z * space.getDT() * space.getDT() / 2d);
    }

    public Vector3D getVelOfPoint(Point3D point, boolean mode){
        return v.add(getAngularVelOfPoint(point, mode));
    }

    public Vector3D getAngularVelOfPoint(Point3D point, boolean mode){
        return w.vectorProduct(new Vector3D(getPositionOfCentre(mode), point));
    }

    @Override
    public void pushToCanvas(CanvasPanel canvas) {
        Set<Polygon3D> polygons = new HashSet<>();
        triangles.forEach(triangle -> polygons.add(triangle.toPolygon(Tools.getRandomColor())));
        drawableInterpretation = new Polyhedron(Point3D.ZERO, polygons);
        canvas.getPolygonals().add(drawableInterpretation);
    }

    @Override
    public void updateDrawingInterpretation() {
        drawableInterpretation.rotate(w.multiply(space.getDT()), getPositionOfCentre(false));
        Point3D oldZero = drawableInterpretation.getZero();
        Vector3D movement = getMovement();
        drawableInterpretation.setZero(movement.addToPoint(oldZero));

    }
}
