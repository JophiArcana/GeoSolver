package core.util.comparators;

import core.structure.unicardinal.alg.*;
import core.util.Utils;

import java.util.Comparator;

public class UnicardinalComparator implements Comparator<Expression> {
    public int compare(Expression expr1, Expression expr2) {
        if (expr1 instanceof Constant c1 && expr2 instanceof Constant c2) {
            return Constant.compare(c1, c2);
        } else if (expr1.getDegree() != expr2.getDegree()) {
            return -(expr1.getDegree() - expr2.getDegree());
        } else if (expr1.getClass() != expr2.getClass()) {
            return -(Utils.classCode(expr1) - Utils.classCode(expr2));
        } else if (expr1 instanceof Variable var1 && expr2 instanceof Variable var2) {
            return var1.name.compareTo(var2.name);
        } else {
            return Utils.compareInputs(expr1, expr2);
        }
    }
}
