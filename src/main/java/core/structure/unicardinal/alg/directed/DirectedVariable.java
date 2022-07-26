package core.structure.unicardinal.alg.directed;

import core.Propositions.equalitypivot.unicardinal.LockedUnicardinalPivot;
import core.Propositions.equalitypivot.unicardinal.UnicardinalPivot;
import core.structure.unicardinal.Variable;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;
import core.structure.unicardinal.alg.symbolic.SymbolicVariable;

import java.util.*;

public class DirectedVariable extends Variable implements DirectedExpression {
    /** SECTION: Factory Methods ==================================================================================== */
    public static LockedUnicardinalPivot<DirectedExpression, DirectedVariable> create(String n, double v) {
        return (LockedUnicardinalPivot<DirectedExpression, DirectedVariable>) new DirectedVariable(n, v).close();
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected DirectedVariable(String n, double v) {
        super(n, v);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public List<UnicardinalPivot<SymbolicExpression>> symbolic() {
        return List.of(SymbolicVariable.create(this.name + "\u209C", Math.tan(this.doubleValue())));
    }
}
