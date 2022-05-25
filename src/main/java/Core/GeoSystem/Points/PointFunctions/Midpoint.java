package Core.GeoSystem.Points.PointFunctions;

import Core.GeoSystem.Points.PointStructure.Point;

public class Midpoint extends Centroid {
    /** SECTION: Factory Methods ==================================================================================== */
    public static Midpoint create(String n, Point a, Point b) {
        return new Midpoint(n, a, b);
    }

    public static Midpoint create(Point a, Point b) {
        return new Midpoint("", a, b);
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected Midpoint(String n, Point a, Point b) {
        super(n, a, b);
    }
}
