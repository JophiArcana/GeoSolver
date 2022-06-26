package core.structure.unicardinal.alg.symbolic.operator;

import core.structure.unicardinal.alg.structure.Scale;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;

public class SymbolicScale extends Scale implements SymbolicExpression {
    /** SECTION: Factory Methods ==================================================================================== */
    public static SymbolicExpression create(double c, SymbolicExpression expr) {
        return (SymbolicExpression) new SymbolicScale(c, expr).close();
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected SymbolicScale(double coefficient, SymbolicExpression expr) {
        super(coefficient, expr);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Expression ====================================================================================== */
    public int getDegree() {
        return this.expression.getDegree();
    }
}
