package core.structure.multicardinal.geo.line.structure;

import core.structure.multicardinal.MultiVariable;
import core.structure.multicardinal.geo.point.structure.*;

public class LineVariable extends MultiVariable implements Line {
    /** SECTION: Instance Variables ================================================================================= */
    public PointVariable pointDual;

    /** SECTION: Factory Methods ==================================================================================== */
    public static LineVariable create(String n, double... args) {
        return new LineVariable(n, true, args);
    }

    public static LineVariable create(String n, boolean anon, double... args) {
        return new LineVariable(n, anon, args);
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected LineVariable(String n, boolean anon, double... args) {
        super(n, anon);
        this.pointDual = PointVariable.create(n + "\u209A", args);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Line ============================================================================================ */
    public Point pointDual() {
        return this.pointDual;
    }
}
