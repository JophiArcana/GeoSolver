package core.structure.unicardinal.alg.structure;

import core.Diagram;
import core.Propositions.equalitypivot.unicardinal.UnicardinalPivot;
import core.structure.unicardinal.Constant;
import core.structure.unicardinal.Unicardinal;
import core.util.*;

import java.util.*;

public abstract class Add extends Reduction {
    /** SECTION: Protected Constructors ============================================================================= */
    protected Add(Collection<? extends UnicardinalPivot<?>> args) {
        super(args);
    }

    /** SECTION: Print Format ======================================================================================= */
    public String toString() {
        return String.join(" + ", Utils.map(this.getInputs(Reduction.TERMS), Object::toString));
    }

    /** SECTION: Interface ========================================================================================== */
    protected abstract Add createRawAdd(Collection<? extends UnicardinalPivot<?>> args);

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Unicardinal ===================================================================================== */
    public void computeValue() {
        double result = 0;
        for (UnicardinalPivot<?> expr : this.getInputs(Reduction.TERMS)) {
            result += expr.doubleValue();
        }
        this.value.set(result);
    }

    /** SUBSECTION: Expression ====================================================================================== */
    public UnicardinalPivot<?> expand() {
        if (this.expansion == null) {
            this.expansion = this.createAdd(Utils.map(Utils.cast(this.getInputs(Reduction.TERMS)), Unicardinal::expand));
        }
        return this.expansion;
    }

    public UnicardinalPivot<?> close() {
        TreeMap<UnicardinalPivot<?>, Double> termMap = new TreeMap<>();
        this.closeHelper(termMap, this.getInputs(Reduction.TERMS));
        ArrayList<UnicardinalPivot<?>> resultTerms = new ArrayList<>(termMap.size());
        termMap.forEach((expr, coefficient) -> {
            if (coefficient == 1) {
                resultTerms.add(expr);
            } else if (coefficient != 0) {
                resultTerms.add(this.createScale(coefficient, expr));
            }
        });
        UnicardinalPivot<?> result = switch (resultTerms.size()) {
            case 0 -> this.createConstant(0);
            case 1 -> resultTerms.get(0);
            default -> Diagram.retrieve(this.createRawAdd(resultTerms));
        };
        return result.merge((UnicardinalPivot<?>) this.equalityPivot);
    }

     private void closeHelper(TreeMap<UnicardinalPivot<?>, Double> map, Collection<? extends UnicardinalPivot<?>> args) {
        for (UnicardinalPivot<?> arg : args) {
            this.degree = Math.max(this.degree, arg.element().getDegree());
            switch (arg.element()) {
                case Add addExpr -> this.closeHelper(map, addExpr.getInputs(Reduction.TERMS));
                case Scale sc -> Utils.addToMultiset(map, sc.expression, sc.coefficient);
                case Constant c -> Utils.addToMultiset(map, this.createConstant(1), c.value);
                default -> Utils.addToMultiset(map, arg, 1);
            }
        }
    }

    protected int identity() {
        return 0;
    }
}



