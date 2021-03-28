package physics;


import exceptions.ImpossiblePairException;
import geometry.IntersectionalPair;
import geometry.Triangle;
import geometry.intersections.SphereToPlaneIntersection;
import geometry.intersections.SpheresIntersection;
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

    public void update() throws InterruptedException {
        for (int i = 0; i< depth; i++){
            handlePhysics();
        }
    }

    private void handlePhysics() throws InterruptedException, ConcurrentModificationException {
        Thread sphereThread = new Thread(() -> {
            for (int i = 0; i < spheres.size() - 1; i++) {
                for (int j = i + 1; j < spheres.size(); j++) {
                    synchronized (spheres.get(i)) {
                        synchronized (spheres.get(j)) {
                            try {
                                if (new IntersectionalPair<>(spheres.get(i), spheres.get(j)).areIntersected()) {
                                    new CollisionalPair<>(spheres.get(i), spheres.get(j)).collide();
                                }
                                SpheresIntersection spherePair = new IntersectionalPair<>(spheres.get(i), spheres.get(j)).getSpheresIntersection();
                                if (spherePair.areIntersected) {
                                    spheres.get(i).pullFromSphere(spherePair);
                                }
                            } catch (ImpossiblePairException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
            }
            spheres.forEach(sphere -> {
                for (Wall wall : walls) {
                    try {
                        for (Triangle triangle : wall.getTriangles()) {
                            if (new IntersectionalPair<>(sphere, triangle).areIntersected()) {
                                new CollisionalPair<>(sphere, wall).collide();
                            }
                            SphereToPlaneIntersection pair = new IntersectionalPair<>(sphere, triangle).getSphereToPlaneIntersection();
                            if (pair.areIntersected)
                                sphere.pullFromPlane(pair);
                        }
                    } catch (ImpossiblePairException e) {
                        e.printStackTrace();
                    }
                }
            });
        });

        Thread polyhedronThread = new Thread(() -> {

            for (int i = 0; i < polyhedrons.size() - 1; i++) {
                for (int j = i + 1; j < polyhedrons.size(); j++) {
                    synchronized (polyhedrons.get(i)) {
                        synchronized (polyhedrons.get(j)) {
                            try {
                                if (new IntersectionalPair<>(polyhedrons.get(i), polyhedrons.get(j)).areIntersected()) {
                                    new CollisionalPair<>(polyhedrons.get(i), polyhedrons.get(j)).collide();
                                }
                            } catch (ImpossiblePairException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
            }


            polyhedrons.forEach(polyhedron -> {
            for (Wall wall : walls) {
                try {
                    for (Triangle triangle : wall.getTriangles())
                        if (new IntersectionalPair<>(polyhedron, triangle).areIntersected()) {
                            new CollisionalPair<>(polyhedron, wall).collide();
                            break;
                        }
                } catch (ImpossiblePairException e) {
                    e.printStackTrace();
                }
            }
            for (PhysicalSphere sphere : spheres) {
                try {
                    if (new IntersectionalPair<>(polyhedron, sphere).areIntersected()){
                        System.out.println(11111111);
                        new CollisionalPair<>(polyhedron, sphere).collide();
                    }
                } catch (ImpossiblePairException e) {
                    e.printStackTrace();
                }
            }
        });});

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

