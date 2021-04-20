package geometry.objects;

import geometry.AABB;
import geometry.objects3D.*;
import limiters.Intersectional;
import utils.FloatComparator;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Трехмерный треугольник
 */
public class Triangle implements Intersectional {
    public Point3D A, B, C;
    public Color color;

    /**
     * Конструктор по трем точкам
     * @param a точка 1
     * @param b точка 2
     * @param c точка 3
     */
    public Triangle(Point3D a, Point3D b, Point3D c, Color color) {
        A = a;
        B = b;
        C = c;
        this.color = color;
    }

    /**
     * Конструктор по полигону
     * @param polygon полигон
     */
    public Triangle(Polygon3D polygon) {
        A = polygon.a1;
        B = polygon.a2;
        C = polygon.a3;
        color = polygon.color;
    }

    /**
     * Конструктор по точке и отрезку
     * @param point точка
     * @param segment отрезок
     */
    public Triangle(Point3D point, Segment segment, Color color){
        A = point;
        B = segment.point1;
        C = segment.point2;
        this.color = color;
    }

    /**
     * @return Площадь треугольника
     */
    public double getSquare() {
        double side1 = new Vector3D(A, B).getLength();
        double side2 = new Vector3D(A, C).getLength();
        double side3 = new Vector3D(B, C).getLength();
        double p = (side1 + side3 + side2) / 2.0d;
        return Math.sqrt(Math.abs(p * (p - side1) * (p - side2) * (p - side3)));
    }

    /**
     * @return Центр масс треугольника
     */
    public Point3D getCentroid() {
        Vector3D rC = new Vector3D(0, 0, 0);
        rC = rC.add(new Vector3D(Point3D.ZERO, A).multiply(1d / 3d));
        rC = rC.add(new Vector3D(Point3D.ZERO, B).multiply(1d / 3d));
        rC = rC.add(new Vector3D(Point3D.ZERO, C).multiply(1d / 3d));

        return new Point3D(rC.x, rC.y, rC.z);
    }

    /**
     *
     * @return Момент инерции треугольника, поделенный на плотность
     *      * (относительно оси проходящей через центр масс, перпендикулярно плоскости многоугольника)
     */
    public double getJDivDensity() {
        double side1 = new Vector3D(A, B).getLength();
        double side2 = new Vector3D(A, C).getLength();
        double side3 = new Vector3D(B, C).getLength();
        return (getSquare() / 36d) * (side1 * side1 + side2 * side2 + side3 * side3);
    }

    /**
     * Метод, преобразующий треугольник в полигон данного цвета
     * @param color цвет полигона
     * @return Полигон
     */
    public Polygon3D toPolygon(Color color) {
        return new Polygon3D(A, B, C, color);
    }


    /**
     * @param movement вектор перемещения
     * @return Треугольник, перемещенный на заданный вектор
     */
    public Triangle move(Vector3D movement) {
        Point3D a = movement.addToPoint(A),
                b = movement.addToPoint(B),
                c = movement.addToPoint(C);

        return new Triangle(a, b, c, color);
    }

    /**
     * @param point точка
     * @return Лежит ли данная точка внутри треугольника
     */
    public boolean contains(Point3D point) {
        double s1 = new Triangle(point, A, B, color).getSquare();
        double s2 = new Triangle(point, A, C, color).getSquare();
        double s3 = new Triangle(point, B, C, color).getSquare();

        return FloatComparator.equals(getSquare(), s1 + s2 + s3) &&
                FloatComparator.equals(new Plane3D(A, B, C).distance(point), 0d);
    }

    /**
     * @return Плоскость, в которой лежит треугольник
     */
    public Plane3D getPlane() {
        return new Plane3D(A, B, C);
    }

    /**
     * @param plane плоскость
     * @return Точки пересечения плоскости с теругольником
     */
    public ArrayList<Point3D> getIntersectionWithPlane(Plane3D plane){
        ArrayList<Point3D> intersectionPoints = new ArrayList<>();

        for (Line3D triangleLine : getSegments()) {
            try {
                Point3D intersectionPoint = plane.getIntersection(triangleLine).get();
                if (contains(intersectionPoint)) {
                    intersectionPoints.add(intersectionPoint);
                }
            }
            catch (Exception ignored){}
        }

        return intersectionPoints;
    }

    /**
     * @return Множество граней треугольника
     */
    public Set<Segment> getSegments(){
        Set<Segment> segments = new HashSet<>();
        segments.add(new Segment(A, B));
        segments.add(new Segment(C, B));
        segments.add(new Segment(A, C));
        return segments;
    }

    /**
     * @param vector вектор поворота
     * @param point точка поворота
     * @return Повернутый треугольник
     */
    public Triangle rotate(Vector3D vector, Point3D point){
        Point3D a = A.rotate(vector, point);
        Point3D b = B.rotate(vector, point);
        Point3D c = C.rotate(vector, point);
        return new Triangle(a,b,c, color);
    }

    /**
     *
     * @param segment отрезок
     * @return Пересекает ли треугольник данный отрезок
     */
    public boolean isIntersectedWithSegment(Segment segment){
        try {
            Point3D intersectionPoint = getPlane().getIntersection(segment).get();
            return new AABB(segment).isPointIn(intersectionPoint) && contains(intersectionPoint);
        }
        catch (Exception exception){
            return false;
        }

    }

    /**
     * @return Вершины треугольника
     */
    public ArrayList<Point3D> getPoints(){
        ArrayList<Point3D> points = new ArrayList<>();
        points.add(A);
        points.add(B);
        points.add(C);
        return points;
    }

    /**
     * @return Строковое представление треугольника
     */
    @Override
    public String toString() {
        return "Triangle{" +
                "A=" + A +
                ", B=" + B +
                ", C=" + C +
                '}';
    }
}
