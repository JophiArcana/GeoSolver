package Core.GeoSystem.Points.PointFunctions;

import Core.GeoSystem.Points.PointFunctions.Center;
import Core.GeoSystem.Points.PointStructure.Point;

public abstract class TriangleCenter extends Center {
    /** SECTION: Instance Variables ================================================================================= */
    public Point a, b, c;

    /** SECTION: Abstract Constructor =============================================================================== */
    public TriangleCenter(String n, Point a, Point b, Point c) {
        super(n, a, b, c);
        this.a = a;
        this.b = b;
        this.c = c;
    }
}
