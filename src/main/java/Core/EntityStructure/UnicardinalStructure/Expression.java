package Core.EntityStructure.UnicardinalStructure;

import Core.EntityStructure.*;
import Core.Utilities.*;

public interface Expression<T> extends Unicardinal {
    default Expression<T> expressionSimplify() {
        Expression<T> reducedExpr = this.reduce();
        Expression<T> reducedExpansion = this.expand().reduce();
        if (Utils.getEngine(this.getType()).numberOfOperations(reducedExpr) > Utils.getEngine(this.getType()).numberOfOperations(reducedExpansion)) {
            return reducedExpansion;
        } else {
            return reducedExpr;
        }
    }

    default Entity simplify() {
        return this.expressionSimplify();
    }

    default Unicardinal expression(ExpressionType varType) {
        if (RINGS.getOrDefault(this.getType(), null) == varType) {
            return this;
        } else {
            return null;
        }
    }

    Expression<T> reduce();
    Expression<T> expand();
    Expression<T> close();

    int getDegree();

    default boolean equalsZero() {
        return false;
    }

    default boolean equalsOne() {
        return false;
    }

    Class<T> getType();
}
