package core.structure.unicardinal.alg.symbolic;

import core.Diagram;
import core.structure.unicardinal.alg.Variable;

public class SymbolicVariable extends Variable implements SymbolicExpression {
    /** SECTION: Factory Methods ==================================================================================== */
    public static SymbolicVariable create(Diagram d, String n, double v) {
        return new SymbolicVariable(d, n, v);
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected SymbolicVariable(Diagram d, String n, double v) {
        super(d, n, v);
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
