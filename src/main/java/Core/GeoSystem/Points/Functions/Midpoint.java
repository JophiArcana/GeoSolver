package Core.GeoSystem.Points.Functions;

import Core.GeoSystem.Points.PointTypes.Point;

public class Midpoint extends Centroid {
    public Midpoint(String n, Point a, Point b) {
        super(n, a, b);
    }

    public Midpoint(Point a, Point b) {
        super("", a, b);
    }
}
