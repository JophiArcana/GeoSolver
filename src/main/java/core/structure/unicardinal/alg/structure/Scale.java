package core.structure.unicardinal.alg.structure;

import core.structure.equalitypivot.*;
import core.structure.unicardinal.*;
import core.util.*;

public abstract class Scale extends Accumulation {
    /** SECTION: Protected Constructors ============================================================================= */
    protected Scale(double coefficient, EqualityPivot<? extends Unicardinal> expr) {
        super(coefficient, expr);
    }

    /** SECTION: Print Format ======================================================================================= */
    public String toString() {
        return "" + this.coefficient + this.expression;
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Unicardinal ===================================================================================== */
    public void computeValue() {
        this.value.setValue(this.coefficient * this.expression.doubleValue());
    }

    /** SUBSECTION: Expression ====================================================================================== */
    public EqualityPivot<? extends Unicardinal> expand() {
        if (this.expansion == null) {
            EqualityPivot<? extends Unicardinal> expressionExpansion = this.expression.simplestElement.expand();
            if (expressionExpansion.simplestElement instanceof Add addExpr) {
                this.expansion = this.createAdd(Utils.map(addExpr.getInputs(Reduction.TERMS),
                        arg -> this.createScale(this.coefficient, arg)));
            } else {
                this.expansion = this.createScale(this.coefficient, expressionExpansion);
            }
        }
        return this.expansion;
    }

    /** SUBSECTION: Accumulation ==================================================================================== */
    protected int identity() {
        return 0;
    }

    protected LockedEqualityPivot<? extends Unicardinal, ? extends Constant> evaluateConstant(double coefficient, Constant expression) {
        return this.createReal(coefficient * expression.value);
    }
}



















