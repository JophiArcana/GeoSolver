package Core.Utilities;

import Core.GeoSystem.Points.*;
import Core.GeoSystem.Points.Functions.*;

public class GeoEngine {
    /** SECTION: Core Functions ===================================================================================== */

    public static Point midpoint(String n, Point a, Point b) {
        return new Midpoint(n, a, b);
    }

    public static Point midpoint(Point a, Point b) {
        return new Midpoint(a, b);
    }

    public static Point centroid(String n, Point ... args) {
        return (Point) (new Centroid(n, args)).simplify();
    }

    public static Point centroid(Point ... args) {
        return (Point) (new Centroid(args)).simplify();
    }

    public static Point circumcenter(String n, Point a, Point b, Point c) {
        return new Circumcenter(n, a, b, c);
    }

    public static Point circumcenter(Point a, Point b, Point c) {
        return new Circumcenter(a, b, c);
    }
}
