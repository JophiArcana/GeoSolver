package Core.Utilities.Comparators;

import Core.EntityStructure.UnicardinalStructure.*;
import Core.Utilities.Utils;

import java.util.Comparator;

public class UnicardinalComparator<T> implements Comparator<Expression<T>> {
    public int compare(Expression<T> expr1, Expression<T> expr2) {
        if (expr1 instanceof Constant<T> c1 && expr2 instanceof Constant<T> c2) {
            return Constant.compare(c1, c2);
        } else if (expr1.getDegree() != expr2.getDegree()) {
            return -(expr1.getDegree() - expr2.getDegree());
        } else if (expr1.getClass() != expr2.getClass()) {
            return -(Utils.classCode(expr1) - Utils.classCode(expr2));
        } else if (expr1 instanceof Variable<T> var1 && expr2 instanceof Variable<T> var2) {
            return var1.name.compareTo(var2.name);
        } else {
            return Utils.compareInputs(expr1, expr2);
        }
    }
}
