package core.structure.unicardinal.alg.directed.constant;

import core.structure.unicardinal.alg.directed.DirectedExpression;
import core.structure.unicardinal.alg.symbolic.constant.SymbolicInfinity;
import core.structure.unicardinal.alg.symbolic.constant.SymbolicReal;
import core.Diagram;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;
import core.structure.unicardinal.alg.structure.Real;

import java.util.*;

public class DirectedReal extends Real implements DirectedExpression {
    /** SECTION: Factory Methods ==================================================================================== */
    public static DirectedReal create(Diagram d, double value) {
        return new DirectedReal(d, value);
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected DirectedReal(Diagram d, double value) {
        super(d, value);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public List<SymbolicExpression> symbolic() {
        if (Math.cos(this.value) == 0) {
            return List.of(SymbolicInfinity.create(this.diagram));
        } else {
            return List.of(SymbolicReal.create(this.diagram, Math.tan(this.value)));
        }
    }
}
