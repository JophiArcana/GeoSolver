package core.structure.unicardinal.alg.symbolic;

import core.structure.unicardinal.alg.structure.Real;
import core.structure.unicardinal.alg.symbolic.operator.*;
import core.structure.unicardinal.alg.symbolic.constant.SymbolicReal;
import core.structure.unicardinal.alg.Expression;

import java.util.*;

public interface SymbolicExpression extends Expression {
    /** SECTION: Implementation ===================================================================================== */
    default List<SymbolicExpression> symbolic() {
        return List.of(this);
    }

    /** SUBSECTION: Expression ====================================================================================== */
    default Real createReal(double value) {
        return SymbolicReal.create(this.getDiagram(), value);
    }

    default Expression createAdd(Iterable<? extends Expression> args) {
        return SymbolicAdd.create((Iterable<SymbolicExpression>) args);
    }

    default Expression createScale(double coefficient, Expression expr) {
        return SymbolicScale.create(coefficient, (SymbolicExpression) expr);
    }
}
