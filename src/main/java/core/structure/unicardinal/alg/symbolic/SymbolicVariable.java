package core.structure.unicardinal.alg.symbolic;

import core.structure.equalitypivot.LockedEqualityPivot;
import core.structure.unicardinal.Variable;

public class SymbolicVariable extends Variable implements SymbolicExpression {
    /** SECTION: Factory Methods ==================================================================================== */
    public static LockedEqualityPivot<SymbolicExpression, SymbolicVariable> create(String n, double v) {
        return (LockedEqualityPivot<SymbolicExpression, SymbolicVariable>) new SymbolicVariable(n, v).close();
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected SymbolicVariable(String n, double v) {
        super(n, v);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Expression ====================================================================================== */
    public int getDegree() {
        if (this.name.charAt(this.name.length() - 1) == '\u209C') {
            return 0;
        } else {
            return 1;
        }
    }
}
