

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

//        Gson gson = new Gson();
//
//        Point3D A = new Point3D(0,0,0),
//                B = new Point3D(100,100,100),
//                C = new Point3D(20,20,0),
//                D = new Point3D(30,30,40);
//
//        ArrayList<Point3D> points = new ArrayList<>();
//        points.add(A);
//        points.add(B);
//        points.add(C);
//        points.add(D);
//
//        Triangle t1 = new Triangle(A,B,C),
//                t2 = new Triangle(A,B,D),
//                t3 = new Triangle(C,B,D),
//                t4 = new Triangle(A,C,D);
//
//        ArrayList<Triangle> triangles = new ArrayList<>();
//        triangles.add(t1);
//        triangles.add(t2);
//        triangles.add(t3);
//        triangles.add(t4);
//
//        Shape shape = new Shape(points, triangles);

//        Shape shape = new ShapeReader("src\\shapes\\assets\\tetrahedron.json").read();
//        for(Point3D point : shape.getPoints()) System.out.println(point);
        new Thread(new Runnable() {
            @Override
            public void run() {
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
