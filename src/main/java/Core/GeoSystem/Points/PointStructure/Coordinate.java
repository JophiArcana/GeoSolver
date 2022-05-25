package Core.GeoSystem.Points.PointStructure;

import Core.AlgSystem.Constants.Real;
import Core.AlgSystem.UnicardinalRings.*;
import Core.EntityStructure.UnicardinalStructure.*;
import Core.EntityStructure.MulticardinalStructure.*;

import java.util.*;

public class Coordinate extends MultiConstant implements Point {
    /** SECTION: Static Data ======================================================================================== */
    public enum Parameter implements InputType {
        X, Y
    }
    public static final InputType[] inputTypes = {Parameter.X, Parameter.Y};

    public static int compare(Coordinate c1, Coordinate c2) {
        if (c1.x.value != c2.x.value) {
            return Double.compare(c1.x.value, c2.x.value);
        } else {
            return Double.compare(c1.y.value, c2.y.value);
        }
    }

    /** SECTION: Instance Variables ================================================================================= */
    public final Real<Symbolic> x, y;

    /** SECTION: Factory Methods ==================================================================================== */
    public Coordinate create(String n, double x, double y) {
        return new Coordinate(n, new Real<>(x, Symbolic.class), new Real<>(y, Symbolic.class));
    }

    public Coordinate create(String n, Real<Symbolic> x, Real<Symbolic> y) {
        return new Coordinate(n, x, y);
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected Coordinate(String n, Real<Symbolic> x, Real<Symbolic> y) {
        super(n);
        this.x = x;
        this.y = y;
        this.inputs.get(Parameter.X).add(this.x);
        this.inputs.get(Parameter.Y).add(this.y);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public ArrayList<Expression<Symbolic>> symbolic() {
        return new ArrayList<>(List.of(this.x, this.y));
    }

    public InputType[] getInputTypes() {
        return Coordinate.inputTypes;
    }
}
