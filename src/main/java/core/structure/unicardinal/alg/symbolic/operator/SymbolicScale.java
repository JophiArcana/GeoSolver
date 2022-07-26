package core.structure.unicardinal.alg.symbolic.operator;

import core.Propositions.equalitypivot.unicardinal.UnicardinalPivot;
import core.structure.unicardinal.alg.structure.Accumulation;
import core.structure.unicardinal.alg.structure.Scale;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;

public class SymbolicScale extends Scale implements SymbolicExpression {
    /** SECTION: Factory Methods ==================================================================================== */
    public static UnicardinalPivot<SymbolicExpression> create(double c, UnicardinalPivot<SymbolicExpression> expr) {
        return (UnicardinalPivot<SymbolicExpression>) new SymbolicScale(c, expr).close();
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected SymbolicScale(double coefficient, UnicardinalPivot<SymbolicExpression> expr) {
        super(coefficient, expr);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Expression ====================================================================================== */
    public int getDegree() {
        return this.expression.element().getDegree();
    }

    /** SUBSECTION: Accumulation ==================================================================================== */
    protected Accumulation createRawAccumulation(double coefficient, UnicardinalPivot<?> expression) {
        return new SymbolicScale(coefficient, (UnicardinalPivot<SymbolicExpression>) expression);
    }
}
