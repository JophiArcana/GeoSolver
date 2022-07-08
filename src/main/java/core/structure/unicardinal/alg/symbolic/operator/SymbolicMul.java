package core.structure.unicardinal.alg.symbolic.operator;

import core.structure.unicardinal.alg.*;
import core.structure.unicardinal.alg.structure.*;
import core.structure.*;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;
import core.structure.unicardinal.alg.symbolic.constant.SymbolicReal;
import core.util.*;

import java.util.*;

public class SymbolicMul extends Reduction implements SymbolicExpression {
    /** SECTION: Factory Methods ==================================================================================== */
    public static SymbolicExpression create(Iterable<SymbolicExpression> args) {
        SymbolicExpression result = (SymbolicExpression) new SymbolicMul(args).close();
        if (Reduction.CONSTANT == 1) {
            return result;
        } else {
            return SymbolicScale.create(Reduction.CONSTANT, result);
        }
    }

    public static SymbolicExpression create(SymbolicExpression... args) {
        return SymbolicMul.create(List.of(args));
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected SymbolicMul(Iterable<SymbolicExpression> args) {
        super(args);
    }

    protected void construct(Iterable<? extends Expression> args) {
        for (Expression arg : args) {
            if (arg instanceof SymbolicMul m) {
                this.construct(m.getInputs(Reduction.TERMS));
            } else {
                this.degree += arg.getDegree();
                switch (arg) {
                    case Scale sc -> {
                        Reduction.CONSTANT *= sc.coefficient;
                        Reduction.TERM_MAP.put(sc.expression, Reduction.TERM_MAP.getOrDefault(sc.expression, 0.0) + 1);
                    }
                    case SymbolicPow p ->
                            Reduction.TERM_MAP.put(p.expression, Reduction.TERM_MAP.getOrDefault(p.expression, 0.0) + p.coefficient);
                    case Real re -> Reduction.CONSTANT *= re.value;
                    default -> Reduction.TERM_MAP.put(arg, Reduction.TERM_MAP.getOrDefault(arg, 0.0) + 1);
                }
            }
        }
    }

    /** SECTION: Print Format ======================================================================================= */
    public String toString() {
        ArrayList<Entity> inputTerms = new ArrayList<>(this.inputs.get(Reduction.TERMS));
        ArrayList<String> stringTerms = new ArrayList<>();
        for (Entity ent : inputTerms) {
            if (Utils.CLOSED_FORM.contains(ent.getClass())) {
                stringTerms.add(ent.toString());
            } else {
                stringTerms.add("(" + ent + ")");
            }
        }
        return String.join("", stringTerms);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Unicardinal ===================================================================================== */
    public void computeValue() {
        double result = 1;
        for (Expression expr : this.getInputs(Reduction.TERMS)) {
            result *= expr.doubleValue();
        }
        this.value.set(result);
    }

    /** SUBSECTION: Expression ====================================================================================== */
    public Expression expand() {
        if (this.expansion == null) {
            List<SymbolicExpression> expansions = Utils.map(this.getInputs(Reduction.TERMS), arg -> (SymbolicExpression) arg.expand());
            ArrayList<SymbolicExpression> singletons = new ArrayList<>();
            ArrayList<SymbolicExpression> expandedTerms = new ArrayList<>(List.of(SymbolicReal.create(1)));
            for (SymbolicExpression expr : expansions) {
                if (expr instanceof Add addExpr) {
                    ArrayList<SymbolicExpression> newExpandedTerms = new ArrayList<>();
                    for (Expression term : addExpr.getInputs(Reduction.TERMS)) {
                        expandedTerms.forEach(arg -> newExpandedTerms.add(SymbolicMul.create(List.of(arg, (SymbolicExpression) term))));
                    }
                    expandedTerms = newExpandedTerms;
                } else {
                    singletons.add(expr);
                }
            }
            SymbolicExpression singleton = SymbolicMul.create(singletons);
            this.expansion = SymbolicAdd.create(Utils.map(expandedTerms, arg -> SymbolicMul.create(List.of(arg, singleton))));
        }
        return this.expansion;
    }

    /** SUBSECTION: Reduction ======================================================================================= */
    protected int identity() {
        return 1;
    }

    public Expression createAccumulation(double coefficient, Expression expr) {
        return SymbolicPow.create((SymbolicExpression) expr, coefficient);
    }
}
