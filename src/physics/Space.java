package physics;

import exceptions.ImpossibleObjectException;
import geometry.PhysicalPolyhedronBuilder;
import geometry.objects3D.Point3D;
import geometry.objects3D.Vector3D;
import graph.CanvasPanel;
import physical_objects.PhysicalPolyhedron;
import physical_objects.PhysicalSphere;
import physical_objects.Wall;
import shapes.Shape;
import shapes.ShapeReader;
import utils.Tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Space {

    private final ArrayList<PhysicalSphere> spheres;
    private final ArrayList<PhysicalPolyhedron> polyhedrons;
    private final ArrayList<Wall> walls;
    private final double DT;
    private final double G;
    private final CanvasPanel canvas;
    private final PhysicsHandler physicsHandler;

    {
        polyhedrons = new ArrayList<>();
        spheres = new ArrayList<>();
        walls = new ArrayList<>();
        physicsHandler = new PhysicsHandler(this, 1);
    }

    public Space(double dt, double g, CanvasPanel canvas) {
        DT = dt;
        G = g;
        this.canvas = canvas;
        try {
//            spheres.add(new PhysicalSphere(this, new Vector3D(-140, 0, 0), new Vector3D(1, 1, 1), 500, -50, 50, 100, Material.Constantin));
//            spheres.add(new PhysicalSphere(this, new Vector3D(140, 0, 0), new Vector3D(1, 1, 1), -510, -50, 50, 100, Material.Constantin));
            polyhedrons.add(new PhysicalPolyhedron(this, new Vector3D(0, 0, 0), new Vector3D(1, 1, 0.01), getG(),
                    new PhysicalPolyhedronBuilder(new ShapeReader("src\\shapes\\assets\\cube.json").read(), new Point3D(510, 510, 1000)), Material.CONSTANTIN));

        } catch (ImpossibleObjectException | IOException e) {
            e.printStackTrace();
        }
        walls.add(new Wall(this,
                new Point3D(0, 0, 0), new Point3D(10000, 0, 000), new Point3D(0, 10000, 000), Material.GOLD));
    }

    public synchronized void changeTime() {

        long time1 = System.nanoTime();

        try {
            physicsHandler.update();
        } catch (Exception ignored) {
        }

        double cTime = ((System.nanoTime() - time1) / 1000000.0);
        double sleepTime = 0;

        if (DT * 1000.0 - cTime > 0) {
            sleepTime = DT * 1000.0f - cTime;
        }

        try {
            Thread.sleep(Tools.transformdouble(sleepTime));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Collections.shuffle(spheres);
    }

    public void addSphere(Vector3D v, Vector3D w, Vector3D a, double x0, double y0, double z0, double r, Material material) throws ImpossibleObjectException {
        spheres.add(new PhysicalSphere(this, v, w, a, x0, y0, z0, r, material));
    }

    public void addSphere(Vector3D v, Vector3D w, Vector3D a, double x0, double y0, double z0, double r) throws ImpossibleObjectException {
        addSphere(v, w, a, x0, y0, z0, r, Material.CONSTANTIN);
    }

    public void addPolyhedron(Vector3D v, Vector3D w, Vector3D a, PhysicalPolyhedronBuilder builder, Material material) throws ImpossibleObjectException {
        polyhedrons.add(new PhysicalPolyhedron(this, v, w, a, builder, material));
    }

    public void addPolyhedron(Vector3D v, Vector3D w, Vector3D a,  PhysicalPolyhedronBuilder builder) throws ImpossibleObjectException {
        polyhedrons.add(new PhysicalPolyhedron(this, v, w, a, builder, Material.CONSTANTIN));
    }

    public void addPolyhedron(Vector3D v, Vector3D w, Vector3D a, Shape shape, Point3D zero, Material material) throws ImpossibleObjectException {
        polyhedrons.add(new PhysicalPolyhedron(this, v, w, a, new PhysicalPolyhedronBuilder(shape, zero), material));
    }

    public void addPolyhedron(Vector3D v, Vector3D w, Vector3D a, Shape shape, Point3D zero) throws ImpossibleObjectException {
        polyhedrons.add(new PhysicalPolyhedron(this, v, w, a, new PhysicalPolyhedronBuilder(shape, zero), Material.CONSTANTIN));
    }

    public double getDT() {
        return DT;
    }

    public Vector3D getG() {
        return new Vector3D(0,0, -G);
    }

    public ArrayList<PhysicalSphere> getSpheres() {
        return spheres;
    }

    public ArrayList<PhysicalPolyhedron> getPolyhedrons() {
        return polyhedrons;
    }

    public ArrayList<Wall> getWalls() {
        return walls;
    }

    public CanvasPanel getCanvas() {
        return canvas;
    }
}
