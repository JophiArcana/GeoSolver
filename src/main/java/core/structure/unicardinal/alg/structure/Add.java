package core.structure.unicardinal.alg.structure;

import core.Diagram;
import core.structure.unicardinal.alg.Expression;
import core.util.*;

import java.util.*;

public abstract class Add extends Reduction {
    /** SECTION: Protected Constructors ============================================================================= */
    protected Add(Collection<? extends Expression> args) {
        super(args);
    }

    /** SECTION: Print Format ======================================================================================= */
    public String toString() {
        return String.join(" + ", Utils.map(this.inputs.get(Reduction.TERMS), Object::toString));
    }

    /** SECTION: Interface ========================================================================================== */
    protected abstract Add createRawAdd(Collection<? extends Expression> args);

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Unicardinal ===================================================================================== */
    public void computeValue() {
        double result = 0;
        for (Expression expr : this.getInputs(Reduction.TERMS)) {
            result += expr.doubleValue();
        }
        this.value.set(result);
    }

    /** SUBSECTION: Expression ====================================================================================== */
    public Expression expand() {
        if (this.expansion == null) {
            this.expansion = this.createAdd(Utils.map(this.getInputs(Reduction.TERMS), Expression::expand));
        }
        return this.expansion;
    }

    public Expression close() {
        TreeMap<Expression, Double> termMap = new TreeMap<>(Utils.UNICARDINAL_COMPARATOR);
        this.closeHelper(termMap, this.getInputs(Reduction.TERMS));
        ArrayList<Expression> resultTerms = new ArrayList<>(termMap.size());
        termMap.forEach((expr, coefficient) -> {
            if (coefficient == 1) {
                resultTerms.add(expr);
            } else if (coefficient != 0) {
                resultTerms.add(this.createScale(coefficient, expr));
            }
        });
        Expression result = switch (resultTerms.size()) {
            case 0 -> this.createReal(0);
            case 1 -> resultTerms.get(0);
            default -> this.createRawAdd(resultTerms);
        };
        return Diagram.retrieve(result);
    }

     private void closeHelper(TreeMap<Expression, Double> map, Collection<? extends Expression> args) {
        for (Expression arg : args) {
            this.degree = Math.max(this.degree, arg.getDegree());
            switch (arg) {
                case Add addExpr -> this.closeHelper(map, addExpr.getInputs(Reduction.TERMS));
                case Scale sc -> Utils.addToMultiset(map, sc.expression, sc.coefficient);
                case Real re -> Utils.addToMultiset(map, this.createReal(1), re.value);
                default -> Utils.addToMultiset(map, arg, 1);
            }
        }
    }

    protected int identity() {
        return 0;
    }
}
