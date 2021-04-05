package physical_objects;

import exceptions.ImpossibleObjectException;
import geometry.intersections.SphereToPlaneIntersection;
import geometry.intersections.SpheresIntersection;
import geometry.objects3D.Point3D;
import geometry.objects3D.Vector3D;
import geometry.polygonal.Sphere;
import graph.CanvasPanel;
import limiters.Collisional;
import limiters.Intersectional;
import physics.Material;
import physics.Space;

/**
 * Физичная сфера
 */
public class PhysicalSphere extends AbstractBody implements Intersectional, Collisional {

    private final double r;
    private final double J;
    private final Sphere drawableInterpretation;

    /**
     * Конструктор
     * @param space пространство, в котором находится сфера
     * @param v начальная скорость сферы
     * @param w начальная угловая скорость сферы
     * @param x0 начальная коордианата по Ox
     * @param y0 начальная коордианата по Oy
     * @param z0 начальная коордианата по Oz
     * @param r величина радиуса сферы
     * @param material материал, из которого сделана сфера
     * @throws ImpossibleObjectException исключение в случае попытки создания сферы с нулевой массой
     */
    public PhysicalSphere(Space space, Vector3D v, Vector3D w, double x0, double y0, double z0, double r, Material material) throws ImpossibleObjectException {
        super(space, x0, y0, z0, v, w, material, (4 * Math.PI * r * r * r / 3d) * material.p);
        this.r = r;
        J = 0.4d * m * r * r;
        drawableInterpretation = new Sphere(new Point3D(x0, y0, z0), r, 15, material.fillColor);
        pushToCanvas(space.getCanvas());
    }

    /**
     * Метод, реалищующий смещение сферы от сферы
     * @param intersection пересечение сферы
     */
    public synchronized void pullFromSphere(SpheresIntersection intersection) {
        if (intersection.getValue() != 0) {
            Point3D nCords = intersection.getCentralLine().normalize()
                    .multiply(intersection.getValue())
                    .addToPoint(getPositionOfCentre(false));
            this.x0 = nCords.x;
            this.y0 = nCords.y;
            this.z0 = nCords.z;
            drawableInterpretation.setCenter(getPositionOfCentre(false));
        }
    }

    /**
     * Метод, реалищующий смещение сферы от плоскости
     * @param intersection пересечение сферы и плоскости
     */
    public synchronized void pullFromPlane(SphereToPlaneIntersection intersection) {
        if (intersection.getValue() != 0) {
            Vector3D movementVector = new Vector3D(intersection.getIntersectionPoint(), getPositionOfCentre(false));
            Point3D nCords = movementVector.normalize()
                    .multiply(intersection.getValue())
                    .addToPoint(getPositionOfCentre(false));
            this.x0 = nCords.x;
            this.y0 = nCords.y;
            this.z0 = nCords.z;
            drawableInterpretation.setCenter(getPositionOfCentre(false));
        }
    }

    /**
     * Метод, реализующий приложение ударного импульса
     * @param impulse импульс силы
     */
    public synchronized void applyStrikeImpulse(Vector3D impulse) {
        v = v.add(impulse.multiply(1d / m));
    }

    /**
     * Метод, реализующий приложение импульса силы трения
     * @param applicationPoint точка приложения
     * @param impulse вектор импульса
     */
    public synchronized void applyFriction(Point3D applicationPoint, Vector3D impulse) {
        applyStrikeImpulse(impulse);
        Vector3D radVector = new Vector3D(getPositionOfCentre(false), applicationPoint);
        radVector.multiply(r / radVector.getLength());
        w = w.add(radVector.vectorProduct(impulse).multiply(1d / J));
    }


    /**
     * @param point точка
     * @param mode считать ли относительно будущего положения
     * @return Скорость вращения данной точки
     */
    public Vector3D getRotationVelOfPoint(Point3D point, boolean mode) {
        Vector3D radVector = new Vector3D(getPositionOfCentre(mode), point);
        radVector = radVector.multiply(r / radVector.getLength());
        return w.vectorProduct(radVector);
    }

    /**
     *
     * @param point точка
     * @param mode считать ли относительно будущего положения
     * @return Полная скорость данной точки
     */
    public Vector3D getVelOfPoint(Point3D point, boolean mode) {
        return getRotationVelOfPoint(point, mode).add(v);
    }

    /**
     * @return Величина радиуса сферы
     */
    public synchronized double getR() {
        return r;
    }

    /**
     * @return Момент инерции сферы
     */
    public double getJ() {
        return J;
    }

    /**
     * Метод, добавляющий сферу на канвас, на котором ее нужно отрисовать
     * @param canvas канвас, на котором нужном отрисовывать многогранник
     */
    @Override
    public void pushToCanvas(CanvasPanel canvas) {
        canvas.getPolygonals().add(drawableInterpretation);
    }

    /**
     * Метод, обновляющий графическую интерпритацию сферы
     */
    @Override
    public void updateDrawingInterpretation() {
        drawableInterpretation.setCenter(getPositionOfCentre(false));
        drawableInterpretation.rotate(w.multiply(space.getDT()), getPositionOfCentre(false));
    }
}
