package geometry.intersections;

import geometry.objects3D.Vector3D;

public class SpheresIntersection extends Intersection{

    private Vector3D centralLine;
    private double value;

    public SpheresIntersection(boolean areIntersected, Vector3D cl, double value) {
        super(areIntersected);
        this.centralLine = cl;
        this.value = value;
    }

    public SpheresIntersection(boolean areIntersected) {
        super(areIntersected);
    }

    public double getValue() {
        return value;
    }

    public Vector3D getCentralLine() {
        return centralLine;
    }
}
