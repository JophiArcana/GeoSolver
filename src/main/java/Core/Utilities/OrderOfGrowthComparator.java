package Core.Utilities;

import Core.AlgSystem.UnicardinalTypes.*;
import Core.EntityTypes.Mutable;

import java.util.*;

public class OrderOfGrowthComparator<T extends Expression<T>> implements Comparator<Expression<T>>, Algebra<T> {
    public final Class<T> TYPE;
    public final AlgEngine<T> ENGINE;
    public Univariate<T> base;

    public OrderOfGrowthComparator(Univariate<T> x, Class<T> type) {
        this.base = x;
        this.TYPE = type;
        this.ENGINE = Utils.getEngine(this.TYPE);
    }

    public OrderOfGrowthComparator(Class<T> type) {
        this(null, type);
    }

    public int compare(Expression<T> e1, Expression<T> e2) {
        if (e1 == null || e1.equals(this.get(Constants.ZERO))) {
            return -e2.signum();
        } else if (e2 == null || e2.equals(this.get(Constants.ZERO))) {
            return e1.signum();
        } else if (this.base != null) {
            return baseCompare(e1, e2, this.base);
        } else {
            TreeSet<Mutable> commonVars = e1.variables();
            commonVars.retainAll(e2.variables());
            if (commonVars.size() == 0) {
                if (e1 instanceof Constant<T> const1 && e2 instanceof Constant<T> const2) {
                    return const1.compareTo(const2);
                } else if (e1.variables().size() > 0) {
                    return baseCompare(e1, e2, (Univariate<T>) e1.variables().first());
                } else if (e2.variables().size() > 0) {
                    return baseCompare(e1, e2, (Univariate<T>) e2.variables().first());
                } else {
                    return 0;
                }
            } else {
                return baseCompare(e1, e2, (Univariate<T>) commonVars.first());
            }
        }
    }

    private int baseCompare(Expression<T> e1, Expression<T> e2, Univariate<T> var) {
        // System.out.println("Comparing " + e1 + " " + e2);
        e1 = ENGINE.orderOfGrowth(e1, var);
        e2 = ENGINE.orderOfGrowth(e2, var);
        e1 = ENGINE.mul(e1.signum(var), e1);
        e2 = ENGINE.mul(e2.signum(var), e2);

        Expression<T> logDerivative1 = e1.logarithmicDerivative(var);
        Expression<T> logDerivative2 = e2.logarithmicDerivative(var);
        int k = logDerivative1.baseForm().getKey().compareTo(logDerivative2.baseForm().getKey());
        if (k != 0) {
            return k;
        } else {
            return e1.baseForm().getKey().compareTo(e2.baseForm().getKey());
        }
    }

    public Class<T> getType() {
        return this.TYPE;
    }
}
