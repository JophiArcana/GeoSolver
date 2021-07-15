package Core.AlgeSystem.Constants;

import Core.AlgeSystem.ExpressionTypes.*;
import Core.AlgeSystem.ExpressionTypes.Symbol;
import Core.EntityTypes.*;
import Core.Utilities.*;

import java.util.ArrayList;

public class Infinity extends Constant {
    public Expression expression;

    public Infinity(Expression expr) {
        this.expression = (expr == null) ? null : (Expression) expr.simplify();
    }

    public Infinity() {
        this.expression = AlgeEngine.X;
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

    public Constant add(Constant x) {
        if (x instanceof Complex cpx) {
            return new Infinity(AlgeEngine.add(this.expression, cpx));
        } else if (x instanceof Infinity inf) {
            return (Constant) (new Infinity(AlgeEngine.add(this.expression, inf.expression))).simplify();
        } else {
            return this;
        }
    }

    public Constant sub(Constant x) {
        if (x instanceof Complex cpx) {
            return new Infinity(AlgeEngine.sub(this.expression, cpx));
        } else if (x instanceof Infinity inf) {
            return (Constant) (new Infinity(AlgeEngine.sub(this.expression, inf.expression))).simplify();
        } else {
            return this;
        }
    }

    public Constant mul(Constant x) {
        if (x instanceof Complex cpx) {
            return new Infinity(AlgeEngine.mul(this.expression, cpx));
        } else if (x instanceof Infinity inf) {
            return new Infinity(AlgeEngine.mul(this.expression, inf.expression));
        } else {
            return this;
        }
    }

    public Constant div(Constant x) {
        return this.mul(x.inverse());
    }

    public Constant inverse() {
        return new Infinity(AlgeEngine.div(Constant.ONE, this.expression));
    }

    public Constant conjugate() {
        return new Infinity(AlgeEngine.conjugate(this.expression));
    }

    public Constant exp() {
        return new Infinity(AlgeEngine.pow(Constant.E, this.expression));
    }

    public Constant log() {
        return new Infinity(AlgeEngine.log(this.expression));
    }

    public Constant pow(Constant x) {
        return new Infinity(AlgeEngine.pow(this.expression, x));
    }

    /** TODO: Implement sine function */
    public Constant sin() {
        return null;
    }

    /** TODO: Implement cosine function */
    public Constant cos() {
        return null;
    }

    /** TODO: Implement tangent function */
    public Constant tan() {
        return null;
    }

    /** TODO: Fix after implementing normalize */

    public double abs() {
        ArrayList<Mutable> vars = new ArrayList<>(this.expression.variables());
        Expression order = AlgeEngine.orderOfGrowth(this.expression, (Symbol) vars.get(0));
        return switch ((int) Math.signum(Utils.GROWTH_COMPARATOR.compare(order, Constant.ONE))) {
            case 1 -> Integer.MAX_VALUE;
            case 0 -> ((Constant) order).abs();
            case -1 -> 0;
            default -> Integer.MIN_VALUE;
        };
    }

    public Constant gcd(Constant c) {
        if (c instanceof Infinity inf) {
            return new Infinity(AlgeEngine.greatestCommonDivisor(this.expression, inf.expression));
        } else {
            return c;
        }
    }

    /** TODO: Implement argument function */
    public double phase() {
        return Double.MAX_VALUE;
    }

    public int compareTo(Entity ent) {
        if (ent instanceof Infinity inf) {
            return Utils.GROWTH_COMPARATOR.compare(this.expression, inf.expression);
        } else {
            return Integer.MIN_VALUE;
        }
    }
}
