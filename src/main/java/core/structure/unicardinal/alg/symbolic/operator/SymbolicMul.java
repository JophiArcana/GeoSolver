package core.structure.unicardinal.alg.symbolic.operator;

import core.structure.unicardinal.alg.*;
import core.structure.unicardinal.alg.structure.*;
import core.structure.*;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;
import core.structure.unicardinal.alg.symbolic.constant.SymbolicReal;
import core.util.*;

import java.util.*;

public class SymbolicMul extends Reduction implements SymbolicExpression {
    /** SECTION: Static Data ======================================================================================== */
    private static double constant;

    /** SECTION: Factory Methods ==================================================================================== */
    public static SymbolicExpression create(Iterable<SymbolicExpression> args) {
        SymbolicExpression result = (SymbolicExpression) new SymbolicMul(args).close();
        if (SymbolicMul.constant == 1) {
            return result;
        } else {
            return SymbolicScale.create(SymbolicMul.constant, result);
        }
    }

    public static SymbolicExpression create(SymbolicExpression... args) {
        SymbolicExpression result = (SymbolicExpression) new SymbolicMul(args).close();
        if (SymbolicMul.constant == 1) {
            return result;
        } else {
            return SymbolicScale.create(SymbolicMul.constant, result);
        }
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected SymbolicMul(Iterable<SymbolicExpression> args) {
        super(args);
    }

    protected SymbolicMul(SymbolicExpression... args) {
        super(args);
    }

    protected void construct(Iterable<? extends Expression> args) {
        if (this.terms.size() == 0) {
            SymbolicMul.constant = 1;
        }
        args.forEach(this::constructArg);
    }

    protected void construct(Expression... args) {
        if (this.terms.size() == 0) {
            SymbolicMul.constant = 1;
        }
        for (Expression arg : args) {
            this.constructArg(arg);
        }
    }

    public void constructArg(Expression arg) {
        if (arg instanceof SymbolicMul m) {
            this.construct(Utils.cast(m.inputs.get(Reduction.TERMS)));
        } else {
            this.degree += arg.getDegree();
            if (arg instanceof Scale sc) {
                SymbolicMul.constant *= sc.coefficient;
                this.terms.put(sc.expression, this.terms.getOrDefault(sc.expression, 0.0) + 1);
            } else if (arg instanceof SymbolicPow p) {
                this.terms.put(p.expression, this.terms.getOrDefault(p.expression, 0.0) + p.coefficient);
            } else if (arg instanceof Real re) {
                SymbolicMul.constant *= re.value;
            } else {
                this.terms.put(arg, this.terms.getOrDefault(arg, 0.0) + 1);
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
        this.value = 1;
        this.terms.forEach((expression, coefficient) -> this.value *= Math.pow(expression.value(), coefficient));
    }

    /** SUBSECTION: Expression ====================================================================================== */
    public Expression expand() {
        if (this.expansion == null) {
            List<SymbolicExpression> expansions = Utils.map(this.getInputs(Reduction.TERMS), arg -> (SymbolicExpression) arg.expand());
            ArrayList<SymbolicExpression> singletons = new ArrayList<>();
            ArrayList<SymbolicExpression> expandedTerms = new ArrayList<>(List.of((SymbolicExpression) Constant.ONE(this.diagram, SymbolicExpression.class)));
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
    protected Real identity() {
        return SymbolicReal.create(this.diagram, 1);
    }

    public Expression createAccumulation(double coefficient, Expression expr) {
        return SymbolicPow.create((SymbolicExpression) expr, coefficient);
    }
}
