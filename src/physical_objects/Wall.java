package physical_objects;

import drawing.Drawable;
import geometry.objects3D.Plane3D;
import geometry.objects3D.Point3D;
import geometry.objects3D.Polygon3D;
import graph.CanvasPanel;
import limiters.Collisional;
import limiters.Intersectional;
import org.jetbrains.annotations.NotNull;
import physics.Material;
import physics.Space;

import java.util.ArrayList;
import java.util.HashSet;

public class Wall implements Drawable, Collisional, Intersectional {

    private final Point3D a;
    private final Point3D b;
    private final Point3D c;
    private final Material material;

    public Wall(@NotNull Space space, Point3D a, Point3D b, Point3D c, Material material) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.material = material;
        pushToCanvas(space.getCanvas());
    }

    public Plane3D getPlane(){
        return new Plane3D(a, b, c);
    }

    public Material getMaterial() {
        return material;
    }

    public ArrayList<Point3D> getPoints(){
        ArrayList<Point3D> points = new ArrayList<>();
        points.add(a);
        points.add(b);
        points.add(c);
        return points;
    }

    @Override
    public void pushToCanvas(CanvasPanel canvas) {

        canvas.getPolygonals().add(() ->
        {
            HashSet<Polygon3D> polygons = new HashSet<>();
            polygons.add(new Polygon3D(a, b, c, material.fillColor));
            return polygons;
        });

    }

    @Override
    public void updateDrawingInterpretation() {}
}
