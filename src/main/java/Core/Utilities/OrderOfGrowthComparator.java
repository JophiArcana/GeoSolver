package Core.Utilities;

import Core.AlgeSystem.ExpressionTypes.Constant;
import Core.AlgeSystem.ExpressionTypes.Expression;
import Core.AlgeSystem.ExpressionTypes.Univariate;
import Core.AlgeSystem.Functions.*;
import Core.EntityTypes.Mutable;

import java.util.*;

public class OrderOfGrowthComparator implements Comparator<Expression> {
    public Univariate base;

    public OrderOfGrowthComparator(Univariate x) {
        this.base = x;
    }

    public OrderOfGrowthComparator() {
        this.base = null;
    }

    public int compare(Expression e1, Expression e2) {
        if (e1 == null || e1.equals(Constant.ZERO)) {
            return -e2.signum();
        } else if (e2 == null || e2.equals(Constant.ZERO)) {
            return e1.signum();
        } else if (this.base != null) {
            return baseCompare(e1, e2, this.base);
        } else {
            HashSet<Mutable> commonVars = e1.variables();
            commonVars.retainAll(e2.variables());
            if (commonVars.size() == 0) {
                if (e1 instanceof Constant const1 && e2 instanceof Constant const2) {
                    return const1.compareTo(const2);
                } else {
                    return 0;
                }
            } else {
                return baseCompare(e1, e2, (Univariate) (new ArrayList<>(commonVars)).get(0));
            }
        }
    }

    private int baseCompare(Expression e1, Expression e2, Univariate var) {
        Expression comparison = AlgeEngine.div(AlgeEngine.orderOfGrowth(e1, var), AlgeEngine.orderOfGrowth(e2, var));
        //System.out.println(String.format("Terms: %s and %s", e1, e2));
        //System.out.println("Comparison: " + comparison);
        if (comparison instanceof Constant) {
            return e1.normalize().constant.compareTo(e2.normalize().constant);
        } else if (!comparison.variables().contains(var)) {
            return 0;
        } else {
            Expression logDerivative = comparison.logarithmicDerivative(var);
            Expression logOrder = AlgeEngine.orderOfGrowth(logDerivative, var);
            //System.out.println("Logarithmic Derivative: " + logDerivative);
            //System.out.println("Order: " + logOrder);
            //System.out.println("Sign: " + logOrder.signum(var));
            if (logOrder instanceof Pow || logOrder instanceof Log || logOrder instanceof Univariate) {
                return 1;
            } else if (logOrder instanceof Mul mulExpr) {
                return mulExpr.constant.compareTo(Constant.ZERO);
            } else if (logOrder instanceof Constant constExpr) {
                return constExpr.compareTo(Constant.ZERO);
            } else {
                return 0;
            }
        }
    }
}
