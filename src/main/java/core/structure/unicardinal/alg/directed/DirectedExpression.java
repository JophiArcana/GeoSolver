package core.structure.unicardinal.alg.directed;

import core.structure.unicardinal.alg.directed.operator.DirectedAdd;
import core.structure.unicardinal.alg.structure.Add;
import core.structure.unicardinal.alg.structure.Real;
import core.structure.unicardinal.alg.Expression;
import core.structure.unicardinal.alg.directed.constant.DirectedReal;
import core.structure.unicardinal.alg.directed.operator.DirectedScale;

import java.util.Collection;

public interface DirectedExpression extends Expression {
    /** SECTION: Implementation ===================================================================================== */

    /** SUBSECTION: Expression ====================================================================================== */
    default Real createReal(double value) {
        return DirectedReal.create(value);
    }

    default Expression createAdd(Collection<? extends Expression> args) {
        return DirectedAdd.create((Collection<DirectedExpression>) args);
    }

    default Expression createScale(double coefficient, Expression expr) {
        return DirectedScale.create(coefficient, (DirectedExpression) expr);
    }

    default int getDegree() {
        return 0;
    }
}
