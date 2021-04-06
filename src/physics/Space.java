package physics;

import exceptions.ImpossibleObjectException;
import geometry.PhysicalPolyhedronBuilder;
import geometry.objects3D.Point3D;
import geometry.objects3D.Vector3D;
import graph.CanvasPanel;
import physical_objects.*;
import geometry.objects.Shape;
import primitives.Primitive;
import utils.Tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Пространство
 */
public class Space {

    private final ArrayList<PhysicalSphere> spheres;
    private final ArrayList<PhysicalPolyhedron> polyhedrons;
    private final ArrayList<Wall> walls;
    private final ArrayList<GravityPlate> gravityPlates;
    private final double DT;
    private final double G;
    private final CanvasPanel canvas;
    private final PhysicsHandler physicsHandler;

    {
        polyhedrons = new ArrayList<>();
        spheres = new ArrayList<>();
        walls = new ArrayList<>();
        gravityPlates = new ArrayList<>();
        physicsHandler = new PhysicsHandler(this, 1);
    }

    /**
     * Конструктор
     * @param dt временной шаг
     * @param g величина ускорения свободного падения
     * @param canvas канвас, на котором отрисовывается пространство
     */
    public Space(double dt, double g, CanvasPanel canvas) {
        DT = dt;
        G = g;
        this.canvas = canvas;
        try {
            polyhedrons.add(new PhysicalPolyhedron(this, new Vector3D(0, 0, 0), new Vector3D(0.5, 0, 0),
                    new PhysicalPolyhedronBuilder(Primitive.OCTAHEDRON.get(), new Point3D(3500, 410, 1000)), Material.CONSTANTIN));
        } catch (ImpossibleObjectException | IOException e) {
            e.printStackTrace();
        }

        addGravityPlate(this,
                new Point3D(0, 0, 0),
                new Point3D(10000, 0, 0),
                new Point3D(0, 10000, 0),
                new Point3D(10000, 10000, 0),
                500,
                Material.GOLD);
    }

    /**
     * Сделать шаг во времени
     */
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
            Thread.sleep(Tools.transformDouble(sleepTime));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Collections.shuffle(spheres);
    }

    /**
     Метод, добавляющий новую сферу в пространство
     */
    public void addSphere(Vector3D v, Vector3D w, double x0, double y0, double z0, double r, Material material) throws ImpossibleObjectException {
        spheres.add(new PhysicalSphere(this, v, w, x0, y0, z0, r, material));
    }
    /**
     Метод, добавляющий новую сферу в пространство
     */
    public void addSphere(Vector3D v, Vector3D w, double x0, double y0, double z0, double r) throws ImpossibleObjectException {
        addSphere(v, w, x0, y0, z0, r, Material.CONSTANTIN);
    }
    /**
     Метод, добавляющий новый многогранник в пространство
     */
    public void addPolyhedron(Vector3D v, Vector3D w, PhysicalPolyhedronBuilder builder, Material material) throws ImpossibleObjectException {
        polyhedrons.add(new PhysicalPolyhedron(this, v, w, builder, material));
    }

    /**
     Метод, добавляющий новый многогранник в пространство
     */
    public void addPolyhedron(Vector3D v, Vector3D w, PhysicalPolyhedronBuilder builder) throws ImpossibleObjectException {
        polyhedrons.add(new PhysicalPolyhedron(this, v, w, builder, Material.CONSTANTIN));
    }

    /**
     Метод, добавляющий новый многогранник в пространство
     */
    public void addPolyhedron(Vector3D v, Vector3D w, Shape shape, Point3D zero, Material material) throws ImpossibleObjectException {
        polyhedrons.add(new PhysicalPolyhedron(this, v, w, new PhysicalPolyhedronBuilder(shape, zero), material));
    }

    /**
     Метод, добавляющий новый многогранник в пространство
     */
    public void addPolyhedron(Vector3D v, Vector3D w, Shape shape, Point3D zero) throws ImpossibleObjectException {
        polyhedrons.add(new PhysicalPolyhedron(this, v, w, new PhysicalPolyhedronBuilder(shape, zero), Material.CONSTANTIN));
    }
    /**
     Метод, добавляющий новую гравитационную пластину в пространство
     */
    public void addGravityPlate(Space space, Point3D a, Point3D b, Point3D c, Point3D d, double g, Material material) {
        GravityPlate plate = new GravityPlate(space, a, b, c, d, g, material);
        gravityPlates.add(plate);
        walls.add(plate);
    }

    /**
     * @return Временной шаг
     */
    public double getDT() {
        return DT;
    }

    /**
     * @param body тело, для которого нужно найти ускорение свободного падения
     * @return Вектор ускорения свободного падения, действует на данное тело в пространстве
     */
    public Vector3D getG(AbstractBody body) {
        Vector3D g = new Vector3D(0, 0, 0);
        for (GravityPlate plate : gravityPlates)
            g = g.add(plate.getG(body.getPositionOfCentre(false)));
        return g;
    }

    /**
     * @return Сферы в пространстве
     */
    public ArrayList<PhysicalSphere> getSpheres() {
        return spheres;
    }

    /**
     * @return Многогранники в пространстве
     */
    public ArrayList<PhysicalPolyhedron> getPolyhedrons() {
        return polyhedrons;
    }

    /**
     * @return Стены в пространстве
     */
    public ArrayList<Wall> getWalls() {
        return walls;
    }

    /**
     * @return Канвас, на котором отрисовывается пространство
     */
    public CanvasPanel getCanvas() {
        return canvas;
    }
}
