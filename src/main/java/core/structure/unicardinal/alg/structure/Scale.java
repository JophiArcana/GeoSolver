package core.structure.unicardinal.alg.structure;

import core.structure.unicardinal.alg.Constant;
import core.structure.unicardinal.alg.Expression;
import core.util.*;
import com.google.common.collect.TreeMultiset;

public abstract class Scale extends Accumulation {
    /** SECTION: Protected Constructors ============================================================================= */
    protected Scale(double coefficient, Expression expr) {
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
    public Expression expand() {
        if (this.expansion == null) {
            Expression expr = this.expression.expand();
            if (expr instanceof Add addExpr) {
                this.expansion = this.createAdd(Utils.map((TreeMultiset<Expression>) addExpr.inputs.get(Reduction.TERMS),
                        arg -> this.createScale(this.coefficient, arg)));
            } else {
                this.expansion = this.createScale(this.coefficient, expr);
            }
        }
        return this.expansion;
    }

    /** SUBSECTION: Accumulation ==================================================================================== */
    protected int identity() {
        return 0;
    }

    protected Constant evaluateConstant(double coefficient, Constant expression) {
        return expression.mul(this.createReal(coefficient));
    }
}



















