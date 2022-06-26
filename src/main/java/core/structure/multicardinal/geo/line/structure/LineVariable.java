package core.structure.multicardinal.geo.line.structure;

import core.Diagram;
import core.structure.multicardinal.MultiVariable;
import core.structure.multicardinal.geo.point.structure.*;

public class LineVariable extends MultiVariable implements Line {
    /** SECTION: Instance Variables ================================================================================= */
    public PointVariable pointDual;

    /** SECTION: Factory Methods ==================================================================================== */
    public static LineVariable create(Diagram d, String n, double xStart, double yStart) {
        return new LineVariable(d, n, xStart, yStart);
    }

    public static LineVariable create(Diagram d, String n, double xStart, double yStart, boolean anon) {
        return new LineVariable(d, n, xStart, yStart, anon);
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected LineVariable(Diagram d, String n, double xStart, double yStart) {
        super(d, n);
        this.pointDual = PointVariable.create(d, n + "\u209A", xStart, yStart);
    }

    protected LineVariable(Diagram d, String n, double xStart, double yStart, boolean anon) {
        super(d, n, anon);
        this.pointDual = PointVariable.create(d, n + "\u209A", xStart, yStart);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Line ============================================================================================ */
    public Point pointDual() {
        return this.pointDual;
    }
}
