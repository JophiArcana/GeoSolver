package core.structure.unicardinal.alg.symbolic;

import core.structure.unicardinal.alg.Variable;

public class SymbolicVariable extends Variable implements SymbolicExpression {
    /** SECTION: Factory Methods ==================================================================================== */
    public static SymbolicVariable create(String n, double v) {
        return new SymbolicVariable(n, v);
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
