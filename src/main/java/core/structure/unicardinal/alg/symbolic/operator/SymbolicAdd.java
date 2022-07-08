package core.structure.unicardinal.alg.symbolic.operator;

import core.structure.unicardinal.alg.structure.Add;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;
import core.structure.unicardinal.alg.Expression;

import java.util.List;

public class SymbolicAdd extends Add implements SymbolicExpression {
    /** SECTION: Factory Methods ==================================================================================== */
    public static SymbolicExpression create(Iterable<SymbolicExpression> args) {
        return (SymbolicExpression) new SymbolicAdd(args).close();
    }

    public static SymbolicExpression create(SymbolicExpression... args) {
        return (SymbolicExpression) new SymbolicAdd(List.of(args)).close();
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected SymbolicAdd(Iterable<SymbolicExpression> args) {
        super(args);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Reduction ======================================================================================= */
    public Expression createAccumulation(double coefficient, Expression expr) {
        return SymbolicScale.create(coefficient, (SymbolicExpression) expr);
    }
}
