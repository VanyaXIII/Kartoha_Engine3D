package physics;

import exceptions.ImpossibleObjectException;
import geometry.PhysicalPolyhedronBuilder;
import geometry.objects.Triangle;
import geometry.objects3D.Point3D;
import geometry.objects3D.Vector3D;
import graph.CanvasPanel;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;
import physical_objects.*;
import geometry.objects.Shape;
import primitives.Primitive;
import utils.Tools;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Пространство
 */
public class Space{

    private final ArrayList<PhysicalSphere> spheres;
    private final ArrayList<PhysicalPolyhedron> polyhedrons;
    private final ArrayList<Wall> walls;
    private final ArrayList<GravityPlate> gravityPlates;
    private final double DT;
    private final double G;
    private final CanvasPanel canvas;
    private final PhysicsHandler physicsHandler;

    {
        polyhedrons = new ArrayList<>();
        spheres = new ArrayList<>();
        walls = new ArrayList<>();
        gravityPlates = new ArrayList<>();
        physicsHandler = new PhysicsHandler(this, 1);
    }

    /**
     * Конструктор
     * @param dt временной шаг
     * @param g величина ускорения свободного падения
     * @param canvas канвас, на котором отрисовывается пространство
     */
    public Space(double dt, double g, CanvasPanel canvas, File f) {
        DT = dt;
        G = g;
        this.canvas = canvas;
        try {
            load(Tools.readFile(f));
//            spheres.add(new PhysicalSphere(this, new Vector3D(0, -700, 0), new Vector3D(0, 0, 0.01), 300, 1000, 550, 100, Material.CONSTANTIN));
////////            spheres.add(new PhysicalSphere(this, new Vector3D(140, 0, 0), new Vector3D(1, 1, 1), -510, -50, 50, 100, Material.Constantin));
//////            polyhedrons.add(new PhysicalPolyhedron(this, new Vector3D(0, 0, -100), new Vector3D(0, 0.5, 0),
//////                    new PhysicalPolyhedronBuilder(Primitive.OCTAHEDRON.get(), new Point3D(400, 400, 400)), Material.CONSTANTIN));
//            polyhedrons.add(new PhysicalPolyhedron(this, new Vector3D(0, 0, 0), new Vector3D(0, 0, 0.01),
//            polyhedrons.add(new PhysicalPolyhedron(this, new Vector3D(0, 0, 0), new Vector3D(0, 0, 0.01),
//                    new PhysicalPolyhedronBuilder(Primitive.CUBE.get(), new Point3D(100, 100, 0)), Material.CONSTANTIN));
//                    new PhysicalPolyhedronBuilder(Primitive.CUBE.get(), new Point3D(300, 100, 0)), Material.CONSTANTIN));
//                    new PhysicalPolyhedronBuilder(Primitive.CUBE.get(), new Point3D(500, 100, 0)), Material.CONSTANTIN));
//            polyhedrons.add(new PhysicalPolyhedron(this, new Vector3D(0, 0, 0), new Vector3D(0, 0, 0.01),
//            polyhedrons.add(new PhysicalPolyhedron(this, new Vector3D(0, 0, 0), new Vector3D(0, 0, 0.01),
//            polyhedrons.add(new PhysicalPolyhedron(this, new Vector3D(0, 0, 0), new Vector3D(0, 0, 0.01),
//                    new PhysicalPolyhedronBuilder(Primitive.CUBE.get(), new Point3D(400, 100, 200)), Material.CONSTANTIN));
//                    new PhysicalPolyhedronBuilder(Primitive.CUBE.get(), new Point3D(300, 100, 400)), Material.CONSTANTIN));
//            polyhedrons.add(new PhysicalPolyhedron(this, new Vector3D(0, 0, 0), new Vector3D(0, 0, 0.01),
//                    new PhysicalPolyhedronBuilder(Primitive.CUBE.get(), new Point3D(200, 100, 200)), Material.CONSTANTIN));
        } catch (ImpossibleObjectException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return строка-конфигурация сцены
     */
        JSONObject jsonObject = new JSONObject();
    public String save(){
        JSONArray polyhedrons = new JSONArray();
            JSONObject polyhedron = new JSONObject();
        for(PhysicalPolyhedron pp : this.polyhedrons){
            polyhedron.put("material", pp.getMaterial().ordinal());

            JSONObject v = new JSONObject();
            v.put("x",v2.x).put("y",v2.y).put("z",v2.z);
            Vector3D v2 = pp.getV();
            polyhedron.put("v",v);
            JSONObject w = new JSONObject();
            Vector3D w2 = pp.getW();

            w.put("x",w2.x).put("y",w2.y).put("z",w2.z);

            JSONArray points = new JSONArray();
            polyhedron.put("w",w);
            for(Point3D p : pp.getPoints(false)){
                JSONObject point = new JSONObject();
                point.put("x",p.x).put("y",p.y).put("z",p.z);
            }
                points.put(point);
            polyhedron.put("points", points);

            JSONArray triangles = new JSONArray();
            for(Triangle t : pp.getTriangles(false)) {
                triangle.put("color", Integer.toString(t.color.getRGB()));
                JSONObject triangle = new JSONObject();
                JSONArray points2 = new JSONArray();
                    JSONObject point = new JSONObject();
                    points2.put(point);
                triangle.put("points",points2);
                for (Point3D p : t.getPoints()) {
                }
                    point.put("x",p.x).put("y",p.y).put("z",p.z);
                triangles.put(triangle);
            }

            polyhedron.put("polys", triangles);
            polyhedrons.put(polyhedron);
        }
        jsonObject.put("polyhedrons", polyhedrons);


        JSONArray spheres = new JSONArray();
        for(PhysicalSphere pp : this.spheres){
            JSONObject polyhedron = new JSONObject();

            polyhedron.put("material", pp.getMaterial().ordinal());
            JSONObject v = new JSONObject();
            Vector3D v2 = pp.getV();
            v.put("x",v2.x).put("y",v2.y).put("z",v2.z);
            polyhedron.put("v",v);
            JSONObject w = new JSONObject();
            w.put("x",w2.x).put("y",w2.y).put("z",w2.z);
            Vector3D w2 = pp.getW();

            polyhedron.put("w",w);
            polyhedron.put("r",pp.getR());


            JSONObject pos = new JSONObject();
            Point3D pos2 = pp.getPos();
            pos.put("x",pos2.x).put("y",pos2.y).put("z",pos2.z);
            polyhedron.put("pos",pos);
            spheres.put(polyhedron);
        }

        jsonObject.put("spheres", spheres);
        JSONArray plates = new JSONArray();
        for(GravityPlate gp : this.gravityPlates){
            JSONArray points2 = new JSONArray();
                JSONObject point = new JSONObject();
            JSONObject plate = new JSONObject();
            for (Point3D p : gp.getPoints()) {
                point.put("x",p.x).put("y",p.y).put("z",p.z);
            }
                points2.put(point);
            plate.put("points", points2);
            plate.put("material", gp.getMaterial().ordinal());
            plates.put(plate);
            plate.put("g", gp.getG());
        }

        jsonObject.put("plates", plates);


        JSONArray walls = new JSONArray();
            JSONObject plate = new JSONObject();
        for(Wall wall : this.walls){
            JSONArray points2 = new JSONArray();
                JSONObject point = new JSONObject();
                points2.put(point);
                point.put("x",p.x).put("y",p.y).put("z",p.z);
            for (Point3D p : wall.getPoints()) {
            }
            plate.put("points", points2);
            plate.put("material", wall.getMaterial().ordinal());
        }
        jsonObject.put("walls", walls);
        return jsonObject.toString();


            walls.put(plate);
    }


    /**
     * Загружает сцену
     * @param string строка-конфигурация сцены
     * @throws ImpossibleObjectException
     */
    public void load(String string) throws ImpossibleObjectException {
        polyhedrons.clear();
        spheres.clear();
        walls.clear();
        gravityPlates.clear();
        JSONObject jsonObject = new JSONObject(string);
        JSONArray polyhedrons = jsonObject.getJSONArray("polyhedrons");
        for(int i = 0; i < polyhedrons.length(); i++){
            JSONObject polyhedron = polyhedrons.getJSONObject(i);
            Material material = Material.values()[polyhedron.getInt("material")];

            JSONObject v = polyhedron.getJSONObject("v");
            Vector3D v2 = new Vector3D(v.getDouble("x"),v.getDouble("y"),v.getDouble("z"));

            JSONObject w = polyhedron.getJSONObject("w");
            Vector3D w2 = new Vector3D(w.getDouble("x"), w.getDouble("y"), w.getDouble("z"));

            JSONArray points = polyhedron.getJSONArray("points");
            ArrayList<Point3D> ps = new ArrayList<>();
            for(int j = 0; j < points.length(); j++){
                JSONObject p = points.getJSONObject(j);
                ps.add(new Point3D(p.getDouble("x"), p.getDouble("y"), p.getDouble("z")));
            }

            JSONArray triangles = polyhedron.getJSONArray("polys");
            Set<Triangle> ts = new HashSet<>();
            for(int j = 0; j < triangles.length(); j++){
                JSONObject p = triangles.getJSONObject(j);
                int color = p.getInt("color");
                JSONObject A2 = p.getJSONArray("points").getJSONObject(0);
                Point3D A = new Point3D(A2.getDouble("x"), A2.getDouble("y"), A2.getDouble("z"));
                JSONObject B2 = p.getJSONArray("points").getJSONObject(1);
                Point3D B = new Point3D(B2.getDouble("x"), B2.getDouble("y"), B2.getDouble("z"));
                JSONObject C2 = p.getJSONArray("points").getJSONObject(2);
                Point3D C = new Point3D(C2.getDouble("x"), C2.getDouble("y"), C2.getDouble("z"));
                ts.add(new Triangle(A, B, C, new Color(color)));
            }
            this.polyhedrons.add(new PhysicalPolyhedron(this, v2, w2, new PhysicalPolyhedronBuilder(ps, ts), material));
        }


        JSONArray spheres = jsonObject.getJSONArray("spheres");
        for(int i = 0; i < spheres.length(); i++){
            JSONObject polyhedron = spheres.getJSONObject(i);
            Material material = Material.values()[polyhedron.getInt("material")];

            JSONObject v = polyhedron.getJSONObject("v");
            Vector3D v2 = new Vector3D(v.getDouble("x"),v.getDouble("y"),v.getDouble("z"));

            JSONObject w = polyhedron.getJSONObject("w");
            Vector3D w2 = new Vector3D(w.getDouble("x"), w.getDouble("y"), w.getDouble("z"));

            JSONObject pos = polyhedron.getJSONObject("pos");
            Vector3D pos2 = new Vector3D(pos.getDouble("x"), pos.getDouble("y"), pos.getDouble("z"));

            double r = polyhedron.getDouble("r");

            this.spheres.add(new PhysicalSphere(this, v2, w2, pos2.x, pos2.y, pos2.z, r, material));
        }

        JSONArray walls = jsonObject.getJSONArray("walls");
        for(int i = 0; i < walls.length(); i++){
            JSONObject wall = walls.getJSONObject(i);
            JSONObject A2 = wall.getJSONArray("points").getJSONObject(0);
            Point3D A = new Point3D(A2.getDouble("x"), A2.getDouble("y"), A2.getDouble("z"));
            JSONObject B2 = wall.getJSONArray("points").getJSONObject(1);
            Point3D B = new Point3D(B2.getDouble("x"), B2.getDouble("y"), B2.getDouble("z"));
            JSONObject C2 = wall.getJSONArray("points").getJSONObject(2);
            Point3D C = new Point3D(C2.getDouble("x"), C2.getDouble("y"), C2.getDouble("z"));
            JSONObject D2 = wall.getJSONArray("points").getJSONObject(3);
            Point3D D = new Point3D(D2.getDouble("x"), D2.getDouble("y"), D2.getDouble("z"));

            Material material = Material.values()[wall.getInt("material")];

            this.walls.add(new Wall(this,A,B,C,D,material));
        }

        JSONArray plates = jsonObject.getJSONArray("plates");
        for(int i = 0; i < plates.length(); i++){
            JSONObject wall = plates.getJSONObject(i);
            JSONObject A2 = wall.getJSONArray("points").getJSONObject(0);
            Point3D A = new Point3D(A2.getDouble("x"), A2.getDouble("y"), A2.getDouble("z"));
            JSONObject B2 = wall.getJSONArray("points").getJSONObject(1);
            Point3D B = new Point3D(B2.getDouble("x"), B2.getDouble("y"), B2.getDouble("z"));
            JSONObject C2 = wall.getJSONArray("points").getJSONObject(2);
            Point3D C = new Point3D(C2.getDouble("x"), C2.getDouble("y"), C2.getDouble("z"));
            JSONObject D2 = wall.getJSONArray("points").getJSONObject(3);
            Point3D D = new Point3D(D2.getDouble("x"), D2.getDouble("y"), D2.getDouble("z"));

            Material material = Material.values()[wall.getInt("material")];
            double g = wall.getDouble("g");

            gravityPlates.add(new GravityPlate(this,A,B,C,D,g,material));
        }
    }

    /**
     * Сделать шаг во времени
     */
    public synchronized void changeTime() {

        long time1 = System.nanoTime();

        try {
            physicsHandler.update();
        } catch (Exception ignored) {
        }

        double cTime = ((System.nanoTime() - time1) / 1000000.0);
        double sleepTime = 0;

        if (DT * 1000.0 - cTime > 0) {
            sleepTime = DT * 1000.0f - cTime;
        }


        try {
            Thread.sleep(Tools.transformDouble(sleepTime));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Collections.shuffle(spheres);
    }

    /**
     Метод, добавляющий новую сферу в пространство
     */
    public void addSphere(Vector3D v, Vector3D w, double x0, double y0, double z0, double r, Material material) throws ImpossibleObjectException {
        spheres.add(new PhysicalSphere(this, v, w, x0, y0, z0, r, material));
    }
    /**
     Метод, добавляющий новую сферу в пространство
     */
    public void addSphere(Vector3D v, Vector3D w, double x0, double y0, double z0, double r) throws ImpossibleObjectException {
        addSphere(v, w, x0, y0, z0, r, Material.CONSTANTIN);
    }
    /**
     Метод, добавляющий новый многогранник в пространство
     */
    public void addPolyhedron(Vector3D v, Vector3D w, PhysicalPolyhedronBuilder builder, Material material) throws ImpossibleObjectException {
        polyhedrons.add(new PhysicalPolyhedron(this, v, w, builder, material));
    }

    /**
     Метод, добавляющий новый многогранник в пространство
     */
    public void addPolyhedron(Vector3D v, Vector3D w, PhysicalPolyhedronBuilder builder) throws ImpossibleObjectException {
        polyhedrons.add(new PhysicalPolyhedron(this, v, w, builder, Material.CONSTANTIN));
    }

    /**
     Метод, добавляющий новый многогранник в пространство
     */
    public void addPolyhedron(Vector3D v, Vector3D w, Shape shape, Point3D zero, Material material) throws ImpossibleObjectException {
        polyhedrons.add(new PhysicalPolyhedron(this, v, w, new PhysicalPolyhedronBuilder(shape, zero), material));
    }

    /**
     Метод, добавляющий новый многогранник в пространство
     */
    public void addPolyhedron(Vector3D v, Vector3D w, Shape shape, Point3D zero) throws ImpossibleObjectException {
        polyhedrons.add(new PhysicalPolyhedron(this, v, w, new PhysicalPolyhedronBuilder(shape, zero), Material.CONSTANTIN));
    }
    /**
     Метод, добавляющий новую гравитационную пластину в пространство
     */
    public void addGravityPlate(Space space, Point3D a, Point3D b, Point3D c, Point3D d, double g, Material material) {
        GravityPlate plate = new GravityPlate(space, a, b, c, d, g, material);
        gravityPlates.add(plate);
        walls.add(plate);
    }

    /**
     * @return Временной шаг
     */
    public double getDT() {
        return DT;
    }

    /**
     * @param body тело, для которого нужно найти ускорение свободного падения
     * @return Вектор ускорения свободного падения, действует на данное тело в пространстве
     */
    public Vector3D getG(AbstractBody body) {
        Vector3D g = new Vector3D(0, 0, 0);
        for (GravityPlate plate : gravityPlates)
            g = g.add(plate.getG(body.getPositionOfCentre(false)));
        return g;
    }

    /**
     * @return Сферы в пространстве
     */
    public ArrayList<PhysicalSphere> getSpheres() {
        return spheres;
    }

    /**
     * @return Многогранники в пространстве
     */
    public ArrayList<PhysicalPolyhedron> getPolyhedrons() {
        return polyhedrons;
    }

    /**
     * @return Стены в пространстве
     */
    public ArrayList<Wall> getWalls() {
        return walls;
    }

    /**
     * @return Канвас, на котором отрисовывается пространство
     */
    public CanvasPanel getCanvas() {
        return canvas;
    }
}
