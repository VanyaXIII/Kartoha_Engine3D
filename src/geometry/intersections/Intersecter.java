package geometry.intersections;

import limiters.Intersectional;

/**
 * Интерфейс, предоставляющий метод для проверки пересечения двух объектов {@link limiters.Intersectional}
 */
public interface Intersecter {

    boolean areIntersected(Intersectional thing1, Intersectional thing2);

}
