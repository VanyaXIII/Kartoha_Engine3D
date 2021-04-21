package physical_objects;

import drawing.Drawable;
import exceptions.ImpossibleObjectException;
import geometry.objects3D.Point3D;
import geometry.objects3D.Vector3D;
import physics.Material;
import physics.Space;

/**
 * Абстарктное физическое тело
 */
public abstract class AbstractBody implements Drawable{

    protected double x0, y0, z0;
    protected Vector3D v;
    protected Vector3D a;
    protected Vector3D w;
    protected final Material material;
    protected final double m;
    protected transient Space space;

    /**
     *Конструктор
     * @param space пространство, в котором находится тело
     * @param x0 начальная координата по Ox
     * @param y0 начальная координата по Oy
     * @param z0 начальная координата по Oz
     * @param v начальная скорость
     * @param w начальная угловая скорость
     * @param material материал, из которого сделано тело
     * @param m масса тела
     * @throws ImpossibleObjectException исключение в случае попытки создания тела с нулевой массой
     */
    protected AbstractBody(Space space, double x0, double y0, double z0, Vector3D v, Vector3D w, Material material, double m) throws ImpossibleObjectException {
        this.space = space;
        this.v = v;
        this.w = w;
        this.a = space.getG(this);
        this.x0 = x0;
        this.y0 = y0;
        this.z0 = z0;
        this.material = material;
        this.m = m;
        if (m <= 0d) throw new ImpossibleObjectException("Impossible object; mass is null");
    }

    /**
     *Конструктор
     * @param space пространство, в котором находится тело
     * @param x0 начальная координата по Ox
     * @param y0 начальная координата по Oy
     * @param z0 начальная координата по Oz
     * @param material материал, из которого сделано тело
     * @param m масса тела
     * @throws ImpossibleObjectException исключение в случае попытки создания тела с нулевой массой
     */
    protected AbstractBody(Space space, double x0, double y0, double z0, Material material, double m) throws ImpossibleObjectException {
        this(space, x0, y0, z0, new Vector3D(0,0,0), new Vector3D(0,0,0), material, m);
    }

    /**
     * Метод, обновляющий положение тела и др данные
     */
    public synchronized void update() {
        changeSpeed();
        updateDrawingInterpretation();
        x0 += v.x * space.getDT() + a.x * space.getDT() * space.getDT() / 2d;
        y0 += v.y * space.getDT() + a.y * space.getDT() * space.getDT() / 2d;
        z0 += v.z * space.getDT() + a.z * space.getDT() * space.getDT() / 2d;
        a = space.getG(this);
    }

    /**
     * Метод, обноляющий скорость тела
     */
    private synchronized void changeSpeed() {
        v = new Vector3D(v.x + a.x * space.getDT(),
                v.y + a.y * space.getDT(),
                v.z + a.z * space.getDT());
    }

    /**
     * @param mode считать ли в будущем положении
     * @return Точка - центр масс тела
     */
    public final synchronized Point3D getPositionOfCentre(boolean mode) {
        double m = mode ? 1.0f : 0.0f;
        return new Point3D(x0 + m * v.x * space.getDT() + m * a.x * space.getDT() * space.getDT() / 2d,
                y0 + m * v.y * space.getDT() + m * a.y * space.getDT() * space.getDT() / 2d,
                z0 + m * v.z * space.getDT() + m * a.z * space.getDT() * space.getDT() / 2d);
    }

    /**
     * @return Масса тела
     */
    public double getM() {
        return m;
    }

    /**
     * @return Скорость тела
     */
    public Vector3D getV() {
        return v;
    }

    /**
     * @return Угловая скорость тела
     */
    public Vector3D getW() {
        return w;
    }

    /**
     * @return Позиция тела
     */
    public Point3D getPos(){
        return new Point3D(x0, y0, z0);
    }

    /**
     * @return Ускорение тела
     */
    public Vector3D getA(){
        return a;
    }

    /**
     * @return Материал, из которого сделано тело
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * @return Пространство, в котором находится тело
     */
    public Space getSpace() {
        return space;
    }

    /** Метод, инициализирцющий пространство, в котором находится объект
     * @param space пространство
     */
    public void initSpace(Space space){
        this.space = space;
    }

    /** Метод, задающий новую скорость телу
     * @param v новая скорость
     */
    public synchronized void setV(Vector3D v) {
        this.v = v;
    }

    /**
     *Метод, задающий новую угловую скорость телу
     * @param w новая угловая скорость
     */
    public synchronized void setW(Vector3D w) {
        this.w = w;
    }
}
