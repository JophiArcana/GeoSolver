package core.structure.unicardinal.alg.directed.constant;

import core.structure.unicardinal.alg.directed.DirectedExpression;
import core.structure.unicardinal.alg.symbolic.constant.SymbolicInfinity;
import core.structure.unicardinal.alg.symbolic.constant.SymbolicReal;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;
import core.structure.unicardinal.alg.structure.Real;

import java.util.*;

public class DirectedReal extends Real implements DirectedExpression {
    /** SECTION: Factory Methods ==================================================================================== */
    public static DirectedReal create(double value) {
        return (DirectedReal) new DirectedReal(value).close();
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected DirectedReal(double value) {
        super(value);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public List<SymbolicExpression> symbolic() {
        if (Math.cos(this.value) == 0) {
            return List.of(SymbolicInfinity.create());
        } else {
            return List.of(SymbolicReal.create(Math.tan(this.value)));
        }
    }
}
