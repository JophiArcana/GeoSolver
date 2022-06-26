package core.structure.unicardinal.alg.directed.operator;

import core.structure.unicardinal.alg.directed.*;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;
import core.structure.unicardinal.alg.structure.Scale;
import core.structure.unicardinal.alg.symbolic.constant.SymbolicReal;
import core.structure.unicardinal.alg.symbolic.operator.*;
import core.util.Utils;

import java.util.*;

public class DirectedScale extends Scale implements DirectedExpression {
    /** SECTION: Factory Methods ==================================================================================== */
    public static DirectedExpression create(double c, DirectedExpression expr) {
        return (DirectedExpression) new DirectedScale(c, expr).close();
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected DirectedScale(double coefficient, DirectedExpression expr) {
        super(coefficient, expr);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public List<SymbolicExpression> symbolic() {
        if (this.coefficient % 1 == 0) {
            int n = (int) this.coefficient;
            int k = Math.abs(n);
            SymbolicExpression t = this.expression.symbolic().get(0);
            ArrayList<SymbolicExpression> numeratorTerms = new ArrayList<>();
            ArrayList<SymbolicExpression> denominatorTerms = new ArrayList<>(List.of(SymbolicReal.create(1)));
            for (int i = 1; i <= k; i++) {
                SymbolicExpression expr = SymbolicScale.create(Utils.binomial(k, i), SymbolicPow.create(t, i));
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
            SymbolicExpression result = Utils.ENGINE.div(
                    SymbolicAdd.create(numeratorTerms),
                    SymbolicAdd.create(denominatorTerms)
            );
            return List.of((n > 0) ? result : SymbolicScale.create(-1, result));
        } else {
            return null;
        }
    }
}
