package Core.AlgeSystem.Constants;

import Core.AlgeSystem.UnicardinalTypes.*;
import Core.EntityTypes.*;
import Core.Utilities.*;

import java.util.Arrays;

public class Infinity<T extends Expression<T>> extends Constant<T> {
    public Expression<T> expression;

    public Infinity(Expression<T> expr, Class<T> type) {
        super(type);
        this.expression = (expr == null) ? null : (Expression<T>) expr.simplify();
    }

    public Infinity(Class<T> type) {
        super(type);
        this.expression = ENGINE.X();
    }

    public String toString() {
        return "Infinity(" + this.expression + ")";
    }

    @Override
    public Entity simplify() {
        if (this.expression instanceof Constant) {
            return this.expression;
        } else {
            return this;
        }
    }

    public Constant<T> add(Constant<T> x) {
        if (x instanceof Complex<T> cpx) {
            return new Infinity<>(ENGINE.add(this.expression, cpx), TYPE);
        } else if (x instanceof Infinity<T> inf) {
            return ENGINE.infinity(ENGINE.add(this.expression, inf.expression));
        } else {
            return this;
        }
    }

    public Constant<T> sub(Constant<T> x) {
        if (x instanceof Complex<T> cpx) {
            return new Infinity<>(ENGINE.sub(this.expression, cpx), TYPE);
        } else if (x instanceof Infinity<T> inf) {
            return ENGINE.infinity(ENGINE.sub(this.expression, inf.expression));
        } else {
            return this;
        }
    }

    public Constant<T> mul(Constant<T> x) {
        if (x instanceof Complex<T> cpx) {
            return new Infinity<>(ENGINE.mul(this.expression, cpx), TYPE);
        } else if (x instanceof Infinity<T> inf) {
            return ENGINE.infinity(ENGINE.mul(this.expression, inf.expression));
        } else {
            return this;
        }
    }

    public Constant<T> div(Constant<T> x) {
        return this.mul(x.inverse());
    }

    public Constant<T> inverse() {
        return new Infinity<>(ENGINE.div(Constant.ONE(TYPE), this.expression), TYPE);
    }

    public Constant<T> conjugate() {
        return new Infinity<>(ENGINE.conjugate(this.expression), TYPE);
    }

    public Constant<T> exp() {
        return new Infinity<>(ENGINE.pow(Constant.E(TYPE), this.expression), TYPE);
    }

    public Constant<T> pow(Constant<T> x) {
        return ENGINE.infinity(ENGINE.pow(this.expression, x));
    }

    /** TODO: Implement sine function */
    public Constant<T> sin() {
        return null;
    }

    /** TODO: Implement cosine function */
    public Constant<T> cos() {
        return null;
    }

    /** TODO: Implement tangent function */
    public Constant<T> tan() {
        return null;
    }

    /** TODO: Fix after implementing normalize */

    public double abs() {
        Expression<T> order = ENGINE.orderOfGrowth(this.expression, ENGINE.X());
        return switch ((int) Math.signum(Utils.getGrowthComparator(TYPE).compare(order, Constant.ONE(TYPE)))) {
            case 1 -> Integer.MAX_VALUE;
            case 0 -> ((Constant<T>) order).abs();
            case -1 -> 0;
            default -> Integer.MIN_VALUE;
        };
    }

    public Constant<T> gcd(Constant<T> c) {
        if (c instanceof Infinity<T> inf) {
            return ENGINE.infinity(ENGINE.greatestCommonDivisor(Arrays.asList(this.expression, inf.expression)));
        } else {
            return c;
        }
    }

    /** TODO: Implement argument function */
    public double phase() {
        return Double.MAX_VALUE;
    }

    public boolean gaussianInteger() {
        return false;
    }

    public boolean integer() {
        return false;
    }

    public boolean positiveInteger() {
        return false;
    }

    public int compareTo(Immutable immutable) {
        if (immutable instanceof Infinity) {
            return Utils.getGrowthComparator(TYPE).compare(this.expression, ((Infinity<T>) immutable).expression);
        } else {
            return Integer.MIN_VALUE;
        }
    }
}
