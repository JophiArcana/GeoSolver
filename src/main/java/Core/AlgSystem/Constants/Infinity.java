package Core.AlgSystem.Constants;

import Core.AlgSystem.Operators.*;
import Core.AlgSystem.UnicardinalRings.DirectedAngle;
import Core.AlgSystem.UnicardinalRings.Symbolic;
import Core.AlgSystem.UnicardinalTypes.*;
import Core.EntityTypes.*;
import Core.Utilities.*;

import java.util.*;

public class Infinity<T> extends Constant<T> {
    /** SECTION: Instance Variables ================================================================================= */
    public Expression<T> expression;

    /** SECTION: Factory Methods ==================================================================================== */
    public static <T> Infinity<T> create(Class<T> type) {
        return new Infinity<>(type);
    }

    public static <T> Constant<T> create(Expression<T> expr, Class<T> type) {
        return (Constant<T>) new Infinity<>(expr, type).close();
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected Infinity(Class<T> type) {
        super(type);
        this.expression = ENGINE.X();
    }

    private Infinity(Expression<T> expr, Class<T> type) {
        super(type);
        this.expression = (expr == null) ? null : (Expression<T>) expr.simplify();
    }

    /** SECTION: Print Format ======================================================================================= */
    public String toString() {
        return "Infinity(" + this.expression + ")";
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    @Override
    public Entity simplify() {
        if (this.expression instanceof Complex) {
            return this.expression;
        } else {
            return this;
        }
    }

    public ArrayList<Expression<Symbolic>> symbolic() {
        if (this.TYPE == Symbolic.class) {
            return new ArrayList<>(List.of((Constant<Symbolic>) this));
        } else if (this.TYPE == DirectedAngle.class) {
            return null;
        } else {
            return null;
        }
    }

    /** SUBSECTION: Expression ====================================================================================== */
    @Override
    public Expression<T> reduce() {
        return new Infinity<>(this.expression.reduce(), TYPE).close();
    }

    @Override
    public Expression<T> expand() {
        return new Infinity<>(this.expression.expand(), TYPE).close();
    }

    @Override
    public Expression<T> close() {
        if (this.expression instanceof Complex) {
            return this.expression;
        } else {
            return this;
        }
    }

    /** SECTION: Basic Operations =================================================================================== */
    public Constant<T> add(Constant<T> x) {
        if (x instanceof Complex<T> cpx) {
            return new Infinity<>(Add.create(List.of(this.expression, cpx), TYPE), TYPE);
        } else if (x instanceof Infinity<T> inf) {
            return Infinity.create(Add.create(List.of(this.expression, inf.expression), TYPE), TYPE);
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

    public boolean isGaussianInteger() {
        return false;
    }

    public boolean isInteger() {
        return false;
    }

}
