package core.structure.unicardinal.alg.symbolic.operator;

import core.Diagram;
import core.structure.unicardinal.alg.*;
import core.structure.unicardinal.alg.structure.*;
import core.structure.*;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;
import core.structure.unicardinal.alg.symbolic.constant.SymbolicReal;
import core.util.*;

import java.util.*;

public class SymbolicMul extends Reduction implements SymbolicExpression {
    /** SECTION: Factory Methods ==================================================================================== */
    public static SymbolicExpression create(Collection<SymbolicExpression> args) {
        return (SymbolicExpression) new SymbolicMul(args).close();
    }

    public static SymbolicExpression create(SymbolicExpression... args) {
        return SymbolicMul.create(List.of(args));
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected SymbolicMul(Collection<SymbolicExpression> args) {
        super(args);
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

    public Expression close() {
        TreeMap<Expression, Double> termMap = new TreeMap<>(Utils.UNICARDINAL_COMPARATOR);
        double[] coefficient = {1};
        this.closeHelper(termMap, coefficient, this.getInputs(Reduction.TERMS));
        ArrayList<SymbolicExpression> resultTerms = new ArrayList<>(termMap.size());
        termMap.forEach((expr, power) -> {
            if (power == 1) {
                resultTerms.add((SymbolicExpression) expr);
            } else if (power != 0) {
                resultTerms.add(SymbolicPow.create((SymbolicExpression) expr, power));
            }
        });
        Expression result = switch (resultTerms.size()) {
            case 0 -> SymbolicReal.create(coefficient[0]);
            case 1 -> SymbolicScale.create(coefficient[0], resultTerms.get(0));
            default -> SymbolicScale.create(coefficient[0], new SymbolicMul(resultTerms));
        };
        return Diagram.retrieve(result);
    }

    private void closeHelper(TreeMap<Expression, Double> map, double[] coefficient, Collection<? extends Expression> args) {
        for (Expression arg : args) {
            if (arg instanceof SymbolicMul m) {
                this.closeHelper(map, coefficient, m.getInputs(Reduction.TERMS));
            } else {
                this.degree += arg.getDegree();
                switch (arg) {
                    case Scale sc -> {
                        coefficient[0] *= sc.coefficient;
                        Utils.addToMultiset(map, sc.expression, 1);
                    }
                    case SymbolicPow p -> Utils.addToMultiset(map, p.expression, p.coefficient);
                    case Real re -> coefficient[0] *= re.value;
                    default -> Utils.addToMultiset(map, arg, 1);
                }
            }
        }
    }

    protected int identity() {
        return 1;
    }
}
