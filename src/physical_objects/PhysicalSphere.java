package physical_objects;

import drawing.Drawable;
import geometry.objects3D.Point3D;
import geometry.objects3D.Vector3D;
import geometry.polygonal.Sphere;
import graph.Canvas;
import graph.CanvasPanel;
import limiters.Collisional;
import limiters.Intersectional;
import physics.Material;
import physics.Space;

public class PhysicalSphere implements Drawable, Intersectional, Collisional {

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
        this.m = (4 * (float) Math.PI * r * r * r / 3f) * material.p;
        J = 0.4f * m * r * r;
        drawableInterpretation = new Sphere(new Point3D(x0, y0, z0), r, 15, material.fillColor);
        pushToCanvas(space.getCanvas());
    }

    public synchronized void update() {
        changeSpeed();
        x0 += v.x * space.getDT();
        y0 += v.y * space.getDT();
        z0 -= (v.z + v.z - space.getG() * space.getDT()) * space.getDT() / 2.0f;
        updateDrawingInterpretation();
    }

    private synchronized void changeSpeed() {
        v = new Vector3D(v.x, v.y, v.z + space.getG() * space.getDT());
    }

    public synchronized Point3D getPosition(boolean mode) {
        float m = mode ? 1.0f : 0.0f;
        return new Point3D(x0 + m * v.x * space.getDT(),
                y0 + m * v.y * space.getDT(),
                z0 - m * ((v.z + v.z + space.getG() * space.getDT()) * space.getDT() / 2.0f));
    }

    public synchronized void applyStrikeImpulse(Vector3D s) {
        s = s.multiply(1 / m);
        v = new Vector3D(v.x + s.x, v.y + s.y, v.z + s.z);
    }

    public float getR() {
        return r;
    }

    public void setV(Vector3D v) {
        this.v = v;
    }

    public void setW(Vector3D w) {
        this.w = w;
    }

    public float getM() {
        return m;
    }

    public Material getMaterial() {
        return material;
    }

    public Vector3D getV() {
        return v;
    }

    @Override
    public void pushToCanvas(CanvasPanel canvas) {
        canvas.getPolygonals().add(drawableInterpretation);
    }

    @Override
    public void updateDrawingInterpretation() {
        drawableInterpretation.setCenter(new Point3D(x0, y0, z0));
    }
}
