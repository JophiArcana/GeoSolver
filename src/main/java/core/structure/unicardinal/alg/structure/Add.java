package core.structure.unicardinal.alg.structure;

import core.structure.unicardinal.alg.Expression;
import core.util.*;
import com.google.common.collect.TreeMultiset;

import java.util.Map;

public abstract class Add extends Reduction {
    /** SECTION: Protected Constructors ============================================================================= */
    protected Add(Iterable<? extends Expression> args) {
        super(args);
    }

    protected Add(Expression... args) {
        super(args);
    }

    protected void construct(Expression... args) {
        for (Expression arg : args) {
            this.constructArg(arg);
        }
    }

    protected void construct(Iterable<? extends Expression> args) {
        args.forEach(this::constructArg);
    }

    private void constructArg(Expression arg) {
        if (!arg.equalsZero()) {
            this.degree = Math.max(this.degree, arg.getDegree());
            if (arg instanceof Add addExpr) {
                this.construct(Utils.cast(addExpr.inputs.get(Reduction.TERMS)));
            } else if (arg instanceof Scale sc) {
                this.terms.put(sc.expression, this.terms.getOrDefault(sc.expression, 0.0) + sc.coefficient);
            } else if (arg instanceof Real re) {
                this.terms.put(this.createReal(1), this.terms.getOrDefault(this.createReal(1), 0.0) + re.value);
            } else {
                this.terms.put(arg, this.terms.getOrDefault(arg, 0.0) + 1);
            }
        }
    }

    /** SECTION: Print Format ======================================================================================= */
    public String toString() {
        return String.join(" + ", Utils.map(inputs.get(Reduction.TERMS), Object::toString));
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Unicardinal ===================================================================================== */
    public void computeValue() {
        double result = 0;
        for (Map.Entry<Expression, Double> entry : this.terms.entrySet()) {
            result += entry.getKey().doubleValue() * entry.getValue();
        }
        this.value.set(result);
    }

    /** SUBSECTION: Expression ====================================================================================== */
    public Expression expand() {
        if (this.expansion == null) {
            this.expansion = this.createAdd(Utils.map((TreeMultiset<Expression>) this.inputs.get(Reduction.TERMS), Expression::expand));
        }
        return this.expansion;
    }

    /** SUBSECTION: Reduction ======================================================================================= */
    protected Real identity() {
        return this.createReal(0);
    }
}
