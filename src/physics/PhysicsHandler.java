package physics;


import exceptions.ImpossiblePairException;
import geometry.IntersectionalPair;
import geometry.objects3D.Vector3D;
import physical_objects.PhysicalPolyhedron;
import physical_objects.PhysicalSphere;
import physical_objects.Wall;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class PhysicsHandler {

    private final ArrayList<PhysicalSphere> spheres;
    private final ArrayList<Wall> walls;
    private final ArrayList<PhysicalPolyhedron> polyhedrons;
    private final int depth;

    PhysicsHandler(Space space, int depth) {
        spheres = space.getSpheres();
        walls = space.getWalls();
        polyhedrons = space.getPolyhedrons();
        this.depth = depth;
    }

    public void update() throws InterruptedException, ConcurrentModificationException{
        Thread sphereThread = new Thread(() -> {
            for (int i = 0; i < spheres.size() - 1; i++) {
                for (int j = i + 1; j < spheres.size(); j++) {
                    synchronized (spheres.get(i)) {
                        synchronized (spheres.get(j)) {
                            try {
                                if (new IntersectionalPair<>(spheres.get(i), spheres.get(j)).areIntersected()) {
                                    new CollisionalPair<>(spheres.get(i), spheres.get(j)).collide();
                                }
                            } catch (ImpossiblePairException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
            }
            spheres.forEach(sphere ->{
                    for (Wall wall : walls) {
                        try {
                            if (new IntersectionalPair<>(sphere, wall.getTriangle()).areIntersected())
                                new CollisionalPair<>(sphere, wall).collide();
                        } catch (ImpossiblePairException e) {
                            e.printStackTrace();
                        }
                    }
            });
        });

        Thread polyhedronThread = new Thread(() -> polyhedrons.forEach(polyhedron -> {
            for (Wall wall : walls) {
                try {
                    if (new IntersectionalPair<>(polyhedron, wall.getTriangle()).areIntersected()) {
                        System.out.println(11111111);
                        new CollisionalPair<>(polyhedron, wall).collide();
                    }
                } catch (ImpossiblePairException e) {
                    e.printStackTrace();
                }
            }
            for (PhysicalSphere sphere : spheres){
                try {
                    if (new IntersectionalPair<>(polyhedron, sphere).areIntersected()){
                        sphere.setV(new Vector3D(0,0,0));
                        polyhedron.setV(new Vector3D(0,0,0));
                    }
                } catch (ImpossiblePairException e) {
                    e.printStackTrace();
                }
            }
        }));

        sphereThread.start();
        polyhedronThread.start();
        sphereThread.join();
        polyhedronThread.join();

        sphereThread = new Thread(() -> {
            synchronized (spheres) {
                spheres.forEach(PhysicalSphere::update);
            }
        });

        polyhedronThread = new Thread(() -> {
            synchronized (polyhedrons) {
                polyhedrons.forEach(PhysicalPolyhedron::update);
            }
        });


        sphereThread.start();
        polyhedronThread.start();
        polyhedronThread.join();
        sphereThread.join();
    }


}

