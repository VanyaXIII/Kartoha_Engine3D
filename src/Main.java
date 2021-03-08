

import com.aparapi.Kernel;
import com.google.gson.Gson;
import geometry.Triangle;
import geometry.objects3D.Point3D;
import geometry.objects3D.Vector3D;
import geometry.polygonal.Sphere;
import graph.Camera;
import graph.CanvasPanel;
import graph.Screen;
import physics.Space;
import shapes.Shape;
import shapes.ShapeReader;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class Main {

    public static final int FocusLength = 300;
    public static final int Distance = 500;

    public static void main(String[] args) throws IOException {
        Camera.Resolution resolution = new Camera.Resolution(1920, 1080);
        Camera camera = new Camera(new Screen(new Vector3D(FocusLength, 0, 0), new Point3D(-Distance, 500, 2000)), resolution, 0);
        CanvasPanel canvas = new CanvasPanel(camera, Kernel.EXECUTION_MODE.JTP);
        canvas.prepare();
        canvas.setSize(1920, 1080);
        canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas.setLocationRelativeTo(null);
        canvas.setResizable(true);
        canvas.setUndecorated(false);
        canvas.setVisible(true);
        Space space = new Space(0.01f, 00f, canvas);

        Gson gson = new Gson();
//
        Point3D A = new Point3D(0,0,0),
                B = new Point3D(200,200,0),
                C = new Point3D(200,0,0),
                D = new Point3D(0,200,0);

        Point3D A1 = new Point3D(0,0,200),
                B1 = new Point3D(200,200,200),
                C1 = new Point3D(200,0,200),
                D1 = new Point3D(0,200,200);
//
        ArrayList<Point3D> points = new ArrayList<>();
        points.add(A);
        points.add(B);
        points.add(C);
        points.add(D);
        points.add(A1);
        points.add(B1);
        points.add(C1);
        points.add(D1);

        Triangle t1 = new Triangle(A,B,C),
                t2 = new Triangle(A,B,D),
                t3 = new Triangle(B, C, C1),
                t5 = new Triangle(B, B1, C1),
                t4 = new Triangle(A, C, C1),
                t7 = new Triangle(A, A1, C1),
                t6 = new Triangle(B, D, D1),
                t8 = new Triangle(B, B1, D1),
                t9 = new Triangle(A, D, D1),
                t10 = new Triangle(A, A1, D1),
                t11 = new Triangle(A1,B1,C1),
                t12 = new Triangle(A1,B1,D1);

        ArrayList<Triangle> triangles = new ArrayList<>();
        triangles.add(t1);
        triangles.add(t2);
        triangles.add(t3);
        triangles.add(t4);
        triangles.add(t5);
        triangles.add(t6);
        triangles.add(t7);
        triangles.add(t8);
        triangles.add(t9);
        triangles.add(t10);
        triangles.add(t11);
        triangles.add(t12);
//
        Shape shape = new Shape(points, triangles);

//        shape.toJson("src\\shapes\\assets\\cube.json");

//        Shape shape = new ShapeReader("src\\shapes\\assets\\tetrahedron.json").read();
//        for(Point3D point : shape.getPoints()) System.out.println(point);

        Date date = new Date();
        date.getTime();

        System.currentTimeMillis();

//        new ShapeReader("src\\shapes\\assets\\tetrahedron.json").read().toJson("src\\shapes\\assets\\tetrahedron1.json");

        new Thread(() -> {
            while (true) {
                synchronized (canvas) {
                    canvas.repaint();
                    space.changeTime();
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
//        while (true) {
//            synchronized (canvas) {
//                canvas.repaint();
//                space.changeTime();
//            }
//        }


    }
}
