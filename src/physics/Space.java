package physics;

import geometry.objects3D.Vector3D;
import graph.CanvasPanel;
import physical_objects.PhysicalSphere;
import utils.Tools;

import java.util.ArrayList;
import java.util.Collections;

public class Space {
    private final ArrayList<PhysicalSphere> spheres;
    private final float DT;
    private final float G;
    private final CanvasPanel canvas;
    private final PhysicsHandler physicsHandler;

    {
        spheres = new ArrayList<>();
        physicsHandler = new PhysicsHandler(this, 1);
    }

    public Space(float dt, float g, CanvasPanel canvas) {
        DT = dt;
        G = g;
        this.canvas = canvas;
        spheres.add(new PhysicalSphere(this, new Vector3D(0.01, 0.01, 0.01), new Vector3D(1, 1, 1), -90, -50, -100, 100, Material.Constantin));
        spheres.add(new PhysicalSphere(this, new Vector3D(0.01, 0.01, 0.01), new Vector3D(1, 1, 1), +90, -50, -100, 100, Material.Constantin));
    }

    public synchronized void changeTime() {

        long time1 = System.nanoTime();

        try {
            physicsHandler.update();
        } catch (Exception ignored) {
        }

        float cTime = (float) ((System.nanoTime() - time1) / 1000000.0);
        float sleepTime = 0;

        if (DT * 1000.0 - cTime > 0) {
            sleepTime = DT * 1000.0f - cTime;
        }

        try {
            Thread.sleep(Tools.transformFloat(sleepTime));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Collections.shuffle(spheres);
    }

    public float getDT() {
        return DT;
    }

    public float getG() {
        return G;
    }

    public ArrayList<PhysicalSphere> getSpheres() {
        return spheres;
    }

    public CanvasPanel getCanvas() {
        return canvas;
    }
}
