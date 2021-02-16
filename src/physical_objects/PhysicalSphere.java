package physical_objects;

import drawing.Drawable;
import geometry.objects3D.Point3D;
import geometry.objects3D.Vector3D;
import geometry.polygonal.Sphere;
import graph.Canvas;
import graph.CanvasPanel;
import physics.Material;
import physics.Space;

public class PhysicalSphere implements Drawable {

    private float x0, y0, z0;
    private final float r;
    private Vector3D v;
    private Vector3D w;
    private final float J;
    private final Space space;
    private final Material material;
    private final float m;
    private final Sphere drawableInterpretation;

    public PhysicalSphere(Space space, Vector3D v, Vector3D w, float x0, float y0, float z0, float r, Material material) {
        this.x0 = x0;
        this.r = r;
        this.y0 = y0;
        this.z0 = z0;
        this.space = space;
        this.v = v;
        this.w = w;
        this.material = material;
        this.m = (4 * (float)Math.PI * r * r * r / 3f) * material.p;
        J = 0.4f * m * r * r;
        drawableInterpretation = new Sphere(new Point3D(x0, y0, z0), r, 15, material.fillColor);
        pushToCanvas();
    }

    @Override
    public void pushToCanvas() {
        space.getCanvas().getPolygonals().add(drawableInterpretation);
    }


}
