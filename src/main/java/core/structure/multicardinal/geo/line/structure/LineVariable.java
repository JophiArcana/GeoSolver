package core.structure.multicardinal.geo.line.structure;

import core.structure.multicardinal.MultiVariable;
import core.structure.multicardinal.geo.point.structure.*;

public class LineVariable extends MultiVariable implements Line {
    /** SECTION: Instance Variables ================================================================================= */
    public PointVariable pointDual;

    /** SECTION: Factory Methods ==================================================================================== */
    public static LineVariable create(String n, double xStart, double yStart) {
        return new LineVariable(n, xStart, yStart, true);
    }

    public static LineVariable create(String n, double xStart, double yStart, boolean anon) {
        return new LineVariable(n, xStart, yStart, anon);
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected LineVariable(String n, double xStart, double yStart, boolean anon) {
        super(n, anon);
        this.pointDual = PointVariable.create(n + "\u209A", xStart, yStart);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Line ============================================================================================ */
    public Point pointDual() {
        return this.pointDual;
    }
}
