package geometry;

import geometry.objects3D.Point3D;
import geometry.objects3D.Vector3D;
import physical_objects.PhysicalSphere;
import physical_objects.Wall;
import utils.FloatComparator;

import java.util.ArrayList;

public class AABB {
    private final Point3D min;
    private final Point3D max;

    public AABB(Point3D min, Point3D max) {
        this.min = min;
        this.max = max;
    }

    public AABB(PhysicalSphere sphere, boolean mode){
        Point3D position = sphere.getPositionOfCentre(mode);
        min = new Point3D(position.x - sphere.getR(), position.y - sphere.getR(), position.z - sphere.getR());
        max = new Point3D(position.x + sphere.getR(), position.y + sphere.getR(), position.z + sphere.getR());
    }

    public AABB(Wall wall){
        ArrayList<Point3D> points = wall.getPoints();
        float posXDeviation = 0f;
        float posYDeviation = 0f;
        float posZDeviation = 0f;
        float negXDeviation = 0f;
        float negYDeviation = 0f;
        float negZDeviation = 0f;

        for (int i = 1; i < points.size(); i++) {
            Vector3D vectorToPoint = new Vector3D(points.get(0), points.get(i));
            if (posXDeviation < vectorToPoint.x) posXDeviation = (float) vectorToPoint.x;
            if (posYDeviation < vectorToPoint.y) posYDeviation = (float) vectorToPoint.y;
            if (posZDeviation < vectorToPoint.z) posZDeviation = (float) vectorToPoint.z;
            if (negXDeviation > vectorToPoint.x) negXDeviation = (float) vectorToPoint.x;
            if (negYDeviation > vectorToPoint.y) negYDeviation = (float) vectorToPoint.y;
            if (negZDeviation > vectorToPoint.z) negZDeviation = (float) vectorToPoint.z;
        }

        min = new Point3D(points.get(0).x + negXDeviation, points.get(0).y + negYDeviation, points.get(0).z + negZDeviation);
        max = new Point3D(points.get(0).x + posXDeviation, points.get(0).y + posYDeviation, points.get(0).z + posZDeviation);
    }

    public boolean isIntersectedWith(AABB b){

        if (this.max.x < b.min.x || this.min.x > b.max.x) return false;
        if (this.max.y < b.min.y || this.min.y > b.max.y) return false;
        if (this.max.z < b.min.z || this.min.z > b.max.z) return false;

        return true;
    }

    public boolean isPointIn(Point3D point){

        if (FloatComparator.compare(this.max.x, point.x) == -1 || FloatComparator.compare(this.min.x, point.x) == 1)
            return false;
        if (FloatComparator.compare(this.max.y, point.y) == -1 || FloatComparator.compare(this.min.y, point.y) == 1)
            return false;
        if (FloatComparator.compare(this.max.z, point.z) == -1 || FloatComparator.compare(this.min.z, point.z) == 1)
            return false;

        return true;
    }

    @Override
    public String toString() {
        return "AABB{" +
                "min=" + min.x + " " + min.y + " " + min.z +
                ", max=" + max.x + " " + max.y + " " + max.z +
                '}';
    }
}
