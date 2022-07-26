package core.structure.unicardinal.alg.symbolic.operator;

import core.Propositions.equalitypivot.unicardinal.LockedUnicardinalPivot;
import core.Propositions.equalitypivot.unicardinal.UnicardinalPivot;
import core.structure.unicardinal.Constant;
import core.structure.unicardinal.Unicardinal;
import core.structure.unicardinal.alg.structure.*;
import core.structure.unicardinal.alg.symbolic.*;
import core.util.*;

import java.util.*;

public class SymbolicPow extends Accumulation implements SymbolicExpression {
    /** SECTION: Factory Methods ==================================================================================== */
    public static UnicardinalPivot<SymbolicExpression> create(UnicardinalPivot<SymbolicExpression> base, double exponent) {
        return (UnicardinalPivot<SymbolicExpression>) new SymbolicPow(base, exponent).close();
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected SymbolicPow(UnicardinalPivot<SymbolicExpression> base, double exponent) {
        super(exponent, base);
    }

    /** SECTION: Print Format ======================================================================================= */
    public String toString() {
        String baseString;
        if (this.expression.element() instanceof SymbolicVariable) {
            baseString = this.expression + " ** ";
        } else {
            baseString = "(" + this.expression + ") ** ";
        }
        if (this.coefficient % 1 == 0) {
            return baseString + (int) this.coefficient;
        } else {
            return baseString + this.coefficient;
        }
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Unicardinal ===================================================================================== */
    public void computeValue() {
        double base = this.expression.doubleValue();
        if (this.coefficient == -1) {
            this.value.set(1 / base);
        } else if (this.coefficient == 2) {
            this.value.set(base * base);
        } else {
            this.value.set(Math.pow(base, this.coefficient));
        }
    }

    /** SUBSECTION: Expression ====================================================================================== */
    public UnicardinalPivot<SymbolicExpression> expand() {
        if (this.expansion == null) {
            this.expansion = switch (this.expression.element()) {
                case SymbolicMul mulExpr -> SymbolicMul.create(Utils.map(mulExpr.getInputs(Reduction.TERMS),
                            arg -> SymbolicPow.create((UnicardinalPivot<SymbolicExpression>) arg, this.coefficient))).element().expand();
                case SymbolicScale scaleExpr -> {
                    double newCoefficient = Math.pow(scaleExpr.coefficient, this.coefficient);
                    UnicardinalPivot<SymbolicExpression> newExpression = (UnicardinalPivot<SymbolicExpression>) SymbolicPow.create(
                            (UnicardinalPivot<SymbolicExpression>) scaleExpr.expression, this.coefficient).element().expand();
                    yield SymbolicScale.create(newCoefficient, newExpression);
                }
                case Add addExpr && this.coefficient >= 1 && this.coefficient % 1 == 0 ->
                    SymbolicAdd.create(this.expandHelper(List.copyOf(Utils.cast(addExpr.getInputs(Reduction.TERMS))), (int) this.coefficient));
                default -> (UnicardinalPivot<? extends Unicardinal>) this.equalityPivot;
            };
        }
        return (UnicardinalPivot<SymbolicExpression>) this.expansion;
    }

    private List<UnicardinalPivot<SymbolicExpression>> expandHelper(List<UnicardinalPivot<SymbolicExpression>> terms, int n) {
        if (n == 1) {
            return terms;
        } else {
            List<UnicardinalPivot<SymbolicExpression>> sqrt = expandHelper(terms, n / 2);
            ArrayList<UnicardinalPivot<SymbolicExpression>> result = new ArrayList<>();
            for (int i = 0; i < sqrt.size(); i++) {
                for (int j = 0; j < i; j++) {
                    result.add(SymbolicScale.create(2, SymbolicMul.create(sqrt.get(i), sqrt.get(j))));
                }
                result.add(SymbolicPow.create(sqrt.get(i), 2));
            }
            if (n % 2 == 0) {
                return result;
            } else {
                ArrayList<UnicardinalPivot<SymbolicExpression>> newResult = new ArrayList<>();
                for (UnicardinalPivot<SymbolicExpression> term : terms) {
                    result.forEach(arg -> newResult.add(SymbolicMul.create(term, arg)));
                }
                return newResult;
            }
        }
    }

    public int getDegree() {
        return (int) (this.coefficient * this.expression.element().getDegree());
    }

    /** SUBSECTION: Accumulation ==================================================================================== */
    protected int identity() {
        return 1;
    }

    protected LockedUnicardinalPivot<SymbolicExpression, SymbolicConstant> evaluateConstant(double coefficient, Constant expression) {
        return SymbolicConstant.create(Math.pow(expression.value, coefficient));
    }

    protected Accumulation createRawAccumulation(double coefficient, UnicardinalPivot<?> expression) {
        return new SymbolicPow((UnicardinalPivot<SymbolicExpression>) expression, coefficient);
    }
}

