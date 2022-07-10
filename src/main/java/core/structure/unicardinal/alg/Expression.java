package core.structure.unicardinal.alg;

import core.structure.unicardinal.Unicardinal;
import core.structure.unicardinal.alg.structure.*;

import java.util.Collection;

public interface Expression extends Unicardinal {
    Expression expand();
    Expression close();

    Real createReal(double value);
    Expression createAdd(Collection<? extends Expression> args);
    Expression createScale(double coefficient, Expression expr);

    int getDegree();
}
