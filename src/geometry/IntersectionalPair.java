package geometry;

import geometry.objects3D.Point3D;
import geometry.objects3D.Vector3D;
import limiters.Intersectional;
import physical_objects.PhysicalSphere;
import utils.TripleMap;

public class IntersectionalPair<FirstThingType extends Intersectional, SecondThingType extends Intersectional> {

    private final FirstThingType firstThing;
    private final SecondThingType secondThing;
    private final static boolean dynamicCollisionMode = true;
    private final static boolean staticCollisionMode = false;
    private final static TripleMap<Class, Class, Intersecter> methodsMap;

    public IntersectionalPair(FirstThingType firstThing, SecondThingType secondThing) {
        this.firstThing = firstThing;
        this.secondThing = secondThing;
    }

    static {
        methodsMap = new TripleMap<>();

        methodsMap.addFirstKey(PhysicalSphere.class);

        methodsMap.putByFirstKey(PhysicalSphere.class, PhysicalSphere.class, IntersectionalPair::sphereToSphere);
    }

    public boolean areIntersected(){
        return methodsMap.getElement(firstThing.getClass(), secondThing.getClass()).areIntersected(firstThing, secondThing);
    }

    private static boolean sphereToSphere(Intersectional thing1, Intersectional thing2) {

        PhysicalSphere sphere1 = (PhysicalSphere) thing1;
        PhysicalSphere sphere2 = (PhysicalSphere) thing2;

        if (!new AABB(sphere1, dynamicCollisionMode).isIntersectedWith(new AABB(sphere2, dynamicCollisionMode)))
            return false;

        Point3D sphere1Pos = sphere1.getPosition(dynamicCollisionMode);
        Point3D sphere2Pos = sphere2.getPosition(dynamicCollisionMode);

        Vector3D distanceVector = new Vector3D(sphere1Pos.x - sphere2Pos.x, sphere1Pos.y - sphere2Pos.y, sphere1Pos.z - sphere2Pos.z);
        if (distanceVector.getLength() <= (sphere1.getR() + sphere2.getR())){
            return !sphere2.equals(sphere1);
        }

        return false;
    }
}
