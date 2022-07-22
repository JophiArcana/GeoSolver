package core.structure.unicardinal.alg.structure;

import core.Diagram;
import core.structure.equalitypivot.*;
import core.structure.unicardinal.Unicardinal;
import core.util.*;

import java.util.*;

public abstract class Add extends Reduction {
    /** SECTION: Protected Constructors ============================================================================= */
    protected Add(Collection<? extends EqualityPivot<? extends Unicardinal>> args) {
        super(args);
    }

    /** SECTION: Print Format ======================================================================================= */
    public String toString() {
        return String.join(" + ", Utils.map(this.getInputs(TERMS), arg -> arg.simplestElement.toString()));
    }

    /** SECTION: Interface ========================================================================================== */
    protected abstract Add createRawAdd(Collection<? extends EqualityPivot<? extends Unicardinal>> args);

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Unicardinal ===================================================================================== */
    public void computeValue() {
        double result = 0;
        for (EqualityPivot<? extends Unicardinal> expr : this.getInputs(TERMS)) {
            result += expr.doubleValue();
        }
        this.value.set(result);
    }

    /** SUBSECTION: Expression ====================================================================================== */
    public EqualityPivot<? extends Unicardinal> expand() {
        if (this.expansion == null) {
            this.expansion = this.createAdd(Utils.map(Utils.cast(this.getInputs(TERMS)), Unicardinal::expand));
        }
        return this.expansion;
    }

    public EqualityPivot<? extends Unicardinal> close() {
        TreeMap<EqualityPivot<? extends Unicardinal>, Double> termMap = new TreeMap<>();
        this.closeHelper(termMap, this.getInputs(TERMS));
        ArrayList<EqualityPivot<? extends Unicardinal>> resultTerms = new ArrayList<>(termMap.size());
        termMap.forEach((expr, coefficient) -> {
            if (coefficient == 1) {
                resultTerms.add(expr);
            } else if (coefficient != 0) {
                resultTerms.add(this.createScale(coefficient, expr));
            }
        });
        EqualityPivot<? extends Unicardinal> result = switch (resultTerms.size()) {
            case 0 -> this.createReal(0);
            case 1 -> resultTerms.get(0);
            default -> Diagram.retrieve(this.createRawAdd(resultTerms));
        };
        return (EqualityPivot<? extends Unicardinal>) this.mergeResult(result);
    }

     private void closeHelper(TreeMap<EqualityPivot<? extends Unicardinal>, Double> map, Collection<? extends EqualityPivot<? extends Unicardinal>> args) {
        for (EqualityPivot<? extends Unicardinal> arg : args) {
            this.degree = Math.max(this.degree, arg.simplestElement.getDegree());
            switch (arg.simplestElement) {
                case Add addExpr -> this.closeHelper(map, addExpr.getInputs(TERMS));
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



