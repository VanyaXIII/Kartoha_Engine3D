package geometry.intersections;

import geometry.objects3D.Vector3D;
/**
 * Пересечение двух сфер
 */
public class SpheresIntersection extends AbstractIntersection {

    private Vector3D centralLine;
    private double value;

    /**
     * Конструктор по информации, есть ли пересечение
     * @param areIntersected пересекаются ли объекты
     */
    public SpheresIntersection(boolean areIntersected) {
        super(areIntersected);
    }


    /**
     *Основной коструктор, принимающий всю необходимую информацию о пересечении
     * @param areIntersected пересекаются ли объекты
     * @param cl вектор через центры сфер
     * @param value величина пересечения
     */
    public SpheresIntersection(boolean areIntersected, Vector3D cl, double value) {
        super(areIntersected);
        this.centralLine = cl;
        this.value = value;
    }

    /**
     * @return Величина пересечения
     */
    public double getValue() {
        return value;
    }

    /**
     *
     * @return Вектор через центры сфер
     */
    public Vector3D getCentralLine() {
        return centralLine;
    }
}
