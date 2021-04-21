package physical_objects;

import geometry.objects.Triangle;
import geometry.objects3D.Line3D;
import geometry.objects3D.Point3D;
import geometry.objects3D.Vector3D;
import physics.Material;
import physics.Space;

/**
 * Пластина, обладающая собственной гравитацией
 */
public class GravityPlate extends Wall {

    /**
     * @return исходный параметр G
     */
    public double getG() {
        return G;
    }

    private final double G;

    /**
     * Конструктор
     * @param space пространство, в котором лежит пластина
     * @param a точка 1
     * @param b точка 2
     * @param c точка 3
     * @param d точка 4
     * @param g величина ускорения свободного падения
     * @param material материал, из которого сделана пластина
     */
    public GravityPlate(Space space, Point3D a, Point3D b, Point3D c, Point3D d, double g, Material material) {
        super(space, a, b, c, d, material);
        G = g;
    }

    /**
     * @param point точка, в которой рассчитвается вектор ускорения свободного падения
     * @return Вектор ускорения свободного падения в данной точке
     */
    public Vector3D getG(Point3D point) {
        Line3D gravityLine = new Line3D(point, getPlane().vector);
        Point3D intersectionPoint = gravityLine.getIntersection(getPlane()).get();
        boolean contains = false;
        for (Triangle triangle : getTriangles())
            if (triangle.contains(intersectionPoint))
                contains = true;
        return contains ? new Vector3D(point, intersectionPoint).normalize().multiply(G) : new Vector3D(0,0,0);
    }

}
