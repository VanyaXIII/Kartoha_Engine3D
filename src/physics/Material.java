package physics;

import java.awt.*;

public class Material {

    public final float p;
    public final Color fillColor;
    public final float coefOfReduction;
    public final float coefOfFriction;

    private Material(float p, Color fillColor, float coefOfReduction, float coefOfFriction) {
        this.p = p;
        this.coefOfReduction = coefOfReduction;
        this.coefOfFriction = coefOfFriction;
        this.fillColor = fillColor;
    }

    public static final Material Steel = new Material(7900, new Color(175, 175, 175),0.5f, 0.1f);
    public static final Material Wood = new Material(500, new Color(124, 67, 11),1f, 0f);
    public static final Material Stone = new Material(2500, new Color(90, 90, 90),1f, 0.5f);
    public static final Material Gold = new Material(19300, new Color(238, 198, 0),1f, 0.0f);
    public static final Material LapisLazuli = new Material(2500, new Color(0, 34, 255), 0.9f, 0.1f);
    public static final Material Osmium = new Material(22500, new Color(148, 157, 191),1f, 0f);
    public static final Material Constantin = new Material(1000, Color.GREEN, 0.8f, 0.5f);
}
