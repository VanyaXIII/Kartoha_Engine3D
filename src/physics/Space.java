package physics;

import drawing.Drawable;
import exceptions.ImpossibleObjectException;
import geometry.objects3D.Point3D;
import geometry.objects3D.Vector3D;
import graph.CanvasPanel;
import physical_objects.PhysicalSphere;
import physical_objects.Wall;
import utils.Tools;

import java.util.ArrayList;
import java.util.Collections;

public class Space {
    private final ArrayList<PhysicalSphere> spheres;
    private final ArrayList<Wall> walls;
    private final double DT;
    private final double G;
    private final CanvasPanel canvas;
    private final PhysicsHandler physicsHandler;

    {
        spheres = new ArrayList<>();
        walls = new ArrayList<>();
        physicsHandler = new PhysicsHandler(this, 1);
    }

    public Space(double dt, double g, CanvasPanel canvas) {
        DT = dt;
        G = g;
        this.canvas = canvas;
        try {
            spheres.add(new PhysicalSphere(this, new Vector3D(-40, 0, 0), new Vector3D(1, 1, 1), 500, -50, 50, 100, Material.Constantin));
            spheres.add(new PhysicalSphere(this, new Vector3D(40, 0, 0), new Vector3D(1, 1, 1), -510, -50, 50, 100, Material.Constantin));
        } catch (ImpossibleObjectException e) {
            e.printStackTrace();
        }
//        walls.add(new Wall(this,
//           new Point3D(0,0,0), new Point3D(10000, 0, 0), new Point3D(0, 10000, 0), Material.Gold;
    }

    public synchronized void changeTime() {

        long time1 = System.nanoTime();

        try {
            physicsHandler.update();
        } catch (Exception ignored) {
        }

        double cTime = (double) ((System.nanoTime() - time1) / 1000000.0);
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

    public double getDT() {
        return DT;
    }

    public double getG() {
        return G;
    }

    public ArrayList<PhysicalSphere> getSpheres() {
        return spheres;
    }

    public ArrayList<Wall> getWalls() {
        return walls;
    }

    public CanvasPanel getCanvas() {
        return canvas;
    }
}
