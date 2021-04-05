package geometry.intersections;

import geometry.objects3D.Point3D;

/**
 * Пересечение многогранника с плоскостью
 */
public class PolyhedronToPlaneIntersection extends AbstractIntersection {
    
    private Point3D intersectionPoint;
    private Point3D pointOfPolygon;
    private double value;

    /**
     * Конструктор по информации, есть ли пересечение
     * @param areIntersected пересекаются ли объекты
     */
    public PolyhedronToPlaneIntersection(boolean areIntersected) {
        super(areIntersected);
    }

    /**
     * Основной коструктор, принимающий всю необходимую информацию о пересечении
     * @param areIntersected пересекаются ли объекты
     * @param intersectionPoint точка пересечения
     * @param pointOfPolygon точка многогранника
     * @param value величина пересечения
     */
    public PolyhedronToPlaneIntersection(boolean areIntersected, Point3D intersectionPoint, Point3D pointOfPolygon, double value){
        super(areIntersected);
        this.intersectionPoint = intersectionPoint;
        this.pointOfPolygon = pointOfPolygon;
        this.value = value;
    }

    /**
     * @return Величина пересечения
     */
    public double getValue() {
        return value;
    }

    /**
     * @return Вочка полигона
     */
    public Point3D getPointOfPolygon() {
        return pointOfPolygon;
    }

    /**
     * @return Вочка пересечения
     */
    public Point3D getIntersectionPoint() {
        return intersectionPoint;
    }
}
