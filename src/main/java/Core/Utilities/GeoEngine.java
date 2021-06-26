package Core.Utilities;

import Core.GeoSystem.Points.*;
import Core.GeoSystem.Points.Functions.Centroid;

public class GeoEngine {
    /** SECTION: Core Functions ===================================================================================== */

    public static Point midpoint(Point a, Point b) {
        return GeoEngine.centroid(a, b);
    }

    public static Point centroid(Point ... args) {
        return (Point) (new Centroid(args)).simplify();
    }
}
