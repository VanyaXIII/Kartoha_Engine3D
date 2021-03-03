

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

public class Main {

    public static final int FocusLength = 300;
    public static final int Distance = 500;

    public static void main(String[] args) {
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
        Space space = new Space(0.001f, 00f, canvas);

        while (true) {
            canvas.repaint();
            space.changeTime();
        }
    }
}
