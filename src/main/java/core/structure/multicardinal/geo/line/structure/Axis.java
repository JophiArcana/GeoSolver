package core.structure.multicardinal.geo.line.structure;

import core.structure.multicardinal.MultiConstant;
import core.structure.multicardinal.geo.point.structure.*;
import core.util.Utils;

import java.util.List;

public class Axis extends MultiConstant implements Line {
    /** SECTION: Static Data ======================================================================================== */
    public static final InputType<Coordinate> COORDINATE = new InputType<>(Coordinate.class, Utils.MULTICARDINAL_COMPARATOR);

    public static final List<InputType<?>> inputTypes = List.of(Axis.COORDINATE);

    public static int compare(Axis a1, Axis a2) {
        return Coordinate.compare(a1.pointDual, a2.pointDual);
    }

    /** SECTION: Instance Variables ================================================================================= */
    public final Coordinate pointDual;

    /** SECTION: Factory Methods ==================================================================================== */
    public static Axis create(String n, Coordinate c) {
        return new Axis(n, c, true);
    }

    public static Axis create(String n, Coordinate c, boolean anon) {
        return new Axis(n, c, anon);
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected Axis(String n, Coordinate c, boolean anon) {
        super(n, anon);
        this.pointDual = c;
        this.getInputs(Axis.COORDINATE).add(this.pointDual);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public List<InputType<?>> getInputTypes() {
        return Axis.inputTypes;
    }

    /** SUBSECTION: Line ============================================================================================ */
    public Point pointDual() {
        return this.pointDual;
    }
}
