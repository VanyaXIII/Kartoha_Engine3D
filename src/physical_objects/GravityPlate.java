package physical_objects;

import geometry.objects.Triangle;
import geometry.objects3D.Line3D;
import geometry.objects3D.Point3D;
import geometry.objects3D.Vector3D;
import org.jetbrains.annotations.NotNull;
import physics.Material;
import physics.Space;

public class GravityPlate extends Wall {

    private final double G;

    public GravityPlate(@NotNull Space space, Point3D a, Point3D b, Point3D c, Point3D d, double g, Material material) {
        super(space, a, b, c, d, material);
        G = g;
    }

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
