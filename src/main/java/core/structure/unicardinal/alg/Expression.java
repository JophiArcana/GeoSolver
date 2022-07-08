package core.structure.unicardinal.alg;

import core.structure.unicardinal.Unicardinal;
import core.structure.unicardinal.alg.structure.Real;

public interface Expression extends Unicardinal {
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
