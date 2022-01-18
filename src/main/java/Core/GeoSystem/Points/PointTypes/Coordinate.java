package Core.GeoSystem.Points.PointTypes;

import Core.AlgSystem.UnicardinalRings.*;
import Core.AlgSystem.UnicardinalTypes.*;
import Core.GeoSystem.MulticardinalTypes.Multiconstant;

import java.util.*;

public class Coordinate extends Multiconstant implements Point {
    /** SECTION: Static Data ======================================================================================== */
    public enum Parameter implements InputType {
        VALUE
    }
    public static final InputType[] inputTypes = {Parameter.VALUE};

    public static int compare(Coordinate c1, Coordinate c2) {
        return Constant.compare(c1.value, c2.value);
    }

    /** SECTION: Instance Variables ================================================================================= */
    public final Constant<Symbolic> value;

    /** SECTION: Factory Methods ==================================================================================== */
    public Coordinate create(String n, Constant<Symbolic> v) {
        return new Coordinate(n, v);
    }

    public Coordinate create(Constant<Symbolic> v) {
        return new Coordinate("", v);
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected Coordinate(String n, Constant<Symbolic> v) {
        super(n);
        this.value = v;
        this.inputs.get(Parameter.VALUE).add(this.value);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public ArrayList<Expression<Symbolic>> symbolic() {
        return new ArrayList<>(Collections.singletonList(this.value));
    }

    public InputType[] getInputTypes() {
        return Coordinate.inputTypes;
    }
}
