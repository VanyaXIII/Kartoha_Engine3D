package geometry.intersections;

import geometry.objects3D.Point3D;


/**
 * Пересечение сферы с плоскостью
 */
public class SphereToPlaneIntersection extends AbstractIntersection {

    private Point3D intersectionPoint;
    private double value;

    /**
     * Конструктор по информации, есть ли пересечение
     * @param areIntersected пересекаются ли объекты
     */
    public SphereToPlaneIntersection(boolean areIntersected) {
        super(areIntersected);
    }

    /**
     * Основной коструктор, принимающий всю необходимую информацию о пересечении
     * @param areIntersected пересекаются ли объекты
     * @param intersectionPoint точка пересечения
     * @param value величина пересечения
     */
    public SphereToPlaneIntersection(boolean areIntersected, Point3D intersectionPoint, double value) {
        super(areIntersected);
        this.value = value;
        this.intersectionPoint = intersectionPoint;
    }

    public double getValue() {
        return value;
    }

    public Point3D getIntersectionPoint() {
        return intersectionPoint;
    }
}

