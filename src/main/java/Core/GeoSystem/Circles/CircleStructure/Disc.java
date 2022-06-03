package Core.GeoSystem.Circles.CircleStructure;

import Core.AlgSystem.Constants.Real;
import Core.AlgSystem.UnicardinalRings.Symbolic;
import Core.EntityStructure.MulticardinalStructure.MultiConstant;
import Core.EntityStructure.UnicardinalStructure.Expression;
import Core.GeoSystem.Points.PointStructure.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class Disc extends MultiConstant implements Circle {
    /** SECTION: Static Data ======================================================================================== */
    public enum Parameter implements InputType {
        CENTER, RADIUS
    }
    public static final InputType[] inputTypes = {Disc.Parameter.CENTER, Parameter.RADIUS};

    public static int compare(Disc d1, Disc d2) {
        if (d1.radius != d2.radius) {
            return Double.compare(d1.radius.value, d2.radius.value);
        } else {
            return Coordinate.compare(d1.center, d2.center);
        }
    }

    /** SECTION: Instance Variables ================================================================================= */
    public final Coordinate center;
    public final Real<Symbolic> radius;

    /** SECTION: Factory Methods ==================================================================================== */
    public static Disc create(String n, double x, double y, double r) {
        return new Disc(n, Coordinate.create(n + "\u2092", x, y), new Real<>(r, Symbolic.class));
    }

    public static Disc create(String n, Coordinate c, double r) {
        return new Disc(n, c, new Real<>(r, Symbolic.class));
    }


    /** SECTION: Protected Constructors ============================================================================= */
    protected Disc(String n, Coordinate c, Real<Symbolic> r) {
        super(n);
        this.center = c;
        this.radius = r;
        this.inputs.get(Parameter.CENTER).add(this.center);
        this.inputs.get(Parameter.RADIUS).add(this.radius);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public ArrayList<Expression<Symbolic>> symbolic() {
        return new ArrayList<>(List.of(this.center.x, this.center.y, this.radius));
    }
}
