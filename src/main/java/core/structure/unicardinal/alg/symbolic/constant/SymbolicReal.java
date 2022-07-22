package core.structure.unicardinal.alg.symbolic.constant;

import core.structure.equalitypivot.*;
import core.structure.unicardinal.alg.structure.Real;
import core.structure.unicardinal.alg.symbolic.SymbolicConstant;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;

public class SymbolicReal extends SymbolicConstant implements Real {
    /** SECTION: Static Data ======================================================================================== */
    public static final LockedEqualityPivot<SymbolicExpression, SymbolicReal>
            ZERO = (LockedEqualityPivot<SymbolicExpression, SymbolicReal>) new SymbolicReal(0).close(),
            ONE = (LockedEqualityPivot<SymbolicExpression, SymbolicReal>) new SymbolicReal(1).close();

    /** SECTION: Factory Methods ==================================================================================== */
    public static LockedEqualityPivot<SymbolicExpression, SymbolicReal> create(double value) {
        if (value == 0) {
            return SymbolicReal.ZERO;
        } else if (value == 1) {
            return SymbolicReal.ONE;
        } else {
            return (LockedEqualityPivot<SymbolicExpression, SymbolicReal>) new SymbolicReal(value).close();
        }
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected SymbolicReal(double value) {
        super(value);
    }
}
