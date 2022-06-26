package core.structure.unicardinal.alg.directed;

import core.structure.unicardinal.alg.symbolic.*;
import core.Diagram;
import core.structure.unicardinal.alg.Variable;

import java.util.*;

public class DirectedVariable extends Variable implements DirectedExpression {
    /** SECTION: Factory Methods ==================================================================================== */
public static DirectedVariable create(Diagram d, String n, double v) {
        return new DirectedVariable(d, n, v);
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected DirectedVariable(Diagram d, String n, double v) {
        super(d, n, v);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public List<SymbolicExpression> symbolic() {
        return List.of(SymbolicVariable.create(this.diagram, this.name + "\u209C", Math.tan(this.value)));
    }
}
