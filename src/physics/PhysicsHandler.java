package physics;


import geometry.IntersectionalPair;
import physical_objects.PhysicalSphere;
import physical_objects.Wall;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class PhysicsHandler {

    private final ArrayList<PhysicalSphere> spheres;
    private final ArrayList<Wall> walls;
    private final int depth;

    PhysicsHandler(Space space, int depth) {
        spheres = space.getSpheres();
        walls = space.getWalls();
        this.depth = depth;
    }

    public void update() throws InterruptedException, ConcurrentModificationException {
        Thread sphereThread = new Thread(() -> {
            for (int i = 0; i < spheres.size() - 1; i++) {
                for (int j = i + 1; j < spheres.size(); j++) {
                    synchronized (spheres.get(i)) {
                        synchronized (spheres.get(j)) {
                            if (new IntersectionalPair<>(spheres.get(i), spheres.get(j)).areIntersected()) {
                                new CollisionalPair<>(spheres.get(i), spheres.get(j)).collide();
                            }

                        }
                    }
                }
            }
            spheres.forEach(sphere ->{
                    for (Wall wall : walls)
                        if (new IntersectionalPair<>(sphere, wall).areIntersected())
                            new CollisionalPair<>(sphere, wall).collide();
            });
        });

        sphereThread.start();
        sphereThread.join();

        sphereThread = new Thread(() -> {
            synchronized (spheres) {
                spheres.forEach(PhysicalSphere::update);
            }
        });


        sphereThread.start();
        sphereThread.join();
    }


}

