package core.structure.unicardinal.alg.directed;

import core.structure.equalitypivot.*;
import core.structure.unicardinal.alg.directed.constant.DirectedReal;
import core.structure.unicardinal.alg.directed.operator.*;
import core.structure.unicardinal.Unicardinal;

import java.util.Collection;

public interface DirectedExpression extends Unicardinal {
    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Expression ====================================================================================== */
    default LockedEqualityPivot<DirectedExpression, DirectedReal> createReal(double value) {
        return DirectedReal.create(value);
    }

    default EqualityPivot<DirectedExpression> createAdd(Collection<? extends EqualityPivot<? extends Unicardinal>> args) {
        return DirectedAdd.create((Collection<EqualityPivot<DirectedExpression>>) args);
    }

    default EqualityPivot<DirectedExpression> createScale(double coefficient, EqualityPivot<? extends Unicardinal> expr) {
        return DirectedScale.create(coefficient, (EqualityPivot<DirectedExpression>) expr);
    }

    default int getDegree() {
        return 0;
    }
}
