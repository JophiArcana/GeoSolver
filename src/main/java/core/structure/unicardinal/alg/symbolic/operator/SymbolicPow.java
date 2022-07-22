package core.structure.unicardinal.alg.symbolic.operator;

import core.structure.equalitypivot.EqualityPivot;
import core.structure.equalitypivot.LockedEqualityPivot;
import core.structure.unicardinal.Constant;
import core.structure.unicardinal.Unicardinal;
import core.structure.unicardinal.alg.structure.Accumulation;
import core.structure.unicardinal.alg.structure.Add;
import core.structure.unicardinal.alg.structure.Reduction;
import core.structure.unicardinal.alg.symbolic.SymbolicConstant;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;
import core.structure.unicardinal.alg.symbolic.constant.*;
import core.util.*;

import java.util.*;

public class SymbolicPow extends Accumulation implements SymbolicExpression {
    /** SECTION: Factory Methods ==================================================================================== */
    public static EqualityPivot<SymbolicExpression> create(EqualityPivot<SymbolicExpression> base, double exponent) {
        return (EqualityPivot<SymbolicExpression>) new SymbolicPow(base, exponent).close();
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected SymbolicPow(EqualityPivot<SymbolicExpression> base, double exponent) {
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
    public EqualityPivot<SymbolicExpression> expand() {
        if (this.expansion == null) {
            this.expansion = switch (this.expression.simplestElement) {
                case SymbolicMul mulExpr -> SymbolicMul.create(Utils.map(mulExpr.getInputs(Reduction.TERMS),
                            arg -> SymbolicPow.create((EqualityPivot<SymbolicExpression>) arg, this.coefficient))).simplestElement.expand();
                case SymbolicScale scaleExpr -> {
                    double newCoefficient = Math.pow(scaleExpr.coefficient, this.coefficient);
                    EqualityPivot<SymbolicExpression> newExpression = (EqualityPivot<SymbolicExpression>) SymbolicPow.create(
                            (EqualityPivot<SymbolicExpression>) scaleExpr.expression, this.coefficient).simplestElement.expand();
                    yield SymbolicScale.create(newCoefficient, newExpression);
                }
                case Add addExpr && this.coefficient >= 1 && this.coefficient % 1 == 0 ->
                    SymbolicAdd.create(this.expandHelper(List.copyOf(Utils.cast(addExpr.getInputs(Reduction.TERMS))), (int) this.coefficient));
                default -> (EqualityPivot<? extends Unicardinal>) this.equalityPivot;
            };
        }
        return (EqualityPivot<SymbolicExpression>) this.expansion;
    }

    private List<EqualityPivot<SymbolicExpression>> expandHelper(List<EqualityPivot<SymbolicExpression>> terms, int n) {
        if (n == 1) {
            return terms;
        } else {
            List<EqualityPivot<SymbolicExpression>> sqrt = expandHelper(terms, n / 2);
            ArrayList<EqualityPivot<SymbolicExpression>> result = new ArrayList<>();
            for (int i = 0; i < sqrt.size(); i++) {
                for (int j = 0; j < i; j++) {
                    result.add(SymbolicScale.create(2, SymbolicMul.create(sqrt.get(i), sqrt.get(j))));
                }
                result.add(SymbolicPow.create(sqrt.get(i), 2));
            }
            if (n % 2 == 0) {
                return result;
            } else {
                ArrayList<EqualityPivot<SymbolicExpression>> newResult = new ArrayList<>();
                for (EqualityPivot<SymbolicExpression> term : terms) {
                    result.forEach(arg -> newResult.add(SymbolicMul.create(term, arg)));
                }
                return newResult;
            }
        }
    }

    public int getDegree() {
        return (int) (this.coefficient * this.expression.simplestElement.getDegree());
    }

    /** SUBSECTION: Accumulation ==================================================================================== */
    protected int identity() {
        return 1;
    }

    protected LockedEqualityPivot<SymbolicExpression, ? extends SymbolicConstant> evaluateConstant(double coefficient, Constant expression) {
        return switch (expression) {
            case SymbolicInfinity inf -> SymbolicInfinity.create(Math.pow(inf.coefficient, coefficient), inf.degree * coefficient);
            case SymbolicReal re -> SymbolicReal.create(Math.pow(re.value, coefficient));
            default -> null;
        };
    }

    protected Accumulation createRawAccumulation(double coefficient, EqualityPivot<? extends Unicardinal> expression) {
        return new SymbolicPow((EqualityPivot<SymbolicExpression>) expression, coefficient);
    }
}

