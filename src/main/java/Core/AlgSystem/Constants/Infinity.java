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
    public Complex<T> coefficient;
    public double degree;

    /** SECTION: Factory Methods ==================================================================================== */
    public static <T> Infinity<T> create(Class<T> type) {
        return new Infinity<>(type);
    }

    public static <T> Constant<T> create(Complex<T> cpx, double d, Class<T> type) {
        return (Constant<T>) new Infinity<>(cpx, d, type).close();
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected Infinity(Class<T> type) {
        super(type);
        this.coefficient = Constant.ONE(TYPE);
        this.degree = 1;
    }

    protected Infinity(Complex<T> cpx, double d, Class<T> type) {
        super(type);
        this.coefficient = cpx;
        this.degree = d;
    }

    /** SECTION: Print Format ======================================================================================= */
    public String toString() {
        return "Infinity(" + this.coefficient + " * \u221E ** " + this.degree + ")";
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    @Override
    public Entity simplify() {
        if (this.degree == 0) {
            return this.coefficient;
        } else if (this.coefficient.equalsZero()) {
            return this.coefficient;
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
    public Expression<T> close() {
        if (this.degree == 0) {
            return this.coefficient;
        } else if (this.coefficient.equalsZero()) {
            return this.coefficient;
        } else {
            return this;
        }
    }

    /** SECTION: Basic Operations =================================================================================== */
    public Constant<T> add(Constant<T> x) {
        if (x instanceof Infinity<T> inf) {
            if (inf.degree > this.degree) {
                return inf;
            } else if (inf.degree == this.degree) {
                return Infinity.create((Complex<T>) this.coefficient.add(inf.coefficient), this.degree, TYPE);
            } else {
                return this;
            }
        } else {
            if (this.degree > 0) {
                return this;
            } else {
                return x;
            }
        }
    }

    public Constant<T> sub(Constant<T> x) {
        if (x instanceof Infinity<T> inf) {
            if (inf.degree > this.degree) {
                return inf.negate();
            } else if (inf.degree == this.degree) {
                return Infinity.create((Complex<T>) this.coefficient.sub(inf.coefficient), this.degree, TYPE);
            } else {
                return this;
            }
        } else {
            if (this.degree > 0) {
                return this;
            } else {
                return x.negate();
            }
        }
    }

    public Constant<T> mul(Constant<T> x) {
        if (x instanceof Complex<T>) {
            return Infinity.create((Complex<T>) this.coefficient.mul(x), this.degree, TYPE);
        } else if (x instanceof Infinity<T> inf) {
            return Infinity.create((Complex<T>) this.coefficient.mul(inf.coefficient), this.degree + inf.degree, TYPE);
        } else {
            return this;
        }
    }

    public Constant<T> div(Constant<T> x) {
        return this.mul(x.inverse());
    }

    public Constant<T> negate() {
        return new Infinity<>((Complex<T>) this.coefficient.negate(), this.degree, TYPE);
    }

    public Constant<T> inverse() {
        return new Infinity<>(this.coefficient, -this.degree, TYPE);
    }

    public Constant<T> conjugate() {
        return new Infinity<>((Complex<T>) this.coefficient.conjugate(), this.degree, TYPE);
    }

    public Constant<T> pow(double x) {
        return Infinity.create((Complex<T>) this.coefficient.pow(x), this.degree * x, TYPE);
    }

    /** TODO: Fix after implementing normalize */

    public double abs() {
        if (this.degree > 0) {
            return Double.MAX_VALUE;
        } else {
            return 0;
        }
    }

    public Constant<T> gcd(Constant<T> c) {
        if (c instanceof Infinity<T> inf) {
            if (inf.degree > this.degree) {
                return this;
            } else if (inf.degree == this.degree) {
                return new Infinity((Complex<T>) this.coefficient.gcd(inf.coefficient), this.degree, TYPE);
            } else {
                return inf;
            }
        } else {
            return c;
        }
    }

    public boolean isGaussianInteger() {
        return false;
    }

    public boolean isInteger() {
        return false;
    }
}
