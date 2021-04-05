package geometry.intersections;

/**
 * Абстарктное пересечение, от него наследуются другие (см {@link geometry.intersections})
 */
public abstract class AbstractIntersection {

    public final boolean areIntersected;

    /**
     * Конструктор создающий пересечение между двумя объектами по информации о том, есть ли оно
     * @param areIntersected пересекаются ли объекты
     */
    public AbstractIntersection(boolean areIntersected) {
        this.areIntersected = areIntersected;
    }
}
