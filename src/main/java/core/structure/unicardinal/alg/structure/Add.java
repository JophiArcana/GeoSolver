package core.structure.unicardinal.alg.structure;

import core.structure.unicardinal.alg.Expression;
import core.util.*;

public abstract class Add extends Reduction {
    /** SECTION: Protected Constructors ============================================================================= */
    protected Add(Iterable<? extends Expression> args) {
        super(args);
    }

    protected void construct(Iterable<? extends Expression> args) {
        for (Expression arg : args) {
            if (!arg.equalsZero()) {
                this.degree = Math.max(this.degree, arg.getDegree());
                switch (arg) {
                    case Add addExpr -> this.construct(Utils.cast(addExpr.inputs.get(Reduction.TERMS)));
                    case Scale sc ->
                            Reduction.TERM_MAP.put(sc.expression, Reduction.TERM_MAP.getOrDefault(sc.expression, 0.0) + sc.coefficient);
                    case Real re -> {
                        Real ONE = this.createReal(1);
                        Reduction.TERM_MAP.put(ONE, Reduction.TERM_MAP.getOrDefault(ONE, 0.0) + re.value);
                    }
                    default -> Reduction.TERM_MAP.put(arg, Reduction.TERM_MAP.getOrDefault(arg, 0.0) + 1);
                }
            }
        }
    }

    /** SECTION: Print Format ======================================================================================= */
    public String toString() {
        return String.join(" + ", Utils.map(this.inputs.get(Reduction.TERMS), Object::toString));
    }

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

    /** SUBSECTION: Reduction ======================================================================================= */
    protected int identity() {
        return 0;
    }
}
