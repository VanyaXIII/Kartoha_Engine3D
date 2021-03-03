package geometry;

import geometry.objects3D.Point3D;
import geometry.objects3D.Vector3D;
import limiters.Intersectional;
import org.jetbrains.annotations.NotNull;
import physical_objects.PhysicalSphere;
import physical_objects.Wall;
import utils.TripleMap;

/**
 * @param <FirstThingType> - type of first thing, that can be intersected with others
 * @param <SecondThingType> - type of second thing, that can be intersected with others
 * @author Ivan
 */

public final class IntersectionalPair<FirstThingType extends Intersectional, SecondThingType extends Intersectional> {

    private final FirstThingType firstThing;
    private final SecondThingType secondThing;
    private final static boolean dynamicCollisionMode = true;
    private final static boolean staticCollisionMode = false;
    private final static TripleMap<Class, Class, Intersecter> methodsMap;

    public IntersectionalPair(@NotNull FirstThingType firstThing, @NotNull SecondThingType secondThing) {
        this.firstThing = firstThing;
        this.secondThing = secondThing;
    }

    static {
        methodsMap = new TripleMap<>();

        methodsMap.addFirstKey(PhysicalSphere.class);
        methodsMap.addFirstKey(Wall.class);

        methodsMap.putByFirstKey(PhysicalSphere.class, PhysicalSphere.class, IntersectionalPair::sphereToSphere);
        methodsMap.putByFirstKey(PhysicalSphere.class, Wall.class, IntersectionalPair::sphereToWall);

        methodsMap.putByFirstKey(Wall.class, PhysicalSphere.class, IntersectionalPair::sphereToWall);
    }

    public boolean areIntersected() {
        return methodsMap.getElement(firstThing.getClass(), secondThing.getClass()).areIntersected(firstThing, secondThing);
    }

    /**
     * @param thing1 - first thing to check intersection
     * @param thing2 - second thing to check intersection
     * @return - boolean statement(are things intersected)
     */

    private static boolean sphereToSphere(Intersectional thing1, Intersectional thing2) {

        PhysicalSphere sphere1 = (PhysicalSphere) thing1;
        PhysicalSphere sphere2 = (PhysicalSphere) thing2;

        if (!new AABB(sphere1, dynamicCollisionMode).isIntersectedWith(new AABB(sphere2, dynamicCollisionMode)))
            return false;

        Point3D sphere1Pos = sphere1.getPositionOfCentre(dynamicCollisionMode);
        Point3D sphere2Pos = sphere2.getPositionOfCentre(dynamicCollisionMode);

        Vector3D distanceVector = new Vector3D(sphere1Pos, sphere2Pos);
        if (distanceVector.getLength() <= (sphere1.getR() + sphere2.getR())) {
            return !sphere2.equals(sphere1);
        }

        return false;
    }

    private static boolean sphereToWall(Intersectional thing1, Intersectional thing2) {

        PhysicalSphere sphere;
        Wall wall;

        if (thing1 instanceof PhysicalSphere) {
            sphere = (PhysicalSphere) thing1;
            wall = (Wall) thing2;
        } else {
            sphere = (PhysicalSphere) thing2;
            wall = (Wall) thing1;
        }


        if (!new AABB(sphere, dynamicCollisionMode).isIntersectedWith(new AABB(wall)))
            return false;

        final Point3D spherePos = sphere.getPositionOfCentre(dynamicCollisionMode);
        final float distance = (float) wall.getPlane().distance(spherePos);

        if (distance <= sphere.getR()) {
            Vector3D normalVector = wall.getPlane().vector;
            Point3D intersectionPoint = normalVector.multiply(-distance / normalVector.getLength()).addToPoint(spherePos);

            if (new AABB(wall).isPointIn(intersectionPoint))
                return true;
            else {
                for (Point3D point : wall.getPoints())
                    if (new Vector3D(point, spherePos).getLength() <= sphere.getR())
                        return true;
            }
        }

        return false;
    }
}
