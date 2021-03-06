package core.structure.unicardinal.alg.symbolic.operator;

import core.structure.unicardinal.alg.Constant;
import core.structure.unicardinal.alg.Expression;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;
import core.structure.unicardinal.alg.structure.*;
import core.util.*;

import java.util.*;

public class SymbolicPow extends Accumulation implements SymbolicExpression {
    /** SECTION: Factory Methods ==================================================================================== */
    public static SymbolicExpression create(SymbolicExpression base, double exponent) {
        return (SymbolicExpression) new SymbolicPow(base, exponent).close();
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected SymbolicPow(SymbolicExpression base, double exponent) {
        super(exponent, base);
    }

    /** SECTION: Print Format ======================================================================================= */
    public String toString() {
        String baseString = this.expression.toString();
        if (!Utils.CLOSED_FORM.contains(this.expression.getClass())) {
            baseString = "(" + baseString + ")";
        }
        if (this.coefficient % 1 == 0) {
            return baseString + " ** " + (int) this.coefficient;
        } else {
            return baseString + " ** " + this.coefficient;
        }
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Unicardinal ===================================================================================== */
    public void computeValue() {
        if (this.coefficient == -1) {
            this.value.set(1.0 / this.expression.doubleValue());
        } else if (this.coefficient == 2) {
            this.value.set(this.expression.doubleValue() * this.expression.doubleValue());
        } else {
            this.value.set(Math.pow(this.expression.doubleValue(), this.coefficient));
        }
    }

    /** SUBSECTION: Expression ====================================================================================== */
    public Expression expand() {
        if (this.expansion == null) {
            this.expansion = switch (this.expression) {
                case SymbolicMul mulExpr ->
                        SymbolicMul.create(Utils.map(mulExpr.getInputs(Reduction.TERMS),
                                arg -> SymbolicPow.create((SymbolicExpression) arg, this.coefficient))).expand();
                case SymbolicScale scaleExpr ->
                        SymbolicScale.create(Math.pow(scaleExpr.coefficient, this.coefficient),
                                (SymbolicExpression) SymbolicPow.create((SymbolicExpression) scaleExpr.expression, this.coefficient).expand());
                case Add addExpr && this.coefficient >= 1 && this.coefficient % 1 == 0 ->
                    SymbolicAdd.create(this.expandHelper(List.copyOf(Utils.cast(addExpr.getInputs(Reduction.TERMS))), (int) this.coefficient));
                default -> this;
            };
        }
        return this.expansion;
    }

    private List<SymbolicExpression> expandHelper(List<SymbolicExpression> terms, int n) {
        if (n == 1) {
            return terms;
        } else {
            List<SymbolicExpression> sqrt = expandHelper(terms, n / 2);
            ArrayList<SymbolicExpression> result = new ArrayList<>();
            for (int i = 0; i < sqrt.size(); i++) {
                for (int j = 0; j < i; j++) {
                    result.add(SymbolicScale.create(2, SymbolicMul.create(sqrt.get(i), sqrt.get(j))));
                }
                result.add(SymbolicPow.create(sqrt.get(i), 2));
            }
            if (n % 2 == 0) {
                return result;
            } else {
                ArrayList<SymbolicExpression> newResult = new ArrayList<>();
                for (SymbolicExpression term : terms) {
                    result.forEach(arg -> newResult.add(SymbolicMul.create(term, arg)));
                }
                return newResult;
            }
        }
    }

    public int getDegree() {
        return (int) (this.coefficient * this.expression.getDegree());
    }

    /** SUBSECTION: Accumulation ==================================================================================== */
    protected int identity() {
        return 1;
    }

    protected Constant evaluateConstant(double coefficient, Constant expression) {
        return expression.pow(coefficient);
    }

    protected Accumulation createRawAccumulation(double coefficient, Expression expression) {
        return new SymbolicPow((SymbolicExpression) expression, coefficient);
    }
}

