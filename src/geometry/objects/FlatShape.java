package geometry.objects;

import geometry.objects3D.Line3D;
import geometry.objects3D.Point3D;
import geometry.objects3D.Vector3D;

import java.util.HashSet;
import java.util.Set;

/**Плоская форма(по факту многоугольник), используется для нахождения момента инерции многогрнника
 */
public class FlatShape {

    private final Set<Segment> segments;
    private final Set<Triangle> triangles;

    {
        triangles = new HashSet<>();
    }

    /**
     * Конструктор по множеству отрезков, лежащих в одной плоскости и образующих многоугольник
     * @param segments отрезки
     */
    public FlatShape(Set<Segment> segments){
        this.segments = segments;
        triangulate();
    }

    /**
     * Метод осуществляет треангуляция многоугольника(разбивает на множество треугольников)
     */
    public void triangulate(){
        Point3D zeroPoint = segments.iterator().next().point1;
        for (Segment segment : segments) triangles.add(new Triangle(zeroPoint, segment));
    }

    /**
     * @return Точку - центр масс многоугольнка
     */
    public Point3D getCentreOfMass() {
        Vector3D rC = new Vector3D(0,0,0);
        double s = 0d;

        for (Triangle triangle : triangles) {
            Vector3D rCOfTriangle = new Vector3D(Point3D.ZERO, triangle.getCentroid());
            rCOfTriangle = rCOfTriangle.multiply(triangle.getSquare());

            s += triangle.getSquare();
            rC = rC.add(rCOfTriangle);
        }

        if (s == 0.0) s = 0.01;

        rC = rC.multiply(1d / s);

        return new Point3D(rC.x, rC.y, rC.z);
    }

    /**
     * @return Площадь многоугольника
     */
    public double getSquare() {
        double square = 0f;
        for (Triangle triangle : triangles) {
            square += triangle.getSquare();
        }
        return square;
    }

    /**
     * @return Момент инерции многоугольника, поделенный на плотность
     * (относительно оси проходящей через центр масс, перпендикулярно плоскости многоугольника)
     */
    public double getJDivDensity() {
        Point3D c = getCentreOfMass();
        double J = 0d;

        for (Triangle triangle : triangles){
            double d = new Vector3D(c, triangle.getCentroid()).getLength();
            J += triangle.getJDivDensity() + triangle.getSquare() * d * d;
        }

        return J;
    }

    /**
     *
     * @param line прямая, относительно которой нужно вычислить момент инерции
     * @return Момент инерции, поделенный на плотность, относительно заданной оси
     */
    public double getRelativeJ(Line3D line){
        double d = line.distance(getCentreOfMass());
        return getJDivDensity() + getSquare() * d * d;
    }

    /**
     * @return Строковое представление многоугольника
     */
    @Override
    public String toString() {
        return "FlatShape{" +
                "segments=" + segments +
                ", triangles=" + triangles +
                '}';
    }
}



