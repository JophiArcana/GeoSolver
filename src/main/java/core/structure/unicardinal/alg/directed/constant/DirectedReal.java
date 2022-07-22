package core.structure.unicardinal.alg.directed.constant;

import core.structure.equalitypivot.EqualityPivot;
import core.structure.equalitypivot.LockedEqualityPivot;
import core.structure.unicardinal.alg.directed.DirectedExpression;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;
import core.structure.unicardinal.alg.structure.Real;
import core.structure.unicardinal.alg.symbolic.constant.*;

import java.util.*;

public class DirectedReal extends Real implements DirectedExpression {
    /** SECTION: Static Data ======================================================================================== */
    public static final LockedEqualityPivot<DirectedExpression, DirectedReal>
            ZERO = (LockedEqualityPivot<DirectedExpression, DirectedReal>) new DirectedReal(0).close(),
            ONE = (LockedEqualityPivot<DirectedExpression, DirectedReal>) new DirectedReal(1).close();

    /** SECTION: Factory Methods ==================================================================================== */
    public static LockedEqualityPivot<DirectedExpression, DirectedReal> create(double value) {
        if (value == 0) {
            return DirectedReal.ZERO;
        } else if (value == 1) {
            return DirectedReal.ONE;
        } else {
            return (LockedEqualityPivot<DirectedExpression, DirectedReal>) new DirectedReal(value).close();
        }
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected DirectedReal(double value) {
        super(value);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public List<EqualityPivot<SymbolicExpression>> symbolic() {
        if (Math.cos(this.value) == 0) {
            return List.of(SymbolicInfinity.create());
        } else {
            return List.of(SymbolicReal.create(Math.tan(this.value)));
        }
    }
}
