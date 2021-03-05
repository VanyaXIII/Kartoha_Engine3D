package physical_objects;

import com.google.gson.Gson;
import drawing.Drawable;
import exceptions.ImpossibleObjectException;
import geometry.objects3D.Point3D;
import geometry.objects3D.Vector3D;
import geometry.polygonal.Sphere;
import graph.CanvasPanel;
import limiters.Collisional;
import limiters.Intersectional;
import physics.Material;
import physics.Space;

public class PhysicalSphere implements Drawable, Intersectional, Collisional {

    private double x0, y0, z0;
    private final double r;
    private Vector3D v;
    private Vector3D w;
    private final double J;
    private final Space space;
    private final Material material;
    private final double m;
    private final Sphere drawableInterpretation;

    public PhysicalSphere(Space space, Vector3D v, Vector3D w, double x0, double y0, double z0, double r, Material material) throws ImpossibleObjectException {
        this.x0 = x0;
        this.r = r;
        this.y0 = y0;
        this.z0 = z0;
        this.space = space;
        this.v = v;
        this.w = w;
        this.material = material;
        this.m = (4 * Math.PI * r * r * r / 3f) * material.p;
        J = 0.4f * m * r * r;
        drawableInterpretation = new Sphere(new Point3D(x0, y0, z0), r, 15, material.fillColor);
        pushToCanvas(space.getCanvas());
        if (m <= 0) throw new ImpossibleObjectException("Impossible sphere; mass is null");
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

    public synchronized Point3D getPositionOfCentre(boolean mode) {
        double m = mode ? 1.0f : 0.0f;
        return new Point3D(x0 + m * v.x * space.getDT(),
                y0 + m * v.y * space.getDT(),
                z0 - m * ((v.z + v.z + space.getG() * space.getDT()) * space.getDT() / 2.0f));
    }

    public synchronized void applyStrikeImpulse(Vector3D impulse) {
        v = v.add(impulse.multiply(1 / m));
    }

    public synchronized void applyFriction(Point3D applicationPoint, Vector3D impulse) {
        applyStrikeImpulse(impulse);
        Vector3D radVector = new Vector3D(getPositionOfCentre(false), applicationPoint);
        radVector.multiply(r / radVector.getLength());
        w = w.add(radVector.vectorProduct(impulse).multiply(1 / J));
    }

    public Vector3D getAngularVelOfPoint(Point3D point, boolean mode) {
        Vector3D radVector = new Vector3D(getPositionOfCentre(mode), point);
        radVector = radVector.multiply(r / radVector.getLength());
        return w.vectorProduct(radVector);
    }

    public Vector3D getVelOfPoint(Point3D point, boolean mode) {
        return getAngularVelOfPoint(point, mode).add(v);
    }

    public synchronized double getR() {
        return r;
    }

    public synchronized void setV(Vector3D v) {
        this.v = v;
    }

    public synchronized void setW(Vector3D w) {
        this.w = w;
    }

    public synchronized double getM() {
        return m;
    }

    public synchronized double getJ() {
        return J;
    }

    public synchronized Material getMaterial() {
        return material;
    }

    public synchronized Vector3D getV() {
        return v;
    }

    @Override
    public void pushToCanvas(CanvasPanel canvas) {
        canvas.getPolygonals().add(drawableInterpretation);
    }

    @Override
    public void updateDrawingInterpretation() {
        drawableInterpretation.setCenter(new Point3D(x0, y0, z0));

//        drawableInterpretation.rotate(w.multiply(space.getDT()), getPositionOfCentre(false));
    }
}
