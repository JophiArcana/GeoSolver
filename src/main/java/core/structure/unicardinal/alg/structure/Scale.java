package core.structure.unicardinal.alg.structure;

import core.Propositions.equalitypivot.unicardinal.*;
import core.structure.unicardinal.*;
import core.util.*;

public abstract class Scale extends Accumulation {
    /** SECTION: Protected Constructors ============================================================================= */
    protected Scale(double coefficient, UnicardinalPivot<?> expr) {
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
    public UnicardinalPivot<?> expand() {
        if (this.expansion == null) {
            UnicardinalPivot<?> expressionExpansion = this.expression.element().expand();
            if (expressionExpansion.element() instanceof Add addExpr) {
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

    protected LockedUnicardinalPivot<?, ? extends Constant> evaluateConstant(double coefficient, Constant expression) {
        return this.createConstant(coefficient * expression.value);
    }
}



















