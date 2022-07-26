package core.structure.unicardinal.alg.directed;

import core.Propositions.equalitypivot.unicardinal.LockedUnicardinalPivot;
import core.Propositions.equalitypivot.unicardinal.UnicardinalPivot;
import core.structure.unicardinal.Constant;
import core.structure.unicardinal.alg.symbolic.*;

import java.util.*;

public class DirectedConstant extends Constant implements DirectedExpression {
    /** SECTION: Static Data ======================================================================================== */
    public static final LockedUnicardinalPivot<DirectedExpression, DirectedConstant>
            ZERO = (LockedUnicardinalPivot<DirectedExpression, DirectedConstant>) new DirectedConstant(0).close(),
            ONE = (LockedUnicardinalPivot<DirectedExpression, DirectedConstant>) new DirectedConstant(1).close();

    /** SECTION: Factory Methods ==================================================================================== */
    public static LockedUnicardinalPivot<DirectedExpression, DirectedConstant> create(double value) {
        if (value == 0) {
            return DirectedConstant.ZERO;
        } else if (value == 1) {
            return DirectedConstant.ONE;
        } else {
            return (LockedUnicardinalPivot<DirectedExpression, DirectedConstant>) new DirectedConstant(value).close();
        }
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected DirectedConstant(double value) {
        super(value);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public List<UnicardinalPivot<SymbolicExpression>> symbolic() {
        return List.of(SymbolicConstant.create(Math.tan(this.value)));
    }
}
