

import com.aparapi.Kernel;
import com.google.gson.Gson;
import geometry.objects3D.Point3D;
import geometry.objects3D.Vector3D;
import graph.Camera;
import graph.CanvasPanel;
import graph.Screen;
import physics.Space;

import javax.swing.*;

public class Main {

    public static final int FocusLength = 300;
    public static final int Distance = 500;

    public static void main(String[] args) throws InterruptedException {

        Camera.Resolution resolution = new Camera.Resolution(1920, 1080);
        Camera camera = new Camera(new Screen(new Vector3D(FocusLength, 0, 0), new Point3D(-Distance, 500, 2000)), resolution);
        CanvasPanel canvas = new CanvasPanel(camera, Kernel.EXECUTION_MODE.JTP);
        canvas.setTitle("Картоха Engine");
        canvas.setSize(1920, 1080);
        canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas.setLocationRelativeTo(null);
        canvas.setResizable(true);
        canvas.setUndecorated(false);
        canvas.setVisible(true);
        Space space = new Space(0.01d, 00d, canvas);


        Gson gson = new Gson();

//        PhysicalSphere sphere = gson.fromJson(gson.toJson(space.getSpheres().get(0)), PhysicalSphere.class);
//        System.out.println(sphere.getSpace());



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
