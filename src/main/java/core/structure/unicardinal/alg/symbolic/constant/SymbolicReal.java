package core.structure.unicardinal.alg.symbolic.constant;

import core.structure.unicardinal.alg.structure.Real;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;
import core.Diagram;

public class SymbolicReal extends Real implements SymbolicExpression {
    /** SECTION: Factory Methods ==================================================================================== */
    public static SymbolicReal create(Diagram d, double value) {
        return new SymbolicReal(d, value);
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected SymbolicReal(Diagram d, double value) {
        super(d, value);
    }
}
