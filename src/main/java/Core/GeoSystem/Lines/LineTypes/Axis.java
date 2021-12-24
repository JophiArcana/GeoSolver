package Core.GeoSystem.Lines.LineTypes;

import Core.EntityTypes.*;
import Core.GeoSystem.MulticardinalTypes.Multiconstant;
import Core.GeoSystem.Points.PointTypes.Coordinate;
import Core.GeoSystem.Points.PointTypes.Point;

public class Axis extends Multiconstant implements Line {
    /** SECTION: Static Data ======================================================================================== */
    public enum Parameter implements InputType {
        COORDINATE
    }
    public static final InputType[] inputTypes = {Parameter.COORDINATE};

    /** SECTION: Instance Variables ================================================================================= */
    public final Coordinate pointDual;

    /** SECTION: Factory Methods ==================================================================================== */
    public static Axis create(String n, Coordinate c) {
        return new Axis(n, c);
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected Axis(String n, Coordinate c) {
        super(n);
        this.pointDual = c;
        this.inputs.get(Parameter.COORDINATE).add(this.pointDual);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public InputType[] getInputTypes() {
        return Axis.inputTypes;
    }

    /** SUBSECTION: Immutable ======================================================================================= */
    public int compareTo(Immutable immutable) {
        if (immutable instanceof Axis axis) {
            return this.pointDual.compareTo(axis.pointDual);
        } else {
            return Integer.MIN_VALUE;
        }
    }

    /** SUBSECTION: Line ============================================================================================ */
    public Point pointDual() {
        return this.pointDual;
    }
}
