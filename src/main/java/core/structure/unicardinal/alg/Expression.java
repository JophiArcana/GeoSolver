package core.structure.unicardinal.alg;

import core.structure.unicardinal.Unicardinal;
import core.structure.unicardinal.alg.structure.Real;
import core.structure.*;
import core.util.*;

public interface Expression extends Unicardinal {
    default Expression expressionSimplify() {
        Expression reducedExpr = this;
        Expression reducedExpansion = this.expand();
        if (Utils.ENGINE.numberOfOperations(reducedExpr) > Utils.ENGINE.numberOfOperations(reducedExpansion)) {
            return reducedExpansion;
        } else {
            return reducedExpr;
        }
    }

    default Entity simplify() {
        return this.expressionSimplify();
    }

    Expression expand();
    Expression close();

    Real createReal(double value);
    Expression createAdd(Iterable<? extends Expression> args);
    Expression createScale(double coefficient, Expression expr);

    int getDegree();

    default boolean equalsZero() {
        return false;
    }

    default boolean equalsOne() {
        return false;
    }
}
