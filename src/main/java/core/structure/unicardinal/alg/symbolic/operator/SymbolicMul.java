package core.structure.unicardinal.alg.symbolic.operator;

import core.Diagram;
import core.structure.equalitypivot.EqualityPivot;
import core.structure.unicardinal.Unicardinal;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;
import core.structure.unicardinal.alg.symbolic.constant.SymbolicReal;
import core.structure.unicardinal.alg.structure.Add;
import core.structure.unicardinal.alg.structure.Real;
import core.structure.unicardinal.alg.structure.Reduction;
import core.structure.unicardinal.alg.structure.Scale;
import core.util.*;

import java.util.*;

public class SymbolicMul extends Reduction implements SymbolicExpression {
    /** SECTION: Factory Methods ==================================================================================== */
    public static EqualityPivot<SymbolicExpression> create(Collection<EqualityPivot<SymbolicExpression>> args) {
        return (EqualityPivot<SymbolicExpression>) new SymbolicMul(args).close();
    }

    @SafeVarargs
    public static EqualityPivot<SymbolicExpression> create(EqualityPivot<SymbolicExpression>... args) {
        return (EqualityPivot<SymbolicExpression>) new SymbolicMul(List.of(args)).close();
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected SymbolicMul(Collection<EqualityPivot<SymbolicExpression>> args) {
        super(args);
    }

    /** SECTION: Print Format ======================================================================================= */
    public String toString() {
        ArrayList<String> stringTerms = new ArrayList<>();
        for (EqualityPivot<?> ent : this.getInputs(Reduction.TERMS)) {
            if (Utils.CLOSED_FORM.contains(ent.simplestElement.getClass())) {
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
        for (EqualityPivot<? extends Unicardinal> expr : this.getInputs(Reduction.TERMS)) {
            result *= expr.doubleValue();
        }
        this.value.set(result);
    }

    /** SUBSECTION: Expression ====================================================================================== */
    public EqualityPivot<? extends Unicardinal> expand() {
        if (this.expansion == null) {
            List<EqualityPivot<SymbolicExpression>> expansions = Utils.map(this.getInputs(Reduction.TERMS), arg -> (EqualityPivot<SymbolicExpression>) arg.simplestElement.expand());
            ArrayList<EqualityPivot<SymbolicExpression>> singletons = new ArrayList<>();
            ArrayList<EqualityPivot<SymbolicExpression>> expandedTerms = new ArrayList<>(List.of(SymbolicReal.ONE));
            for (EqualityPivot<SymbolicExpression> expr : expansions) {
                if (expr.simplestElement instanceof Add addExpr) {
                    ArrayList<EqualityPivot<SymbolicExpression>> newExpandedTerms = new ArrayList<>();
                    for (EqualityPivot<? extends Unicardinal> term : addExpr.getInputs(Reduction.TERMS)) {
                        expandedTerms.forEach(arg -> newExpandedTerms.add(SymbolicMul.create(arg, (EqualityPivot<SymbolicExpression>) term)));
                    }
                    expandedTerms = newExpandedTerms;
                } else {
                    singletons.add(expr);
                }
            }
            EqualityPivot<SymbolicExpression> singleton = SymbolicMul.create(singletons);
            this.expansion = SymbolicAdd.create(Utils.map(expandedTerms, arg -> SymbolicMul.create(arg, singleton)));
        }
        return this.expansion;
    }

    public EqualityPivot<? extends Unicardinal> close() {
        TreeMap<EqualityPivot<SymbolicExpression>, Double> termMap = new TreeMap<>();
        double[] coefficient = {1};
        this.closeHelper(termMap, coefficient, Utils.cast(this.getInputs(Reduction.TERMS)));
        ArrayList<EqualityPivot<SymbolicExpression>> resultTerms = new ArrayList<>(termMap.size());
        termMap.forEach((expr, power) -> {
            if (power == 1) {
                resultTerms.add(expr);
            } else if (power != 0) {
                resultTerms.add(SymbolicPow.create(expr, power));
            }
        });
        EqualityPivot<? extends Unicardinal> result = switch (resultTerms.size()) {
            case 0 -> SymbolicReal.create(coefficient[0]);
            case 1 -> SymbolicScale.create(coefficient[0], resultTerms.get(0));
            default -> SymbolicScale.create(coefficient[0], Diagram.retrieve(new SymbolicMul(resultTerms)));
        };
        return (EqualityPivot<? extends Unicardinal>) this.mergeResult(result);
    }

    private void closeHelper(TreeMap<EqualityPivot<SymbolicExpression>, Double> map, double[] coefficient, Collection<EqualityPivot<SymbolicExpression>> args) {
        for (EqualityPivot<SymbolicExpression> arg : args) {
            if (arg.simplestElement instanceof SymbolicMul m) {
                this.closeHelper(map, coefficient, Utils.cast(m.getInputs(Reduction.TERMS)));
            } else {
                this.degree += arg.simplestElement.getDegree();
                switch (arg.simplestElement) {
                    case Scale sc -> {
                        coefficient[0] *= sc.coefficient;
                        Utils.addToMultiset(map, (EqualityPivot<SymbolicExpression>) sc.expression, 1);
                    }
                    case SymbolicPow p -> Utils.addToMultiset(map, (EqualityPivot<SymbolicExpression>) p.expression, p.coefficient);
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
