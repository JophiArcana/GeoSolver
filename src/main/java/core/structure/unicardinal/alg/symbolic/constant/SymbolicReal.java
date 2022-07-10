package core.structure.unicardinal.alg.symbolic.constant;

import core.structure.unicardinal.alg.structure.Real;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;

public class SymbolicReal extends Real implements SymbolicExpression {
    /** SECTION: Factory Methods ==================================================================================== */
    public static SymbolicReal create(double value) {
        return (SymbolicReal) new SymbolicReal(value).close();
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected SymbolicReal(double value) {
        super(value);
    }
}
