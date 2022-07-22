package core.structure.unicardinal.alg.directed;

import core.structure.equalitypivot.EqualityPivot;
import core.structure.equalitypivot.LockedEqualityPivot;
import core.structure.unicardinal.Variable;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;
import core.structure.unicardinal.alg.symbolic.SymbolicVariable;

import java.util.*;

public class DirectedVariable extends Variable implements DirectedExpression {
    /** SECTION: Factory Methods ==================================================================================== */
    public static LockedEqualityPivot<DirectedExpression> create(String n, double v) {
        return (LockedEqualityPivot<DirectedExpression>) new DirectedVariable(n, v).close();
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected DirectedVariable(String n, double v) {
        super(n, v);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public List<EqualityPivot<SymbolicExpression>> symbolic() {
        return List.of(SymbolicVariable.create(this.name + "\u209C", Math.tan(this.doubleValue())));
    }
}
