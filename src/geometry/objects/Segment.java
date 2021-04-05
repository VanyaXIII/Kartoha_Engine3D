package geometry.objects;

import geometry.objects3D.Line3D;
import geometry.objects3D.Point3D;
import geometry.objects3D.Vector3D;

import java.util.ArrayList;

/**
 * Трехменрный отрезок
 */
public class Segment extends Line3D {

    public final Point3D point1;
    public final Point3D point2;

    /**
     * Конструктор отрезка по точке и вектора, откладываемого от нее
     * @param p точка
     * @param vector вектор
     */
    public Segment(Point3D p, Vector3D vector) {
        super(p, vector);
        this.point1 = p;
        this.point2 = vector.addToPoint(point1);
    }

    /**
     * Конструктор отрезка по двум точкам
     * @param p1 первая точка
     * @param p2 вторая точка
     */
    public Segment(Point3D p1, Point3D p2) {
        super(p1, p2);
        this.point1 = p1;
        this.point2 = p2;
    }

    /**
     * @return Вектор отрезка
     */
    public Vector3D getVector(){
        return new Vector3D(point1, point2);
    }

    /**
     * @return Длину отрезка
     */
    public double getLength(){
        return getVector().getLength();
    }

    /**
     * @return Строковое представление отрезка
     */
    @Override
    public String toString() {
        return "Segment{" +
                "point1=" + point1 +
                ", point2=" + point2 +
                '}';
    }
}
