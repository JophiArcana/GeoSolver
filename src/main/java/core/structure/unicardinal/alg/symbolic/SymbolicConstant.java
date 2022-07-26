package core.structure.unicardinal.alg.symbolic;

import core.Propositions.equalitypivot.unicardinal.LockedUnicardinalPivot;
import core.structure.unicardinal.Constant;

public class SymbolicConstant extends Constant implements SymbolicExpression {
    /** SECTION: Static Data ======================================================================================== */
    public static final LockedUnicardinalPivot<SymbolicExpression, SymbolicConstant>
            ZERO = (LockedUnicardinalPivot<SymbolicExpression, SymbolicConstant>) new SymbolicConstant(0).close(),
            ONE = (LockedUnicardinalPivot<SymbolicExpression, SymbolicConstant>) new SymbolicConstant(1).close();

    /** SECTION: Factory Methods ==================================================================================== */
    public static LockedUnicardinalPivot<SymbolicExpression, SymbolicConstant> create(double value) {
        if (value == 0) {
            return SymbolicConstant.ZERO;
        } else if (value == 1) {
            return SymbolicConstant.ONE;
        } else {
            return (LockedUnicardinalPivot<SymbolicExpression, SymbolicConstant>) new SymbolicConstant(value).close();
        }
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected SymbolicConstant(double value) {
        super(value);
    }
}
