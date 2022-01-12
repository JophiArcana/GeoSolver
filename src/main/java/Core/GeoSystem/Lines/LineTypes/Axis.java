package Core.GeoSystem.Lines.LineTypes;

import Core.AlgSystem.UnicardinalTypes.Constant;
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

    public static int compare(Axis a1, Axis a2) {
        return Constant.compare(a1.pointDual.value, a2.pointDual.value);
    }

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

    /** SUBSECTION: Line ============================================================================================ */
    public Point pointDual() {
        return this.pointDual;
    }
}
