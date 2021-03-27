package geometry.intersections;

import geometry.objects3D.Point3D;

public class SphereToPlaneIntersection extends Intersection{

    private Point3D collisionPoint;
    private double value;

    public SphereToPlaneIntersection(boolean areIntersected) {
        super(areIntersected);
    }

    public SphereToPlaneIntersection(boolean areIntersected, Point3D collisionPoint, float value) {
        super(areIntersected);
        this.value = value;
        this.collisionPoint = collisionPoint;
    }

    public double getValue() {
        return value;
    }

    public Point3D getCollisionPoint() {
        return collisionPoint;
    }
}

