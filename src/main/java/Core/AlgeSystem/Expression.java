package Core.AlgeSystem;

import Core.AlgeSystem.Functions.*;
import Core.EntityTypes.*;
import Core.Utilities.*;

import java.util.*;

public interface Expression extends Entity {
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
                Expression factor = ASEngine.pow(entry.getKey(), entry.getValue());
                if (Utils.CLOSED_FORM.contains(factor.getClass())) {
                    stringTerms.add(factor.toString());
                } else {
                    stringTerms.add("(" + factor + ")");
                }
            }
            return constString + String.join("", stringTerms);
        }
    }

    default ArrayList<Expression> expression() {
        return new ArrayList<>(Collections.singletonList((Expression) this.simplify()));
    }

    Factorization normalize();
    Expression derivative(Symbol s);

    default Expression derivative() {
        if (this instanceof Constant) {
            return Constant.ZERO;
        } else {
            ArrayList<Mutable> vars = new ArrayList<>(this.variables());
            return this.derivative((Symbol) vars.get(0));
        }
    }

    default Expression logarithmicDerivative(Symbol s) {
        return ASEngine.div(this.derivative(s), this);
    }

    default Expression logarithmicDerivative() {
        return ASEngine.div(this.derivative(), this);
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
            return inf.expression.signum(ASEngine.X);
        } else {
            Expression order = ASEngine.orderOfGrowth(expr, s);
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
            return inf.expression.signum(ASEngine.X);
        } else {
            ArrayList<Mutable> vars = new ArrayList<>(this.variables());
            Expression order = ASEngine.orderOfGrowth(expr, (Symbol) vars.get(0));
            if (order instanceof Pow || null instanceof Log || order instanceof Symbol) {
                return 1;
            } else {
                Complex constant = (Complex) ((Mul) order).constant;
                return (int) Math.signum(constant.re.doubleValue());
            }
        }
    }
}
