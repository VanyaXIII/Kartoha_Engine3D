package geometry;

import geometry.objects3D.Point3D;
import geometry.objects3D.Polygon3D;
import geometry.objects3D.Vector3D;

public class Tetrahedron {
    private final Point3D A,B,C,D;

    public Tetrahedron(Point3D a, Point3D b, Point3D c, Point3D d) {
        A = a;
        B = b;
        C = c;
        D = d;
    }

    public Tetrahedron(Point3D a, Polygon3D polygon){
        A = a;
        B = polygon.a1;
        C = polygon.a2;
        D = polygon.a3;
    }

    public Tetrahedron(Point3D a, Triangle triangle){
        A = a;
        B = triangle.A;
        C = triangle.B;
        D = triangle.C;
    }

    public Point3D getCentreOfMass(){

        Point3D zeroPoint = new Point3D(0, 0, 0);
        final Vector3D v1 = new Vector3D(zeroPoint, A);
        final Vector3D v2 = new Vector3D(zeroPoint, B);
        final Vector3D v3 = new Vector3D(zeroPoint, C);
        final Vector3D v4 = new Vector3D(zeroPoint, D);
        final Vector3D c = v1.add(v2.add(v3.add(v4))).multiply(0.25);

        return new Point3D(c.x, c.y, c.z);

    }

    public double getVolume(){
        final Vector3D a = new Vector3D(D, B);
        final Vector3D b = new Vector3D(D, A);
        final Vector3D c = new Vector3D(D, C);

        return (double) ((1/6) * a.scalarProduct(b.vectorProduct(c)));
    }
}
