package physics;

import geometry.objects3D.Point3D;
import geometry.objects3D.Vector3D;
import limiters.Collisional;
import org.jetbrains.annotations.NotNull;
import physical_objects.PhysicalSphere;
import physical_objects.Wall;
import utils.Tools;
import utils.TripleMap;

public final class CollisionalPair<FirstThingType extends Collisional, SecondThingType extends Collisional> {

    private final FirstThingType firstThing;
    private final SecondThingType secondThing;
    private final static TripleMap<Class, Class, Collider> methodsMap;

    public CollisionalPair(@NotNull FirstThingType firstThing, @NotNull SecondThingType secondThing) {
        this.firstThing = firstThing;
        this.secondThing = secondThing;
    }


    static {
        methodsMap = new TripleMap<>();

        methodsMap.addFirstKey(PhysicalSphere.class);
        methodsMap.addFirstKey(Wall.class);

        methodsMap.putByFirstKey(PhysicalSphere.class, PhysicalSphere.class, CollisionalPair::sphereToSphere);
        methodsMap.putByFirstKey(PhysicalSphere.class, Wall.class, CollisionalPair::sphereToWall);

        methodsMap.putByFirstKey(Wall.class, PhysicalSphere.class, CollisionalPair::sphereToWall);
    }


    public void collide() {
        methodsMap.getElement(firstThing.getClass(), secondThing.getClass()).collide(firstThing, secondThing);
    }

    /**
     * This method processes collision between two <b>Spheres</b>;
     * changes their velocity
     *
     * @param thing1 - first sphere
     * @param thing2 - second sphere
     */

    private static void sphereToSphere(Collisional thing1, Collisional thing2) {
        PhysicalSphere sphere1 = (PhysicalSphere) thing1;
        PhysicalSphere sphere2 = (PhysicalSphere) thing2;

        final Point3D firstSpherePos = sphere1.getPosition(false);
        final Point3D secondSpherePos = sphere2.getPosition(false);
        final Vector3D axisX = new Vector3D(firstSpherePos.x - secondSpherePos.x,
                firstSpherePos.y - secondSpherePos.y, firstSpherePos.z - secondSpherePos.z);
        final float axisXLen = (float) axisX.getLength();

        final float m1 = sphere1.getM();
        final float m2 = sphere2.getM();
        final float ratio = m1 / m2;
        final float k = Tools.countAverage(sphere1.getMaterial().coefOfReduction, sphere2.getMaterial().coefOfReduction);
        final float fr = Tools.countAverage(sphere1.getMaterial().coefOfFriction, sphere2.getMaterial().coefOfFriction);

        final float v1x = (float) (sphere1.getV().scalarProduct(axisX) / axisXLen);
        final float v2x = (float) (sphere2.getV().scalarProduct(axisX) / axisXLen);
        final float u1x = ((ratio - k) * v1x + (k + 1) * v2x) / (ratio + 1);
        final float u2x = ((ratio * (k + 1)) * v1x + (1 - k * ratio) * v2x) / (ratio + 1);
        System.out.println(v2x);
        System.out.println(v1x);
        final float s = (m1 * m2) / (m1 + m2) * (1f + k) * Math.abs(v1x - v2x);
        System.out.println(s / m1);

        sphere1.applyStrikeImpulse(axisX.multiply(m1 * (u1x - v1x) / axisXLen));

        sphere2.applyStrikeImpulse(axisX.multiply(m2 * (u2x - v2x) / axisXLen));


        final float r1 = sphere1.getR();
        final float r2 = sphere2.getR();
    }

    /**
     *This method processes collision between <b>Sphere</b> and <b>Wall</b>;
     * changes <b>Sphere's</b> velocity
     * @param thing1 - sphere or wall (PhysicalSphere or Wall)
     * @param thing2 - sphere or wall (PhysicalSphere or Wall)
     */

    private static void sphereToWall(Collisional thing1, Collisional thing2) {
        PhysicalSphere sphere;
        Wall wall;

        if (thing1 instanceof PhysicalSphere) {
            sphere = (PhysicalSphere) thing1;
            wall = (Wall) thing2;
        } else {
            wall = (Wall) thing1;
            sphere = (PhysicalSphere) thing2;
        }

        final float k = Tools.countAverage(sphere.getMaterial().coefOfReduction, wall.getMaterial().coefOfReduction);
        final float fr = Tools.countAverage(sphere.getMaterial().coefOfFriction, wall.getMaterial().coefOfFriction);

        Vector3D axisY = wall.getPlane().vector;
        if (sphere.getV().scalarProduct(axisY) / axisY.getLength() > 0)
            axisY = new Vector3D(-axisY.x, -axisY.y, -axisY.z);
        final float axisYLen = (float) axisY.getLength();


        final float vy = (float) Math.abs(sphere.getV().scalarProduct(axisY) / axisY.getLength());

        final float s = (1f + k) * sphere.getM() * vy;

        sphere.applyStrikeImpulse(axisY.multiply(s / axisYLen));

        System.out.println(s / sphere.getM());
        System.out.println(vy);
    }
}
