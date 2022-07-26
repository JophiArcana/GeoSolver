package core.structure.unicardinal.alg.symbolic.operator;

import core.Diagram;
import core.Propositions.equalitypivot.unicardinal.UnicardinalPivot;
import core.structure.unicardinal.*;
import core.structure.unicardinal.alg.symbolic.*;
import core.structure.unicardinal.alg.structure.*;
import core.structure.unicardinal.alg.symbolic.SymbolicVariable;
import core.util.*;

import java.util.*;

public class SymbolicMul extends Reduction implements SymbolicExpression {
    /** SECTION: Factory Methods ==================================================================================== */
    public static UnicardinalPivot<SymbolicExpression> create(Collection<UnicardinalPivot<SymbolicExpression>> args) {
        return new SymbolicMul(args).close();
    }

    @SafeVarargs
    public static UnicardinalPivot<SymbolicExpression> create(UnicardinalPivot<SymbolicExpression>... args) {
        return new SymbolicMul(List.of(args)).close();
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected SymbolicMul(Collection<UnicardinalPivot<SymbolicExpression>> args) {
        super(args);
    }

    /** SECTION: Print Format ======================================================================================= */
    public String toString() {
        ArrayList<String> stringTerms = new ArrayList<>();
        for (UnicardinalPivot<?> ent : this.getInputs(Reduction.TERMS)) {
            if (ent.element() instanceof SymbolicVariable) {
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
        for (UnicardinalPivot<?> expr : this.getInputs(Reduction.TERMS)) {
            result *= expr.doubleValue();
        }
        this.value.set(result);
    }

    /** SUBSECTION: Expression ====================================================================================== */
    public UnicardinalPivot<SymbolicExpression> expand() {
        if (this.expansion == null) {
            List<UnicardinalPivot<SymbolicExpression>> expansions = Utils.map(this.getInputs(Reduction.TERMS), arg -> (UnicardinalPivot<SymbolicExpression>) arg.element().expand());
            ArrayList<UnicardinalPivot<SymbolicExpression>> singletons = new ArrayList<>();
            ArrayList<UnicardinalPivot<SymbolicExpression>> expandedTerms = new ArrayList<>(List.of(SymbolicConstant.ONE));
            for (UnicardinalPivot<SymbolicExpression> expr : expansions) {
                if (expr.element() instanceof Add addExpr) {
                    ArrayList<UnicardinalPivot<SymbolicExpression>> newExpandedTerms = new ArrayList<>();
                    for (UnicardinalPivot<? extends Unicardinal> term : addExpr.getInputs(Reduction.TERMS)) {
                        expandedTerms.forEach(arg -> newExpandedTerms.add(SymbolicMul.create(arg, (UnicardinalPivot<SymbolicExpression>) term)));
                    }
                    expandedTerms = newExpandedTerms;
                } else {
                    singletons.add(expr);
                }
            }
            UnicardinalPivot<SymbolicExpression> singleton = SymbolicMul.create(singletons);
            this.expansion = SymbolicAdd.create(Utils.map(expandedTerms, arg -> SymbolicMul.create(arg, singleton)));
        }
        return (UnicardinalPivot<SymbolicExpression>) this.expansion;
    }

    public UnicardinalPivot<SymbolicExpression> close() {
        TreeMap<UnicardinalPivot<SymbolicExpression>, Double> termMap = new TreeMap<>();
        double[] coefficient = {1};
        this.closeHelper(termMap, coefficient, Utils.cast(this.getInputs(Reduction.TERMS)));
        ArrayList<UnicardinalPivot<SymbolicExpression>> resultTerms = new ArrayList<>(termMap.size());
        termMap.forEach((expr, power) -> {
            if (power == 1) {
                resultTerms.add(expr);
            } else if (power != 0) {
                resultTerms.add(SymbolicPow.create(expr, power));
            }
        });
        UnicardinalPivot<? extends Unicardinal> result = switch (resultTerms.size()) {
            case 0 -> SymbolicConstant.create(coefficient[0]);
            case 1 -> SymbolicScale.create(coefficient[0], resultTerms.get(0));
            default -> SymbolicScale.create(coefficient[0], Diagram.retrieve(new SymbolicMul(resultTerms)));
        };
        return (UnicardinalPivot<SymbolicExpression>) result.merge((UnicardinalPivot<?>) this.equalityPivot);
    }

    private void closeHelper(TreeMap<UnicardinalPivot<SymbolicExpression>, Double> map, double[] coefficient, Collection<UnicardinalPivot<SymbolicExpression>> args) {
        for (UnicardinalPivot<SymbolicExpression> arg : args) {
            if (arg.element() instanceof SymbolicMul m) {
                this.closeHelper(map, coefficient, Utils.cast(m.getInputs(Reduction.TERMS)));
            } else {
                this.degree += arg.element().getDegree();
                switch (arg.element()) {
                    case Scale sc -> {
                        coefficient[0] *= sc.coefficient;
                        Utils.addToMultiset(map, (UnicardinalPivot<SymbolicExpression>) sc.expression, 1);
                    }
                    case SymbolicPow p -> Utils.addToMultiset(map, (UnicardinalPivot<SymbolicExpression>) p.expression, p.coefficient);
                    case Constant c -> coefficient[0] *= c.value;
                    default -> Utils.addToMultiset(map, arg, 1);
                }
            }
        }
    }

    protected int identity() {
        return 1;
    }
}
