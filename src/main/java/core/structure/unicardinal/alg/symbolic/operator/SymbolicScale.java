package core.structure.unicardinal.alg.symbolic.operator;

import core.structure.equalitypivot.EqualityPivot;
import core.structure.unicardinal.Unicardinal;
import core.structure.unicardinal.alg.structure.Accumulation;
import core.structure.unicardinal.alg.structure.Scale;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;

public class SymbolicScale extends Scale implements SymbolicExpression {
    /** SECTION: Factory Methods ==================================================================================== */
    public static EqualityPivot<SymbolicExpression> create(double c, EqualityPivot<SymbolicExpression> expr) {
        return (EqualityPivot<SymbolicExpression>) new SymbolicScale(c, expr).close();
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected SymbolicScale(double coefficient, EqualityPivot<SymbolicExpression> expr) {
        super(coefficient, expr);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Expression ====================================================================================== */
    public int getDegree() {
        return this.expression.simplestElement.getDegree();
    }

    /** SUBSECTION: Accumulation ==================================================================================== */
    protected Accumulation createRawAccumulation(double coefficient, EqualityPivot<? extends Unicardinal> expression) {
        return new SymbolicScale(coefficient, (EqualityPivot<SymbolicExpression>) expression);
    }
}
