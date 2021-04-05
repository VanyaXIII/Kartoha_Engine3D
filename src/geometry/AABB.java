package geometry;

import geometry.objects.Segment;
import geometry.objects.Triangle;
import geometry.objects3D.Point3D;
import geometry.objects3D.Vector3D;
import physical_objects.PhysicalPolyhedron;
import physical_objects.PhysicalSphere;
import physical_objects.Wall;
import utils.FloatComparator;


import java.util.*;

/**
 * AABB - Axis Aligned Bounding Box (коробка, ограничивающая объект)
 */
public class AABB {
    private final Point3D min;
    private final Point3D max;

    /**Конструктор по максимальной и минимальной точке
     * @param min точка с минимальными координатами
     * @param max точка с максимальными координатами
     */
    public AABB(Point3D min, Point3D max) {
        this.min = min;
        this.max = max;
    }

    /**
     *Конструктор по сфере
     * @param sphere сфера
     * @param mode нужно ли считать относительно будущего положения
     */
    public AABB(PhysicalSphere sphere, boolean mode){
        Point3D position = sphere.getPositionOfCentre(mode);
        min = new Point3D(position.x - sphere.getR(), position.y - sphere.getR(), position.z - sphere.getR());
        max = new Point3D(position.x + sphere.getR(), position.y + sphere.getR(), position.z + sphere.getR());
    }

    /**
     * Конструктор по стене
     * @param wall стена
     */
    public AABB(Wall wall){
        this(wall.getPoints());
    }

    /**
     * Конструктор по треугольнику
     * @param triangle треугольник
     */
    public AABB(Triangle triangle){this(triangle.getPoints());}

    /**
     * Конструктор по отрезку
     * @param segment отрезок
     */
    public AABB(Segment segment){
        ArrayList<Point3D> points = new ArrayList<>();
        points.add(segment.point1);
        points.add(segment.point2);
        AABB aabb = new AABB(points);
        this.min = aabb.getMin();
        this.max = aabb.getMax();
    }

    /**
     * Конструктор по многограннику
     * @param polyhedron многогранник
     * @param mode нужно ли считать относительно будущего положения
     */
    public AABB(PhysicalPolyhedron polyhedron, boolean mode){
        this(new ArrayList<>(polyhedron.getPoints(mode)));
    }

    /**
     * Конструктор по множеству точек
     * @param points множество точек
     */
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

    /**
     * @param b другая коробка
     * @return Пересекается ли данная коробка с другой
     */
    public boolean isIntersectedWith(AABB b){

        if (this.max.x < b.min.x || this.min.x > b.max.x) return false;
        if (this.max.y < b.min.y || this.min.y > b.max.y) return false;
        return !(this.max.z < b.min.z) && !(this.min.z > b.max.z);
    }

    /**
     * @param point точка
     * @return Находится ли данная точка внутри коробки
     */
    public boolean isPointIn(Point3D point){

        if (FloatComparator.compare(this.max.x, point.x) == -1 || FloatComparator.compare(this.min.x, point.x) == 1)
            return false;
        if (FloatComparator.compare(this.max.y, point.y) == -1 || FloatComparator.compare(this.min.y, point.y) == 1)
            return false;
        return FloatComparator.compare(this.max.z, point.z) != -1 && FloatComparator.compare(this.min.z, point.z) != 1;
    }

    /**
     * @return Минимальная точка коробки
     */
    public Point3D getMin() {
        return min;
    }

    /**
     * @return Максимальная точка коробки
     */
    public Point3D getMax() {
        return max;
    }

    /**
     * @return Строковое представление коробки
     */
    @Override
    public String toString() {
        return "AABB{" +
                "min=" + min +
                ", max=" + max +
                '}';
    }
}
