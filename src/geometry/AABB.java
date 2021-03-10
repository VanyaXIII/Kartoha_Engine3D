package geometry;

import geometry.objects3D.Line3D;
import geometry.objects3D.Point3D;
import geometry.objects3D.Vector3D;
import physical_objects.PhysicalPolyhedron;
import physical_objects.PhysicalSphere;
import physical_objects.Wall;
import utils.FloatComparator;
import utils.Tools;


import java.util.*;

public class AABB {
    private Point3D min;
    private Point3D max;

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
        this(wall.getPoints());
    }

    public AABB(Segment segment){
        ArrayList<Point3D> points = new ArrayList<>();
        points.add(segment.point1);
        points.add(segment.point2);
        AABB aabb = new AABB(points);
        this.min = aabb.getMin();
        this.max = aabb.getMax();
    }

    public AABB(PhysicalPolyhedron polyhedron, boolean mode){
        this(polyhedron.getPoints(mode));
    }

    public AABB(ArrayList<Point3D> points){
        double posXDeviation = 0f;
        double posYDeviation = 0f;
        double posZDeviation = 0f;
        double negXDeviation = 0f;
        double negYDeviation = 0f;
        double negZDeviation = 0f;

        for (int i = 1; i < points.size(); i++) {
            Vector3D vectorToPoint = new Vector3D(points.get(0), points.get(i));
            if (posXDeviation < vectorToPoint.x) posXDeviation = vectorToPoint.x;
            if (posYDeviation < vectorToPoint.y) posYDeviation = vectorToPoint.y;
            if (posZDeviation < vectorToPoint.z) posZDeviation = vectorToPoint.z;
            if (negXDeviation > vectorToPoint.x) negXDeviation = vectorToPoint.x;
            if (negYDeviation > vectorToPoint.y) negYDeviation = vectorToPoint.y;
            if (negZDeviation > vectorToPoint.z) negZDeviation = vectorToPoint.z;
        }

        this.min = new Point3D(points.get(0).x + negXDeviation, points.get(0).y + negYDeviation, points.get(0).z + negZDeviation);
        this.max = new Point3D(points.get(0).x + posXDeviation, points.get(0).y + posYDeviation, points.get(0).z + posZDeviation);
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

    public ArrayList<Point3D> getAllPoints(){
        ArrayList<Point3D> points = new ArrayList<>();
        points.add(min);
        points.add(max);
        points.add(new Point3D(max.x, min.y, min.z));
        points.add(new Point3D(max.x, max.y, min.z));
        points.add(new Point3D(min.x, max.y, min.z));
        points.add(new Point3D(max.x, min.y, max.z));
        points.add(new Point3D(min.x, min.y, max.z));
        points.add(new Point3D(min.x, max.y, max.z));
        return points;
    }

    public Segment countProjectionOnLine(Line3D line){
        ArrayList<Point3D> points = new ArrayList<>();
        getAllPoints().forEach(point -> points.add(Tools.countProjectionOfPoint(point, line)));
        SortedMap<Double, Point3D> distances = new TreeMap<>();
        points.forEach(point -> distances.put(new Vector3D(Point3D.ZERO, point).getLength(), point));
        System.out.println(distances.firstKey());
        System.out.println(distances.lastKey());
        System.out.println(new Line3D(distances.get(distances.firstKey()), distances.get(distances.lastKey())));
        return new Segment(distances.get(distances.firstKey()), distances.get(distances.lastKey()));
    }

    public Point3D getMin() {
        return min;
    }

    public Point3D getMax() {
        return max;
    }

    @Override
    public String toString() {
        return "AABB{" +
                "min=" + min +
                ", max=" + max +
                '}';
    }
}
