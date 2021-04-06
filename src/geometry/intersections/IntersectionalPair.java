package geometry.intersections;

import exceptions.ImpossiblePairException;
import geometry.AABB;
import geometry.objects.Segment;
import geometry.objects.Triangle;
import geometry.objects3D.Line3D;
import geometry.objects3D.Plane3D;
import geometry.objects3D.Point3D;
import geometry.objects3D.Vector3D;
import limiters.Intersectional;
import physical_objects.PhysicalPolyhedron;
import physical_objects.PhysicalSphere;
import physical_objects.Wall;
import utils.TripleMap;

/**Класс, реализующий обработку и проверку пересечений между объектами {@link limiters.Intersectional}
 */

public final class IntersectionalPair<FirstThingType extends Intersectional, SecondThingType extends Intersectional> {

    private final FirstThingType firstThing;
    private final SecondThingType secondThing;
    private final static boolean dynamicCollisionMode = true;
    private final static boolean staticCollisionMode = false;
    private final static TripleMap<Class, Class, Intersecter> methodsMap;

    /**
     *Конструктор, принимающиий предметы, исследуемые на пересечение
     * @param firstThing объект {@link limiters.Intersectional}, пересечение которого с другим объектом нужно найти или проверить
     * @param secondThing объект {@link limiters.Intersectional}, пересечение с которым нужно найти или проверить
     * @throws ImpossiblePairException исключение в случае попытки проверить пересечение треугольника с треугольком
     */
    public IntersectionalPair(FirstThingType firstThing, SecondThingType secondThing) throws ImpossiblePairException {
        this.firstThing = firstThing;
        this.secondThing = secondThing;
        if (firstThing instanceof Wall && secondThing instanceof Wall)
            throw new ImpossiblePairException("Trying to check triangle with triangle intersection");
    }

    /*Статический иницализатор, создающий таблицу переходов, содержащую методы для исследования пересчений конкретных пар объектов
     */

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

    /**Метод, распределящий все поиски пересечений между объектами типа {@link limiters.Intersectional}, вызывает метод для определенной пары объектов
     */

    public boolean areIntersected() {
        return methodsMap.getElement(firstThing.getClass(), secondThing.getClass()).areIntersected(firstThing, secondThing);
    }

    /**@param thing1 многогранник 1
      @param thing2 многогранник 2
     * @return Пересекаются ли два многогранника
     */

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

    /**@param thing1 многогранник или треугольник
     * @param thing2 треугольник или многогранник
     * @return Пересекаются ли многогранник и треугольник
     */

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

        for (Segment segment : polyhedron.getSegments(dynamicCollisionMode))
            if (triangle.isIntersectedWithSegment(segment))
                return true;

        return false;
    }

    /**@param thing1 сфера или многогранник
     * @param thing2 многогранник или сфера
     * @return  Пересекается ли сфера и многогрaнник
     */

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

    /**@param thing1 сфера 1
     * @param thing2 сфера 2
     * @return Пересекаются ли две сферы
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

    /**@param thing1 сфера или треугольник
     * @param thing2 треугольник или сфера
     * @return Пересекаются ли сфера и треугольник
     */

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

                for (Segment segment : triangle.getSegments()) {
                    if (segment.distance(spherePos) <= sphere.getR()) {
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

    /**
     *
     * @return Пересечение двух сфер
     */
    public SpheresIntersection getSpheresIntersection() {
        if (!(firstThing instanceof PhysicalSphere && secondThing instanceof PhysicalSphere))
            return new SpheresIntersection(false);

        if (!new AABB((PhysicalSphere) firstThing, staticCollisionMode).isIntersectedWith(new AABB((PhysicalSphere) secondThing, staticCollisionMode)))
            return new SpheresIntersection(false);

        PhysicalSphere sphere1 = (PhysicalSphere) firstThing;
        PhysicalSphere sphere2 = (PhysicalSphere) secondThing;
        Point3D sphere1Pos = sphere1.getPositionOfCentre(staticCollisionMode);
        Point3D sphere2Pos = sphere2.getPositionOfCentre(staticCollisionMode);
        Vector3D distanceVector = new Vector3D(sphere2Pos, sphere1Pos);
        double distance = distanceVector.getLength();

        if (distance < sphere1.getR() + sphere2.getR()) {
            if (sphere2.equals(sphere1)) {
                return new SpheresIntersection(false);
            } else {
                if (distanceVector.getLength() != 0)
                    return new SpheresIntersection(true, distanceVector, sphere2.getR() + sphere1.getR() - distance);
                else
                    return new SpheresIntersection(true, distanceVector, 0);
            }
        }

        return new SpheresIntersection(false);
    }

    /**
     * @return Пересечение сферы и плоскости
     */
    public SphereToPlaneIntersection getSphereToPlaneIntersection() {

        if (!(firstThing instanceof PhysicalSphere && secondThing instanceof Triangle))
            return new SphereToPlaneIntersection(false);

        PhysicalSphere sphere = (PhysicalSphere) firstThing;
        Triangle triangle = (Triangle) secondThing;

        if (!new AABB(sphere, staticCollisionMode).isIntersectedWith(new AABB(triangle)))
            return new SphereToPlaneIntersection(false);
        else {
            try {
                if (new IntersectionalPair<>(sphere, triangle).areIntersected()) {
                    Line3D line = new Line3D(sphere.getPositionOfCentre(staticCollisionMode), triangle.getPlane().vector);
                    return new SphereToPlaneIntersection(true,
                            line.getIntersection(triangle.getPlane()).get(),
                            sphere.getR() - triangle.getPlane().distance(sphere.getPositionOfCentre(staticCollisionMode)));
                }

            } catch (ImpossiblePairException e) {
                e.printStackTrace();
            }
        }

        return new SphereToPlaneIntersection(false);
    }

    /**
     *
     * @return Пересечение многогранник и плоскости
     */

    public PolyhedronToPlaneIntersection getPolyhedronToPlaneIntersection() {
        if (!(firstThing instanceof PhysicalPolyhedron && secondThing instanceof Triangle))
            return new PolyhedronToPlaneIntersection(false);

        if (!new AABB((PhysicalPolyhedron) firstThing, staticCollisionMode).isIntersectedWith(new AABB((Triangle) secondThing)))
            return new PolyhedronToPlaneIntersection(false);

        PhysicalPolyhedron polyhedron = (PhysicalPolyhedron) firstThing;
        Triangle triangle = (Triangle) secondThing;
        Point3D position = polyhedron.getPositionOfCentre(staticCollisionMode);
        Point3D farPoint = null;

        for (Point3D point : polyhedron.getPoints(staticCollisionMode)) {
            boolean intersects = triangle.isIntersectedWithSegment(new Segment(point, position));
            if (farPoint == null && intersects) {
                farPoint = point;
            }
            if (intersects && triangle.getPlane().distance(point) > triangle.getPlane().distance(farPoint)) {
                farPoint = point;
            }
        }

        if (farPoint != null) {
            Point3D collisionPoint = triangle.getPlane().getIntersection(new Line3D(farPoint, triangle.getPlane().vector)).get();
            return new PolyhedronToPlaneIntersection(true, collisionPoint, farPoint, triangle.getPlane().distance(farPoint));
        }

        return new PolyhedronToPlaneIntersection(false);


    }
}
