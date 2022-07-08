package core.structure.unicardinal.alg.structure;

import core.structure.unicardinal.alg.Constant;
import core.structure.unicardinal.alg.DefinedExpression;
import core.structure.unicardinal.alg.Expression;
import core.util.Utils;

import java.util.List;

public abstract class Accumulation extends DefinedExpression {
    /** SECTION: Static Data ======================================================================================== */
    public static final InputType<Real> COEFFICIENT = new InputType<>(Real.class, Constant::compare);
    public static final InputType<Expression> EXPRESSION = new InputType<>(Expression.class, Utils.UNICARDINAL_COMPARATOR);

    public static final List<InputType<?>> inputTypes = List.of(Accumulation.COEFFICIENT, Accumulation.EXPRESSION);

    /** SECTION: Instance Variables ================================================================================= */
    public final double coefficient;
    public final Expression expression;

    /** SECTION: Abstract Constructor =============================================================================== */
    protected Accumulation(double coefficient, Expression expr) {
        super();
        if (expr instanceof Constant constExpr) {
            this.coefficient = 1;
            this.expression = this.evaluateConstant(coefficient, constExpr);
        } else if (expr.getClass() == this.getClass()) {
            this.coefficient = coefficient * ((Accumulation) expr).coefficient;
            this.expression = ((Accumulation) expr).expression;
        } else {
            this.coefficient = coefficient;
            this.expression = expr;
        }
        this.getInputs(Accumulation.COEFFICIENT).add(this.createReal(this.coefficient));
        this.getInputs(Accumulation.EXPRESSION).add(this.expression);

        expr.reverseSymbolicDependencies().add(this);
        this.computeValue();
    }

    /** SECTION: Interface ========================================================================================== */
    protected abstract Real identity();
    protected abstract Constant evaluateConstant(double c, Constant e);

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public List<InputType<?>> getInputTypes() {
        return Accumulation.inputTypes;
    }

    /** SUBSECTION: Expression ====================================================================================== */
    public Expression close() {
        if (this.coefficient == 1) {
            return this.expression;
        } else if (this.coefficient == 0) {
            return this.identity();
        } else {
            return this;
        }
    }
}
