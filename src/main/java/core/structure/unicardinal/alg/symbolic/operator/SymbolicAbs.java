package core.structure.unicardinal.alg.symbolic.operator;

import core.structure.unicardinal.alg.*;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;
import core.util.Utils;

import java.util.*;

public class SymbolicAbs extends DefinedExpression implements SymbolicExpression {
    /** SECTION: Static Data ======================================================================================== */
    public static final InputType<SymbolicExpression> EXPRESSION = new InputType<>(SymbolicExpression.class, Utils.UNICARDINAL_COMPARATOR);

    public static final List<InputType<?>> inputTypes = List.of(SymbolicAbs.EXPRESSION);

    /** SECTION: Instance Variables ================================================================================= */
    public SymbolicExpression expression;

    /** SECTION: Factory Methods ==================================================================================== */
    public static SymbolicExpression create(SymbolicExpression expression) {
        return (SymbolicExpression) new SymbolicAbs(expression).close();
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected SymbolicAbs(SymbolicExpression expression) {
        super();
        this.expression = expression;
        expression.reverseSymbolicDependencies().add(this);
        this.computeValue();
    }

    /** SECTION: Print Format ======================================================================================= */
    public String toString() {
        return "|" + this.expression + "|";
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public List<InputType<?>> getInputTypes() {
        return SymbolicAbs.inputTypes;
    }

    /** SUBSECTION: Unicardinal ===================================================================================== */
    public void computeValue() {
        this.value.set(Math.abs(this.expression.doubleValue()));
    }

    /** SUBSECTION: Expression ====================================================================================== */
    public Expression expand() {
        return SymbolicAbs.create((SymbolicExpression) this.expression.expand());
    }

    public Expression close() {
        if (this.expression instanceof SymbolicPow powExpr && powExpr.coefficient % 2 == 0) {
            return powExpr.expression;
        } else if (this.expression instanceof SymbolicScale scaleExpr) {
            return SymbolicScale.create(Math.abs(scaleExpr.coefficient), SymbolicAbs.create((SymbolicExpression) scaleExpr.expression));
        } else {
            return this;
        }
    }

    public int getDegree() {
        return this.expression.getDegree();
    }
}
