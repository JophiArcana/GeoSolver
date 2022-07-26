package core.structure.unicardinal.alg.directed;

import core.Propositions.equalitypivot.unicardinal.LockedUnicardinalPivot;
import core.Propositions.equalitypivot.unicardinal.UnicardinalPivot;
import core.structure.unicardinal.alg.directed.operator.*;
import core.structure.unicardinal.Unicardinal;

import java.util.Collection;

public interface DirectedExpression extends Unicardinal {
    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Expression ====================================================================================== */
    default LockedUnicardinalPivot<DirectedExpression, DirectedConstant> createConstant(double value) {
        return DirectedConstant.create(value);
    }

    default UnicardinalPivot<DirectedExpression> createAdd(Collection<? extends UnicardinalPivot<?>> args) {
        return DirectedAdd.create((Collection<UnicardinalPivot<DirectedExpression>>) args);
    }

    default UnicardinalPivot<DirectedExpression> createScale(double coefficient, UnicardinalPivot<?> expr) {
        return DirectedScale.create(coefficient, (UnicardinalPivot<DirectedExpression>) expr);
    }

    default int getDegree() {
        return 0;
    }
}
