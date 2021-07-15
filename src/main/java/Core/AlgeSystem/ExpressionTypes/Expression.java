package Core.AlgeSystem.ExpressionTypes;

import Core.AlgeSystem.Constants.*;
import Core.AlgeSystem.Functions.*;
import Core.EntityTypes.*;
import Core.EntityTypes.Cardinals.UnicardinalTypes.Unicardinal;
import Core.Utilities.*;

import java.util.*;

public interface Expression extends Unicardinal {
    String varType = Unicardinal.V;

    class Factorization {
        public Constant constant;
        public TreeMap<Expression, Expression> terms;

        public Factorization(Constant c, TreeMap<Expression, Expression> t) {
            constant = c;
            terms = t;
        }

        public String toString() {
            String constString;
            if (constant.equals(Constant.ONE)) {
                constString = "";
            } else if (constant.equals(Constant.NONE)) {
                constString = "-";
            } else {
                constString = constant.toString();
            }
            ArrayList<String> stringTerms = new ArrayList<>();
            for (Map.Entry<Expression, Expression> entry : terms.entrySet()) {
                Expression factor = new Pow(entry.getKey(), entry.getValue()).reduction();
                if (Utils.CLOSED_FORM.contains(factor.getClass())) {
                    stringTerms.add(factor.toString());
                } else {
                    stringTerms.add("(" + factor + ")");
                }
            }
            return constString + String.join("", stringTerms);
        }
    }

    default Entity simplify() {
        Expression reducedExpr = this.reduction();
        if (AlgeEngine.numberOfOperations(reducedExpr) - 2 <= AlgeEngine.numberOfOperations(reducedExpr.expand())) {
            return reducedExpr;
        } else {
            return reducedExpr.expand();
        }
    }

    default ArrayList<Expression> expression() {
        return new ArrayList<>(Collections.singletonList(this));
    }

    default Expression expression(String varType) {
        return (varType.equals(Expression.varType)) ? this : null;
    }

    Expression reduction();
    Expression expand();

    Factorization normalize();
    Expression derivative(Symbol s);

    default Expression derivative() {
        return (this instanceof Constant) ? Constant.ZERO : this.derivative((Symbol) this.variables().first());
    }

    default Expression logarithmicDerivative(Symbol s) {
        return AlgeEngine.div(this.derivative(s), this);
    }

    default int signum(Symbol s) {
        Expression expr = (Expression) this.simplify();
        if (expr instanceof Complex cpx) {
            if (Math.signum(cpx.re.doubleValue()) != 0) {
                return (int) Math.signum(cpx.re.doubleValue());
            } else {
                return (int) Math.signum(cpx.im.doubleValue());
            }
        } else if (expr instanceof Infinity inf) {
            return inf.expression.signum(AlgeEngine.X);
        } else {
            Expression order = AlgeEngine.orderOfGrowth(expr, s);
            if (order instanceof Pow || null instanceof Log || order instanceof Symbol) {
                return 1;
            } else {
                Complex constant = (Complex) ((Mul) order).constant;
                return (int) Math.signum(constant.re.doubleValue());
            }
        }
    }

    default int signum() {
        Expression expr = (Expression) this.simplify();
        if (expr instanceof Complex cpx) {
            if (Math.signum(cpx.re.doubleValue()) != 0) {
                return (int) Math.signum(cpx.re.doubleValue());
            } else {
                return (int) Math.signum(cpx.im.doubleValue());
            }
        } else if (expr instanceof Infinity inf) {
            return inf.expression.signum(AlgeEngine.X);
        } else {
            Expression order = AlgeEngine.orderOfGrowth(expr, (Symbol) this.variables().first());
            if (order instanceof Pow || null instanceof Log || order instanceof Symbol) {
                return 1;
            } else {
                Complex constant = (Complex) ((Mul) order).constant;
                return (int) Math.signum(constant.re.doubleValue());
            }
        }
    }

    default String getVarType() {
        return Expression.varType;
    }
}
