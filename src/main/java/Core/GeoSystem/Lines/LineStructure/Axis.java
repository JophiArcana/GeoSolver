package Core.GeoSystem.Lines.LineStructure;

import Core.EntityStructure.MulticardinalStructure.MultiConstant;
import Core.GeoSystem.Points.PointStructure.Coordinate;
import Core.GeoSystem.Points.PointStructure.Point;

public class Axis extends MultiConstant implements Line {
    /** SECTION: Static Data ======================================================================================== */
    public enum Parameter implements InputType {
        COORDINATE
    }
    public static final InputType[] inputTypes = {Parameter.COORDINATE};

    public static int compare(Axis a1, Axis a2) {
        return Coordinate.compare(a1.pointDual, a2.pointDual);
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
