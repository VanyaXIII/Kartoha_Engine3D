package physics;

import geometry.objects3D.Vector3D;
import graph.CanvasPanel;
import physical_objects.PhysicalSphere;

import java.util.ArrayList;

public class Space {
    private final ArrayList<PhysicalSphere> spheres;
    private final float DT;
    private final float G;
    private final CanvasPanel canvas;

    {
        spheres = new ArrayList<>();
    }

    public Space(float dt, float g, CanvasPanel canvas) {
        DT = dt;
        G = g;
        this.canvas = canvas;
        spheres.add(new PhysicalSphere(this, new Vector3D(1,1,1), new Vector3D(1, 1 ,1), -50, -50, -100, 100, Material.Constantin));
    }

    public float getDT() {
        return DT;
    }

    public float getG() {
        return G;
    }

    public CanvasPanel getCanvas() {
        return canvas;
    }
}
