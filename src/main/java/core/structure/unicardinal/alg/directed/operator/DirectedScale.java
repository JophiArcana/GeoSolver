package core.structure.unicardinal.alg.directed.operator;

import core.structure.equalitypivot.EqualityPivot;
import core.structure.unicardinal.Unicardinal;
import core.structure.unicardinal.alg.directed.DirectedExpression;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;
import core.structure.unicardinal.alg.structure.*;
import core.structure.unicardinal.alg.symbolic.constant.SymbolicReal;
import core.structure.unicardinal.alg.symbolic.operator.*;
import core.util.Utils;

import java.util.*;

public class DirectedScale extends Scale implements DirectedExpression {
    /** SECTION: Factory Methods ==================================================================================== */
    public static EqualityPivot<DirectedExpression> create(double c, EqualityPivot<DirectedExpression> expr) {
        return (EqualityPivot<DirectedExpression>) new DirectedScale(c, expr).close();
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected DirectedScale(double coefficient, EqualityPivot<DirectedExpression> expr) {
        super(coefficient, expr);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public List<EqualityPivot<SymbolicExpression>> symbolic() {
        if (this.coefficient % 1 == 0) {
            int n = (int) this.coefficient;
            int k = Math.abs(n);
            EqualityPivot<SymbolicExpression> t = this.expression.simplestElement.symbolic().get(0);
            ArrayList<EqualityPivot<SymbolicExpression>> numeratorTerms = new ArrayList<>(n << 1 + 1);
            ArrayList<EqualityPivot<SymbolicExpression>> denominatorTerms = new ArrayList<>(List.of(SymbolicReal.ONE));
            for (int i = 1; i <= k; i++) {
                EqualityPivot<SymbolicExpression> expr = SymbolicScale.create(Utils.binomial(k, i), SymbolicPow.create(t, i));
                switch (i % 4) {
                    case 0:
                        denominatorTerms.add(expr);
                    case 1:
                        numeratorTerms.add(expr);
                    case 2:
                        denominatorTerms.add(SymbolicScale.create(-1, expr));
                    case 3:
                        numeratorTerms.add(SymbolicScale.create(-1, expr));
                }
            }
            EqualityPivot<SymbolicExpression> result = SymbolicMul.create(
                    SymbolicAdd.create(numeratorTerms),
                    SymbolicPow.create(SymbolicAdd.create(denominatorTerms), -1)
            );
            return List.of((n > 0) ? result : SymbolicScale.create(-1, result));
        } else {
            return null;
        }
    }

    /** SUBSECTION: Accumulation ==================================================================================== */
    protected Accumulation createRawAccumulation(double coefficient, EqualityPivot<? extends Unicardinal> expression) {
        return new DirectedScale(coefficient, (EqualityPivot<DirectedExpression>) expression);
    }
}
