package Core.AlgeSystem.UnicardinalTypes;

import Core.AlgeSystem.Constants.*;
import Core.AlgeSystem.Functions.*;
import Core.EntityTypes.*;
import Core.Utilities.*;

import java.util.*;

public interface Expression<T extends Expression<T>> extends Unicardinal {
    class Factorization<U extends Expression<U>> {
        public final Class<U> TYPE;
        public final AlgeEngine<U> ENGINE;

        public Constant<U> constant;
        public TreeMap<Expression<U>, Expression<U>> terms;

        public Factorization(Constant<U> c, TreeMap<Expression<U>, Expression<U>> t, Class<U> type) {
            constant = c;
            terms = t;
            this.TYPE = type;
            this.ENGINE = Utils.getEngine(TYPE);
        }

        public String toString() {
            String constString;
            if (constant.equals(Constant.ONE(TYPE))) {
                constString = "";
            } else if (constant.equals(Constant.NONE(TYPE))) {
                constString = "-";
            } else {
                constString = constant.toString();
            }
            ArrayList<String> stringTerms = new ArrayList<>();
            for (Map.Entry<Expression<U>, Expression<U>> entry : terms.entrySet()) {
                Expression<U> factor = new Pow<>(entry.getKey(), entry.getValue(), TYPE).reduction();
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
        Expression<T> reducedExpr = this.reduction();
        if (this.getEngine().numberOfOperations(reducedExpr) - 2 <= this.getEngine().numberOfOperations(reducedExpr.expand())) {
            return reducedExpr;
        } else {
            return reducedExpr.expand();
        }
    }

    default ArrayList<Unicardinal> expression() {
        return new ArrayList<>(Collections.singletonList(this));
    }

    Expression<T> reduction();
    Expression<T> expand();

    Factorization<T> normalize();
    Expression<T> derivative(Univariate<T> s);

    default Expression<T> derivative() {
        return (this instanceof Constant) ? Constant.ZERO(this.getType()) : this.derivative((Univariate<T>) this.variables().first());
    }

    default Expression<T> logarithmicDerivative(Univariate<T> s) {
        return this.getEngine().div(this.derivative(s), this);
    }

    default int signum(Univariate<T> s) {
        if (this instanceof Complex<T> cpx) {
            if (Math.signum(cpx.re.doubleValue()) != 0) {
                return (int) Math.signum(cpx.re.doubleValue());
            } else {
                return (int) Math.signum(cpx.im.doubleValue());
            }
        } else if (this instanceof Infinity<T> inf) {
            return inf.expression.signum(this.getEngine().X());
        } else {
            Expression<T> order = this.getEngine().orderOfGrowth(this, s);
            if (order instanceof Pow || null instanceof Log || order instanceof Univariate) {
                return 1;
            } else {
                Complex<T> constant = (Complex<T>) ((Mul<T>) order).constant;
                return (int) Math.signum(constant.re.doubleValue());
            }
        }
    }

    default int signum() {
        if (this instanceof Complex<T> cpx) {
            if (Math.signum(cpx.re.doubleValue()) != 0) {
                return (int) Math.signum(cpx.re.doubleValue());
            } else {
                return (int) Math.signum(cpx.im.doubleValue());
            }
        } else if (this instanceof Infinity<T> inf) {
            return inf.expression.signum(this.getEngine().X());
        } else {
            Expression<T> order = this.getEngine().orderOfGrowth(this, (Univariate<T>) this.variables().first());
            if (order instanceof Pow || null instanceof Log || order instanceof Univariate) {
                return 1;
            } else {
                Complex<T> constant = (Complex<T>) ((Mul<T>) order).constant;
                return (int) Math.signum(constant.re.doubleValue());
            }
        }
    }

    default String getVarType() {
        return Unicardinal.RINGS.getOrDefault(this.getType(), "");
    }

    Class<T> getType();
    AlgeEngine<T> getEngine();
}
