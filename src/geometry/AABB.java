package geometry;

import geometry.objects3D.Point3D;
import physical_objects.PhysicalSphere;

public class AABB {
    private final Point3D min;
    private final Point3D max;

    public AABB(Point3D min, Point3D max) {
        this.min = min;
        this.max = max;
    }

    public AABB(PhysicalSphere sphere, boolean mode){
        Point3D position = sphere.getPosition(mode);
        min = new Point3D(position.x - sphere.getR(), position.y - sphere.getR(), position.z - sphere.getR());
        max = new Point3D(position.x + sphere.getR(), position.y + sphere.getR(), position.z + sphere.getR());
    }

    public boolean isIntersectedWith(AABB b){

        if (this.max.x < b.min.x || this.min.x > b.max.x) return false;
        if (this.max.y < b.min.y || this.min.y > b.max.y) return false;
        if (this.max.z < b.min.z || this.min.z > b.max.z) return false;

        return true;
    }
}
