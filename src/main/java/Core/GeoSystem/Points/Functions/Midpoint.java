package Core.GeoSystem.Points.Functions;

import Core.EntityTypes.Entity;
import Core.GeoSystem.Points.Point;

public class Midpoint extends Centroid {
    public Midpoint(String n, Point a, Point b) {
        super(n, a, b);
    }

    public Midpoint(Point a, Point b) {
        super("", a, b);
    }
}
