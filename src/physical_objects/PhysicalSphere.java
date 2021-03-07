package physical_objects;

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

public class PhysicalSphere extends AbstractBody implements Intersectional, Collisional {

    private final double r;
    private final double J;
    private final Sphere drawableInterpretation;

    public PhysicalSphere(Space space, Vector3D v, Vector3D w, double x0, double y0, double z0, double r, Material material) throws ImpossibleObjectException {
        super(space, x0, y0, z0, v, w, material, (4 * Math.PI * r * r * r / 3d) * material.p);
        this.r = r;
        J = 0.4d * m * r * r;
        drawableInterpretation = new Sphere(new Point3D(x0, y0, z0), r, 15, material.fillColor);
        pushToCanvas(space.getCanvas());
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

    public double getJ() {
        return J;
    }

    @Override
    public void pushToCanvas(CanvasPanel canvas) {
        canvas.getPolygonals().add(drawableInterpretation);
    }

    @Override
    public void updateDrawingInterpretation() {
        drawableInterpretation.setCenter(new Point3D(x0, y0, z0));
        drawableInterpretation.rotate(w.multiply(space.getDT()), getPositionOfCentre(false));
    }
}
