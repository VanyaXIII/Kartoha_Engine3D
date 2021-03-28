

import com.aparapi.Kernel;
import com.google.gson.Gson;
import geometry.Triangle;
import geometry.objects3D.Point3D;
import geometry.objects3D.Vector3D;
import graph.Camera;
import graph.CanvasPanel;
import graph.Screen;
import physics.Space;
import geometry.Shape;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class Main {

    public static final int FocusLength = 300;
    public static final int Distance = 500;

    public static void main(String[] args){
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
        Space space = new Space(0.008d, 00d, canvas);

        Point3D A = new Point3D(0,0,0),
                B = new Point3D(200,200,0),
                C = new Point3D(200,0,0),
                D = new Point3D(0,200,0),
                E = new Point3D(100, 100, 173),
                F = new Point3D(100, 100, -173);
//
        ArrayList<Point3D> points = new ArrayList<>();
        points.add(A);
        points.add(B);
        points.add(C);
        points.add(D);
        points.add(E);
        points.add(F);

        Triangle t1 = new Triangle(F, A, C),
                t2 = new Triangle(F, B, C),
                t3 = new Triangle(E, A, C),
                t5 = new Triangle(E, B, C),
                t4 = new Triangle(E, A, D),
                t6 = new Triangle(B, D, E),
                t7 = new Triangle(F, A, D),
                t8 = new Triangle(B, D, F);

        ArrayList<Triangle> triangles = new ArrayList<>();
        triangles.add(t1);
        triangles.add(t2);
        triangles.add(t3);
        triangles.add(t4);
        triangles.add(t5);
        triangles.add(t6);
        triangles.add(t7);
        triangles.add(t8);


        new Thread(() -> {
            while (true) {
                synchronized (canvas) {
                    canvas.repaint();
                    space.changeTime();
                }
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
