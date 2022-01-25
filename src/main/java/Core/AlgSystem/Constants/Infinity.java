package Core.AlgSystem.Constants;

import Core.AlgSystem.UnicardinalRings.DirectedAngle;
import Core.AlgSystem.UnicardinalRings.Symbolic;
import Core.AlgSystem.UnicardinalTypes.*;
import Core.EntityTypes.*;
import Core.Utilities.Utils;

import java.util.*;

public class Infinity<T> extends Constant<T> {
    /** SECTION: Instance Variables ================================================================================= */
    public double coefficient, degree;

    /** SECTION: Factory Methods ==================================================================================== */
    public static <T> Infinity<T> create(Class<T> type) {
        return new Infinity<>(type);
    }

    public static <T> Constant<T> create(double c, double d, Class<T> type) {
        return (Constant<T>) new Infinity<>(c, d, type).close();
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected Infinity(Class<T> type) {
        super(type);
        this.coefficient = 1;
        this.degree = 1;
    }

    protected Infinity(double c, double d, Class<T> type) {
        super(type);
        this.coefficient = c;
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
        if (this.degree == 0 || this.coefficient == 0) {
            return new Real<>(this.coefficient, TYPE);
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
        if (this.degree == 0 || this.coefficient == 0) {
            return new Real<>(this.coefficient, TYPE);
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
                return Infinity.create(this.coefficient + inf.coefficient, this.degree, TYPE);
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
                return Infinity.create(this.coefficient - inf.coefficient, this.degree, TYPE);
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
        if (x instanceof Real<T> re) {
            return Infinity.create(this.coefficient * re.value, this.degree, TYPE);
        } else if (x instanceof Infinity<T> inf) {
            return Infinity.create(this.coefficient * inf.coefficient, this.degree + inf.degree, TYPE);
        } else {
            return null;
        }
    }

    public Constant<T> div(Constant<T> x) {
        if (x instanceof Real<T> re) {
            if (re.value == 0) {
                return Infinity.create(this.coefficient, this.degree + 1, TYPE);
            } else {
                return Infinity.create(this.coefficient / re.value, this.degree, TYPE);
            }
        } else if (x instanceof Infinity<T> inf) {
            return Infinity.create(this.coefficient / inf.coefficient, this.degree - inf.degree, TYPE);
        } else {
            return null;
        }
    }

    public Constant<T> negate() {
        return new Infinity<>(-this.coefficient, this.degree, TYPE);
    }

    public Constant<T> invert() {
        return new Infinity<>(1 / this.coefficient, -this.degree, TYPE);
    }

    public Constant<T> pow(double x) {
        return Infinity.create(Math.pow(this.coefficient, x), this.degree * x, TYPE);
    }

    public Constant<T> gcd(Constant<T> c) {
        if (c instanceof Infinity<T> inf) {
            return Infinity.create(Utils.gcd(this.coefficient, inf.coefficient), Math.min(this.degree, inf.degree), TYPE);
        } else if (c instanceof Real<T>) {
            return c;
        } else {
            return null;
        }
    }

    public boolean isInteger() {
        return false;
    }
}
