package Core.Utilities;

import Core.GeoSystem.Points.Functions.*;
import Core.GeoSystem.Points.PointTypes.Point;

public class GeoEngine {
    /** SECTION: Core Functions ===================================================================================== */
    public static Point midpoint(String n, Point a, Point b) {
        return Midpoint.create(n, a, b);
    }

    public static Point centroid(String n, Point ... args) {
        return Centroid.create(n, args);
    }

    public static Point circumcenter(String n, Point a, Point b, Point c) {
        return Circumcenter.create(n, a, b, c);
    }
}
