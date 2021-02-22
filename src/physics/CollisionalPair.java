package physics;

import geometry.objects3D.Point3D;
import geometry.objects3D.Vector3D;
import limiters.Collisional;
import org.jetbrains.annotations.NotNull;
import physical_objects.PhysicalSphere;
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

        methodsMap.putByFirstKey(PhysicalSphere.class, PhysicalSphere.class, CollisionalPair::sphereToSphere);
    }


    public void collide() {
        methodsMap.getElement(firstThing.getClass(), secondThing.getClass()).collide(firstThing, secondThing);
    }

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
        System.out.println(v2x);
        System.out.println(v1x);
        final float s = (m1 * m2) / (m1 + m2) * (1f + k) * Math.abs(v1x - v2x);
        System.out.println(s / m1);

        sphere1.applyStrikeImpulse(new Vector3D(
                axisX.x * s / axisXLen,
                axisX.y * s / axisXLen,
                axisX.z * s / axisXLen));

        sphere2.applyStrikeImpulse(new Vector3D(
                -axisX.x * s / axisXLen,
                -axisX.y * s / axisXLen,
                -axisX.z * s / axisXLen));



        final float r1 = sphere1.getR();
        final float r2 = sphere2.getR();
    }
}
