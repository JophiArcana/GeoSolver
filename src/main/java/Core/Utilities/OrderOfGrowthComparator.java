package Core.Utilities;

import Core.AlgeSystem.UnicardinalTypes.*;
import Core.AlgeSystem.Functions.*;
import Core.EntityTypes.Mutable;

import java.util.*;
import javafx.util.*;

public class OrderOfGrowthComparator<T extends Expression<T>> implements Comparator<Expression<T>> {
    public final Class<T> TYPE;
    public final AlgeEngine<T> ENGINE;
    public Univariate<T> base;

    public OrderOfGrowthComparator(Univariate<T> x, Class<T> type) {
        this.base = x;
        this.TYPE = type;
        this.ENGINE = Utils.getEngine(this.TYPE);
    }

    public OrderOfGrowthComparator(Class<T> type) {
        this.base = null;
        this.TYPE = type;
        this.ENGINE = Utils.getEngine(this.TYPE);
    }

    public int compare(Expression<T> e1, Expression<T> e2) {
        if (e1 == null || e1.equals(Constant.ZERO(TYPE))) {
            return -e2.signum();
        } else if (e2 == null || e2.equals(Constant.ZERO(TYPE))) {
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
        if (e1 instanceof Log<T> || e2 instanceof Log<T>) {
            return this.baseCompare(ENGINE.exp(e1), ENGINE.exp(e2), var);
        } else {
            Pair<Constant<T>, Expression<T>> baseForm1 = e1.baseForm();
            Pair<Constant<T>, Expression<T>> baseForm2 = e2.baseForm();
            if (baseForm1.getValue().equals(baseForm2.getValue())) {
                return baseForm1.getKey().compareTo(baseForm2.getKey());
            } else {
                Expression<T> logDerivative1 = e1.logarithmicDerivative(var);
                Expression<T> logDerivative2 = e2.logarithmicDerivative(var);
                int sign1 = logDerivative1.signum(var);
                int sign2 = logDerivative2.signum(var);
                Constant<T> NONE = Constant.NONE(TYPE);
                if (sign1 != sign2) {
                    return sign1 - sign2;
                } else if (sign1 == 0) {
                    return 0;
                } else if (sign1 == -1) {
                    return this.baseCompare(ENGINE.mul(NONE, logDerivative2), ENGINE.mul(NONE, logDerivative1), var);
                } else {
                    return this.baseCompare(logDerivative1, logDerivative2, var);
                }
            }
        }
        /**e1 = ENGINE.orderOfGrowth(e1, var);
        e2 = ENGINE.orderOfGrowth(e2, var);
        System.out.println(e1 + " and " + e2 + " comparison with " + var);
        Expression<T> comparison = ENGINE.div(e1, e2);
        //System.out.println(e1 + " and " + e2 + " comparison: " + comparison);
        if (comparison instanceof Constant) {
            return e1.normalize().constant.compareTo(e2.normalize().constant);
        } else if (!comparison.variables().contains(var)) {
            return 0;
        } else {
            System.out.println("Here: " + e1 + " and " + e2);
            Expression<T> logDerivative = comparison.logarithmicDerivative(var);
            Expression<T> logOrder = ENGINE.orderOfGrowth(logDerivative, var);
            if (logOrder instanceof Pow || logOrder instanceof Log || logOrder instanceof Univariate) {
                return 1;
            } else if (logOrder instanceof Mul<T> mulExpr) {
                return mulExpr.constant.compareTo(Constant.ZERO(TYPE));
            } else if (logOrder instanceof Constant<T> constExpr) {
                return constExpr.compareTo(Constant.ZERO(TYPE));
            } else {
                return 0;
            }
        }*/
    }
}
