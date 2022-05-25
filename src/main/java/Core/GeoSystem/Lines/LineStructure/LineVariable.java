package Core.GeoSystem.Lines.LineStructure;

import Core.EntityStructure.MulticardinalStructure.MultiVariable;
import Core.GeoSystem.Points.PointStructure.*;

public class LineVariable extends MultiVariable implements Line {
    /** SECTION: Instance Variables ================================================================================= */
    public PointVariable pointDual;

    /** SECTION: Factory Methods ==================================================================================== */
    public static LineVariable create(String n) {
        return new LineVariable(n);
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected LineVariable(String n) {
        super(n);
        this.pointDual = PointVariable.create(n + "\u209A");
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Line ============================================================================================ */
    public Point pointDual() {
        return this.pointDual;
    }
}
