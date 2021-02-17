package geometry;

import limiters.Intersectional;

public interface Intersecter {
    boolean areIntersected(Intersectional thing1, Intersectional thing2);
}
