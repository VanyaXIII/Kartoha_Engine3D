

import com.aparapi.Kernel;
import com.google.gson.Gson;
import geometry.objects3D.Point3D;
import geometry.objects3D.Vector3D;
import graph.Camera;
import graph.CanvasPanel;
import graph.Screen;
import java.io.BufferedWriter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import physical_objects.PhysicalPolyhedron;
import org.json.JSONException;
import java.io.OutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import physics.PhysicsHandler;
import physics.Space;

import javax.swing.*;

public class Main {

    public static final int FocusLength = 300;
    public static final int Distance = 500;

    public static void main(String[] args) throws InterruptedException {
        load();
    }

    /**
     * Метод-загрузчик сцены
     * @throws InterruptedException
     */
    public static void load() throws InterruptedException {
        while (true) {
            JFileChooser fileopen = new JFileChooser();
            int ret = fileopen.showDialog(null, "Открыть сцену");
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = fileopen.getSelectedFile();
                Camera.Resolution resolution = new Camera.Resolution(1920, 1080);
                Camera camera = new Camera(
                    new Screen(new Vector3D(FocusLength, 0, 0), new Point3D(-Distance, 500, 2000)),
                    resolution);
                CanvasPanel canvas = new CanvasPanel(camera, Kernel.EXECUTION_MODE.JTP);
                canvas.setTitle("Картоха Engine");
                canvas.setSize(1920, 1080);
                canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                canvas.setLocationRelativeTo(null);
                canvas.setResizable(true);
                canvas.setUndecorated(false);
                canvas.setVisible(true);
                try {

                    Space space = new Space(0.01d, 00d, canvas, file);
                    canvas.addKeyListener(new KeyListener() {
                        @Override
                        public void keyTyped(KeyEvent e) {

                        }

                                        fileNotFoundException.printStackTrace();
                        @Override
                                        os.write(space.save());
                                    } catch (IOException fileNotFoundException) {
                                JFileChooser fileopen = new JFileChooser();
                            if (e.getKeyCode() == KeyEvent.VK_B && e.isControlDown()) {
                                    try (BufferedWriter os = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))){
                                        ioException.printStackTrace();
                                    }
                                        file.createNewFile();
                        public void keyPressed(KeyEvent e) {
                                int ret = fileopen.showSaveDialog(null);
                                if (ret == JFileChooser.APPROVE_OPTION) {
                                    File file = fileopen.getSelectedFile();
                                    try {
                                    } catch (IOException ioException) {
                                    }
                                } else {
                                    System.exit(0);
                                }
                                try {
                                    load();
                                }
                            }
                                } catch (InterruptedException interruptedException) {
                        }
                                    interruptedException.printStackTrace();
                                canvas.setVisible(false);
                                canvas.dispose();
                            if (e.getKeyCode() == KeyEvent.VK_N && e.isControlDown()) {
                            }

                        public void keyReleased(KeyEvent e) {
                    Thread.sleep(2000);
                        }
                    });


                        @Override
                        while (true) {
                                canvas.repaint();
                            }
                                space.changeTime();
                            synchronized (canvas) {
                    new Thread(() -> {

                            try {
                                Thread.sleep(5);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                        }
                            }
                    }).start();
                    break;
                    canvas.setVisible(false);
                } catch (JSONException e) {
                    canvas.dispose();
                    JOptionPane.showMessageDialog(new JFrame(), "Файл не содержит сцены", "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
                }
            } else {
                System.exit(0);
            }
        }
    }

    public static void start(File f) throws InterruptedException {

    }
}
