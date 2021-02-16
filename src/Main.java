

import com.aparapi.Kernel;
import geometry.objects3D.Point3D;
import geometry.objects3D.Vector3D;
import geometry.polygonal.Sphere;
import graph.Camera;
import graph.CanvasPanel;
import graph.Screen;
import physics.Space;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class Main {

    public static final int FocusLength = 300;
    public static final int Distance = 500;
    public static void main(String[] args) {
        Camera.Resolution resolution = new Camera.Resolution(1920, 1080);
        Camera camera = new Camera(new Screen(new Vector3D(FocusLength,0,0), new Point3D(-Distance,0,0)), resolution, 0);
        CanvasPanel canvas = new CanvasPanel(camera, Kernel.EXECUTION_MODE.JTP);

        //Куб
        Point3D A = new Point3D(-50, -50, -100),
                B = new Point3D(-50, -50, -200),
                C = new Point3D(-50, 50, -200),
                D = new Point3D(-50, 50, -100),
                A2 = new Point3D(50, -50, -100),
                B2 = new Point3D(50, -50, -200),
                C2 = new Point3D(50, 50, -200),
                D2 = new Point3D(50, 50, -100);


        Sphere s = new Sphere(A, 100, 15, Color.BLUE);
//        canvas.getPolygonals().add(s);
        canvas.prepare();
        canvas.setSize(1920, 1080);
        canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas.setLocationRelativeTo(null);
        canvas.setResizable(true);
        canvas.setUndecorated(false);
        canvas.setVisible(true);
        Space space = new Space(0.001f, 10f, canvas);

        new Thread(()->{
            while (true) {
                canvas.repaint();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
