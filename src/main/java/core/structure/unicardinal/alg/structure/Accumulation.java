package core.structure.unicardinal.alg.structure;

import core.Diagram;
import core.structure.unicardinal.alg.Constant;
import core.structure.unicardinal.alg.DefinedExpression;
import core.structure.unicardinal.alg.Expression;
import core.util.Utils;

import java.util.List;
import java.util.Optional;

public abstract class Accumulation extends DefinedExpression {
    /** SECTION: Static Data ======================================================================================== */
    public static final InputType<Real> COEFFICIENT = new InputType<>(Real.class, Constant::compare);
    public static final InputType<Expression> EXPRESSION = new InputType<>(Expression.class, Utils.UNICARDINAL_COMPARATOR);

    public static final List<InputType<?>> inputTypes = List.of(Accumulation.COEFFICIENT, Accumulation.EXPRESSION);

    /** SECTION: Instance Variables ================================================================================= */
    public double coefficient;
    public Expression expression;

    /** SECTION: Abstract Constructor =============================================================================== */
    protected Accumulation(double coefficient, Expression expr) {
        super();
        this.coefficient = coefficient;
        this.expression = expr;
        this.getInputs(Accumulation.COEFFICIENT).add(this.createReal(this.coefficient));
        this.getInputs(Accumulation.EXPRESSION).add(this.expression);

        expr.reverseComputationalDependencies().add(this);
    }

    /** SECTION: Interface ========================================================================================== */
    protected abstract int identity();
    protected abstract Constant evaluateConstant(double coefficient, Constant expression);

    protected abstract Accumulation createRawAccumulation(double coefficient, Expression expression);

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public List<InputType<?>> getInputTypes() {
        return Accumulation.inputTypes;
    }

    /** SUBSECTION: Expression ====================================================================================== */
    public Expression close() {
        Expression result;
        if (this.coefficient == 0) {
            result = this.createReal(this.identity());
        } else if (this.expression instanceof Constant constExpr) {
            result = this.evaluateConstant(this.coefficient, constExpr);
        } else if (this.getClass() == this.expression.getClass()) {
            Accumulation acc = (Accumulation) this.expression;
            result = this.createRawAccumulation(this.coefficient * acc.coefficient, acc.expression);
        } else {
            result = this;
        }
        return Diagram.retrieve(result);
    }
}
