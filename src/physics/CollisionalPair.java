package physics;

import geometry.objects3D.Plane3D;
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


    /**
     * Main method that dispatches all collisions
     */

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

        final Point3D firstSpherePos = sphere1.getPositionOfCentre(true);
        final Point3D secondSpherePos = sphere2.getPositionOfCentre(true);
        final Vector3D axisX = new Vector3D(firstSpherePos.x - secondSpherePos.x,
                firstSpherePos.y - secondSpherePos.y, firstSpherePos.z - secondSpherePos.z);
        final double axisXLen = axisX.getLength();

        final double m1 = sphere1.getM();
        final double m2 = sphere2.getM();
        final double ratio = m1 / m2;
        final double k = Tools.countAverage(sphere1.getMaterial().coefOfReduction, sphere2.getMaterial().coefOfReduction);
        final double fr = Tools.countAverage(sphere1.getMaterial().coefOfFriction, sphere2.getMaterial().coefOfFriction);

        final double r1 = sphere1.getR();
        final double r2 = sphere2.getR();

        final double v1x =  (sphere1.getV().scalarProduct(axisX) / axisXLen);
        final double v2x =  (sphere2.getV().scalarProduct(axisX) / axisXLen);
        final double u1x = ((ratio - k) * v1x + (k + 1) * v2x) / (ratio + 1);
        final double u2x = ((ratio * (k + 1)) * v1x + (1 - k * ratio) * v2x) / (ratio + 1);
        final double s = (m1 * m2) / (m1 + m2) * (1f + k) * Math.abs(v1x - v2x);

        final Plane3D frictionPlane = new Plane3D(axisX, firstSpherePos);

        final Point3D collisionPoint1 = axisX.multiply(-r1 / axisXLen).addToPoint(sphere1.getPositionOfCentre(true));
        final Point3D collisionPoint2 = axisX.multiply(r2 / axisXLen).addToPoint(sphere2.getPositionOfCentre(true));

        final Vector3D vel1 = Tools.calcProjectionOfVectorOnPlane(sphere1.getVelOfPoint(collisionPoint1, true), frictionPlane);
        final Vector3D vel2 = Tools.calcProjectionOfVectorOnPlane(sphere2.getVelOfPoint(collisionPoint2, true), frictionPlane);

        Vector3D relativeVel1 = vel1.subtract(vel2);
        Vector3D relativeVel2 = vel2.subtract(vel1);

        Vector3D firstSphereFriction1 = relativeVel2.multiply(fr * s / relativeVel2.getLength());
        Vector3D firstSphereFriction2 = relativeVel2.multiply(m1 * m2 / (3.5 * (m1+m2)));

        if (firstSphereFriction1.getLength() < firstSphereFriction2.getLength()){
            System.out.println(1234);
            sphere1.applyFriction(collisionPoint1, firstSphereFriction1.multiply(-1));
            sphere2.applyFriction(collisionPoint1, firstSphereFriction1);
        }

        else {
            sphere1.applyFriction(collisionPoint1, firstSphereFriction2);
            sphere2.applyFriction(collisionPoint2, firstSphereFriction2.multiply(-1));
        }




        sphere1.applyStrikeImpulse(axisX.multiply(m1 * (u1x - v1x) / axisXLen));
        sphere2.applyStrikeImpulse(axisX.multiply(m2 * (u2x - v2x) / axisXLen));
    }

    /**
     * This method processes collision between <b>Sphere</b> and <b>Wall</b>;
     * changes <b>Sphere's</b> velocity
     *
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

        final double k = Tools.countAverage(sphere.getMaterial().coefOfReduction, wall.getMaterial().coefOfReduction);
        final double fr = Tools.countAverage(sphere.getMaterial().coefOfFriction, wall.getMaterial().coefOfFriction);
        final double m = sphere.getM();
        final double J = sphere.getJ();

        Vector3D axisY = wall.getPlane().vector;

        if (sphere.getV().scalarProduct(axisY) > 0)
            axisY = axisY.multiply(-1);

        final double axisYLen = axisY.getLength();

        final Point3D collisionPoint = axisY.multiply(-sphere.getR() / axisYLen).addToPoint(sphere.getPositionOfCentre(true));

        final double vy = (double) Math.abs(sphere.getV().scalarProduct(axisY) / axisY.getLength());
        final double s = (1f + k) * sphere.getM() * vy;


        Vector3D v = Tools.calcProjectionOfVectorOnPlane(sphere.getV(), wall.getPlane());
        Vector3D angularVel = sphere.getAngularVelOfPoint(collisionPoint, true);

        final Vector3D velOfCollisionPoint = v.add(angularVel);

        System.out.println(sphere.getVelOfPoint(collisionPoint, true).getLength());

        System.out.println(v.add(angularVel));


        Vector3D frictionImpulse1 = velOfCollisionPoint.multiply(-fr * Math.abs(s) / velOfCollisionPoint.getLength());
        Vector3D frictionImpulse2 = velOfCollisionPoint.multiply(-m / 3.5d);

        if (frictionImpulse1.getLength() < frictionImpulse2.getLength()) {
            sphere.applyFriction(collisionPoint, frictionImpulse1);
        } else {
            sphere.applyFriction(collisionPoint, frictionImpulse2);
        }


        sphere.applyStrikeImpulse(axisY.multiply(s / axisYLen));


    }
}
