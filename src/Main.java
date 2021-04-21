

import com.aparapi.Kernel;
import geometry.objects3D.Point3D;
import geometry.objects3D.Vector3D;
import graph.Camera;
import graph.CanvasPanel;
import graph.Screen;
import org.json.JSONException;
import physics.Space;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

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

                        @Override
                        public void keyPressed(KeyEvent e) {
                            if (e.getKeyCode() == KeyEvent.VK_B && e.isControlDown()) {
                                JFileChooser fileopen = new JFileChooser();
                                int ret = fileopen.showSaveDialog(null);
                                if (ret == JFileChooser.APPROVE_OPTION) {
                                    File file = fileopen.getSelectedFile();
                                    try {
                                        file.createNewFile();
                                    } catch (IOException ioException) {
                                        ioException.printStackTrace();
                                    }
                                    try (BufferedWriter os = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))){
                                        os.write(space.save());
                                    } catch (IOException fileNotFoundException) {
                                        fileNotFoundException.printStackTrace();
                                    }
                                } else {
                                    System.exit(0);
                                }
                            }
                            if (e.getKeyCode() == KeyEvent.VK_N && e.isControlDown()) {
                                canvas.setVisible(false);
                                canvas.dispose();
                                try {
                                    load();
                                } catch (InterruptedException interruptedException) {
                                    interruptedException.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void keyReleased(KeyEvent e) {

                        }
                    });

                    Thread.sleep(2000);

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
                    break;
                } catch (JSONException e) {
                    canvas.setVisible(false);
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
