package core.structure.unicardinal.alg.directed;

import core.structure.unicardinal.alg.symbolic.*;
import core.structure.unicardinal.alg.Variable;

import java.util.*;

public class DirectedVariable extends Variable implements DirectedExpression {
    /** SECTION: Factory Methods ==================================================================================== */
public static DirectedVariable create(String n, double v) {
        return new DirectedVariable(n, v);
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected DirectedVariable(String n, double v) {
        super(n, v);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public List<SymbolicExpression> symbolic() {
        return List.of(SymbolicVariable.create(this.name + "\u209C", Math.tan(this.value)));
    }
}
