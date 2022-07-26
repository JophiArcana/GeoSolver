package core.structure.unicardinal.alg.directed.operator;

import core.Propositions.equalitypivot.unicardinal.UnicardinalPivot;
import core.structure.unicardinal.alg.directed.DirectedExpression;
import core.structure.unicardinal.alg.symbolic.*;
import core.structure.unicardinal.alg.structure.*;
import core.structure.unicardinal.alg.symbolic.operator.*;
import core.util.Utils;

import java.util.*;

public class DirectedScale extends Scale implements DirectedExpression {
    /** SECTION: Factory Methods ==================================================================================== */
    public static UnicardinalPivot<DirectedExpression> create(double c, UnicardinalPivot<DirectedExpression> expr) {
        return (UnicardinalPivot<DirectedExpression>) new DirectedScale(c, expr).close();
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected DirectedScale(double coefficient, UnicardinalPivot<DirectedExpression> expr) {
        super(coefficient, expr);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public List<UnicardinalPivot<SymbolicExpression>> symbolic() {
        if (this.coefficient % 1 == 0) {
            int n = (int) this.coefficient;
            int k = Math.abs(n);
            UnicardinalPivot<SymbolicExpression> t = this.expression.element().symbolic().get(0);
            ArrayList<UnicardinalPivot<SymbolicExpression>> numeratorTerms = new ArrayList<>(n << 1 + 1);
            ArrayList<UnicardinalPivot<SymbolicExpression>> denominatorTerms = new ArrayList<>(List.of(SymbolicConstant.ONE));
            for (int i = 1; i <= k; i++) {
                UnicardinalPivot<SymbolicExpression> expr = SymbolicScale.create(Utils.binomial(k, i), SymbolicPow.create(t, i));
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
            UnicardinalPivot<SymbolicExpression> result = SymbolicMul.create(
                    SymbolicAdd.create(numeratorTerms),
                    SymbolicPow.create(SymbolicAdd.create(denominatorTerms), -1)
            );
            return List.of((n > 0) ? result : SymbolicScale.create(-1, result));
        } else {
            return null;
        }
    }

    /** SUBSECTION: Accumulation ==================================================================================== */
    protected Accumulation createRawAccumulation(double coefficient, UnicardinalPivot<?> expression) {
        return new DirectedScale(coefficient, (UnicardinalPivot<DirectedExpression>) expression);
    }
}
