package Core.AlgSystem.UnicardinalTypes;

import Core.AlgSystem.Constants.*;
import Core.AlgSystem.Operators.*;
import Core.EntityTypes.*;
import Core.Utilities.*;
import javafx.util.Pair;

import java.util.*;

public interface Expression<T extends Expression<T>> extends Unicardinal {
    class Factorization<U extends Expression<U>> {
        public final Class<U> TYPE;
        public final AlgeEngine<U> ENGINE;

        public Constant<U> constant;
        public TreeMap<Expression<U>, Constant<U>> terms;

        public Factorization(Constant<U> c, TreeMap<Expression<U>, Constant<U>> t, Class<U> type) {
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
            for (Map.Entry<Expression<U>, Constant<U>> entry : terms.entrySet()) {
                Expression<U> factor = Pow.create(entry.getKey(), entry.getValue(), TYPE);
                if (Utils.CLOSED_FORM.contains(factor.getClass())) {
                    stringTerms.add(factor.toString());
                } else {
                    stringTerms.add("(" + factor + ")");
                }
            }
            return constString + String.join("", stringTerms);
        }
    }

    default boolean equals(Entity ent) {
        if (ent instanceof DefinedExpression) {
            Expression<T> difference = this.getEngine().sub(this, ent).expand();
            if (difference instanceof Constant<T>) {
                return difference.equals(Constant.ZERO(this.getType()));
            }
        }
        return false;
    }

    default Expression<T> expressionSimplify() {
        Expression<T> reducedExpr = this.reduce();
        Expression<T> reducedExpansion = this.expand().reduce();
        if (this.getEngine().numberOfOperations(reducedExpr) - 2 <= this.getEngine().numberOfOperations(reducedExpansion)) {
            return reducedExpr;
        } else {
            return reducedExpansion;
        }
    }

    default Entity simplify() {
        return this.expressionSimplify();
    }

    default Unicardinal expression(ExpressionType varType) {
        return (varType == Unicardinal.RINGS.getOrDefault(this.getType(), null)) ? this : null;
    }

    Expression<T> reduce();
    Expression<T> expand();
    Expression<T> close();

    Factorization<T> normalize();
    Expression<T> derivative(Univariate<T> var);

    default Expression<T> derivative() {
        return (this instanceof Constant) ? Constant.ZERO(this.getType()) : this.derivative((Univariate<T>) this.variables().first());
    }

    default Expression<T> logarithmicDerivative(Univariate<T> s) {
        return this.getEngine().div(this.derivative(s), this);
    }

    default Pair<Constant<T>, Expression<T>> baseForm() {
        if (this instanceof Mul<T> mulExpr) {
            Mul<T> copy = Mul.create(mulExpr.TYPE);
            copy.terms = mulExpr.terms;
            copy.inputs.get(Mul.Parameter.CONSTANT).add(Constant.ONE(mulExpr.TYPE));
            copy.inputs.get(Mul.Parameter.TERMS).addAll(mulExpr.inputs.get(Mul.Parameter.TERMS));
            return new Pair<>(mulExpr.constant, copy.close());
        } else if (this instanceof Constant<T> constExpr) {
            return new Pair<>(constExpr, Constant.ONE(this.getType()));
        } else {
            return new Pair<>(Constant.ONE(this.getType()), this);
        }
    }

    default int signum(Univariate<T> var) {
        if (this instanceof Complex<T> cpx) {
            if (Math.signum(cpx.re.doubleValue()) != 0) {
                return (int) Math.signum(cpx.re.doubleValue());
            } else {
                return (int) Math.signum(cpx.im.doubleValue());
            }
        } else if (this instanceof Infinity<T> inf) {
            return inf.expression.signum(this.getEngine().X());
        } else {
            Expression<T> order = this.getEngine().orderOfGrowth(this, var);
            if (order instanceof Pow || order instanceof Univariate) {
                return 1;
            } else {
                Complex<T> constant = (Complex<T>) ((Mul<T>) order).constant;
                return (int) Math.signum(constant.re.doubleValue());
            }
        }
    }

    default int signum() {
        TreeSet<Mutable> variables = this.variables();
        return this.signum((variables.size() == 0) ? null : (Univariate<T>) variables.first());
    }

    default Expression<T> fullSubstitute() {
        Expression<T> expr = this;
        Univariate<T> X = this.getEngine().X();
        for (Mutable var : this.variables()) {
            expr = (Expression<T>) expr.substitute(new Pair<>(var, X));
        }
        return expr;
    }

    Class<T> getType();
    AlgeEngine<T> getEngine();

    /** SECTION: Optimizations ====================================================================================== */

    default boolean equalsOne() {
        return (this instanceof Complex<T> cpx) && (cpx.re.doubleValue() == 1 && cpx.im.doubleValue() == 0);
    }

    default boolean equalsZero() {
        return (this instanceof Complex<T> cpx) && (cpx.re.doubleValue() == 0 && cpx.im.doubleValue() == 0);
    }
}
