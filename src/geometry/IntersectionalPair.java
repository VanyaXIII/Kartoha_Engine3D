package geometry;

import exceptions.ImpossiblePairException;
import geometry.objects3D.Line3D;
import geometry.objects3D.Plane3D;
import geometry.objects3D.Point3D;
import geometry.objects3D.Vector3D;
import limiters.Intersectional;
import org.jetbrains.annotations.NotNull;
import physical_objects.PhysicalPolyhedron;
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

    public IntersectionalPair(@NotNull FirstThingType firstThing, @NotNull SecondThingType secondThing) throws ImpossiblePairException {
        this.firstThing = firstThing;
        this.secondThing = secondThing;
        if (firstThing instanceof Wall && secondThing instanceof Wall)
            throw new ImpossiblePairException("Trying to check wall to wall intersection");
    }

    static {
        methodsMap = new TripleMap<>();

        methodsMap.addFirstKey(PhysicalSphere.class);
        methodsMap.addFirstKey(Triangle.class);
        methodsMap.addFirstKey(PhysicalPolyhedron.class);

        methodsMap.putByFirstKey(PhysicalSphere.class, PhysicalSphere.class, IntersectionalPair::sphereToSphere);
        methodsMap.putByFirstKey(PhysicalSphere.class, Triangle.class, IntersectionalPair::sphereToTriangle);
        methodsMap.putByFirstKey(PhysicalSphere.class, PhysicalPolyhedron.class, IntersectionalPair::sphereToPolyhedron);

        methodsMap.putByFirstKey(Triangle.class, PhysicalSphere.class, IntersectionalPair::sphereToTriangle);
        methodsMap.putByFirstKey(Triangle.class, PhysicalPolyhedron.class, IntersectionalPair::polyhedronToTriangle);

        methodsMap.putByFirstKey(PhysicalPolyhedron.class, Triangle.class, IntersectionalPair::polyhedronToTriangle);
        methodsMap.putByFirstKey(PhysicalPolyhedron.class, PhysicalSphere.class, IntersectionalPair::sphereToPolyhedron);
        methodsMap.putByFirstKey(PhysicalPolyhedron.class, PhysicalPolyhedron.class, IntersectionalPair::polyhedronToPolyhedron);
    }

    public boolean areIntersected() {
        return methodsMap.getElement(firstThing.getClass(), secondThing.getClass()).areIntersected(firstThing, secondThing);
    }

    private static boolean polyhedronToPolyhedron(Intersectional thing1, Intersectional thing2) {
        PhysicalPolyhedron polyhedron1 = (PhysicalPolyhedron) thing1;
        PhysicalPolyhedron polyhedron2 = (PhysicalPolyhedron) thing2;

        if (!new AABB(polyhedron1, dynamicCollisionMode).isIntersectedWith(new AABB(polyhedron2, dynamicCollisionMode)))
            return false;

        for (Segment segment : polyhedron1.getSegments(dynamicCollisionMode))
            for (Triangle triangle : polyhedron2.getTriangles(dynamicCollisionMode))
                if (triangle.isIntersectedWithSegment(segment))
                    return true;

        for (Segment segment : polyhedron2.getSegments(dynamicCollisionMode))
            for (Triangle triangle : polyhedron1.getTriangles(dynamicCollisionMode))
                if (triangle.isIntersectedWithSegment(segment))
                    return true;


        return false;
    }

    private static boolean polyhedronToTriangle(Intersectional thing1, Intersectional thing2) {
        PhysicalPolyhedron polyhedron;
        Triangle triangle;

        if (thing1 instanceof PhysicalPolyhedron) {
            polyhedron = (PhysicalPolyhedron) thing1;
            triangle = (Triangle) thing2;
        } else {
            polyhedron = (PhysicalPolyhedron) thing2;
            triangle = (Triangle) thing1;
        }

        if (!new AABB(polyhedron, dynamicCollisionMode).isIntersectedWith(new AABB(triangle)))
            return false;

        boolean intersected = false;

        for (Point3D point : polyhedron.getPoints(dynamicCollisionMode))
            if (triangle.isIntersectedWithSegment(new Segment(point, polyhedron.getPositionOfCentre(dynamicCollisionMode))))
                intersected = true;

        return intersected;
    }

    private static boolean sphereToPolyhedron(Intersectional thing1, Intersectional thing2) {
        PhysicalSphere sphere;
        PhysicalPolyhedron polyhedron;

        if (thing1 instanceof PhysicalSphere) {
            sphere = (PhysicalSphere) thing1;
            polyhedron = (PhysicalPolyhedron) thing2;
        } else {
            sphere = (PhysicalSphere) thing2;
            polyhedron = (PhysicalPolyhedron) thing1;
        }

        if (!new AABB(polyhedron, dynamicCollisionMode).isIntersectedWith(new AABB(sphere, dynamicCollisionMode)))
            return false;

        boolean intersected = false;
        for (Triangle triangle : polyhedron.getTriangles(dynamicCollisionMode)) {
            try {
                if (new IntersectionalPair<>(sphere, triangle).areIntersected()) {
                    intersected = true;
                    break;
                }

            } catch (ImpossiblePairException e) {
                e.printStackTrace();
            }
        }
        return intersected;
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

    private static boolean sphereToTriangle(Intersectional thing1, Intersectional thing2) {

        PhysicalSphere sphere;
        Triangle triangle;

        if (thing1 instanceof PhysicalSphere) {
            sphere = (PhysicalSphere) thing1;
            triangle = (Triangle) thing2;
        } else {
            sphere = (PhysicalSphere) thing2;
            triangle = (Triangle) thing1;
        }


        if (!new AABB(sphere, dynamicCollisionMode).isIntersectedWith(new AABB(triangle)))
            return false;

        final Point3D spherePos = sphere.getPositionOfCentre(dynamicCollisionMode);
        final double distance = triangle.getPlane().distance(spherePos);

        if (distance <= sphere.getR()) {
            Vector3D normalVector = triangle.getPlane().vector;
            Point3D intersectionPoint = triangle.getPlane().getIntersection(new Line3D(spherePos, normalVector)).get();

            if (triangle.contains(intersectionPoint))
                return true;
            else {

                for (Segment segment : triangle.getSegments()){
                    if (segment.distance(spherePos) <= sphere.getR()){
                        Point3D point = new Plane3D(segment.vector, spherePos).getIntersection(segment).get();
                        if (new AABB(segment).isPointIn(point))
                            return true;
                    }
                }

                for (Point3D point : triangle.getPoints())
                    if (new Vector3D(point, spherePos).getLength() <= sphere.getR())
                        return true;
            }
        }

        return false;
    }
}
