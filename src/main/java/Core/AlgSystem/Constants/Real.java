package Core.AlgSystem.Constants;

import Core.AlgSystem.UnicardinalRings.*;
import Core.EntityStructure.UnicardinalStructure.Constant;
import Core.EntityStructure.UnicardinalStructure.Expression;
import Core.Utilities.*;

import java.util.ArrayList;
import java.util.List;

public class Real<T> extends Constant<T> {
    /** SECTION: Instance Variables ================================================================================= */
    public final double value;

    /** SECTION: Factory Methods ==================================================================================== */
    public static <T> Real<T> create(double value, Class<T> type) {
        return new Real<>(value, type);
    }

    /** SECTION: Protected Constructors ============================================================================= */
    public Real(double value, Class<T> type) {
        super(type);
        assert !Double.isNaN(value): value;
        if (Math.abs(value - Math.round(value)) < AlgEngine.EPSILON) {
            this.value = Math.round(value);
        } else {
            this.value = value;
        }
    }

    /** SECTION: Print Format ======================================================================================= */
    public String toString() {
        return "" + this.value;
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public ArrayList<Expression<Symbolic>> symbolic() {
        if (this.TYPE == Symbolic.class) {
            return new ArrayList<>(List.of((Constant<Symbolic>) this));
        } else if (this.TYPE == DirectedAngle.class) {
            if (Math.cos(this.value) == 0) {
                return new ArrayList<>(List.of(Infinity.create(Symbolic.class)));
            } else {
                return new ArrayList<>(List.of(Real.create(Math.tan(this.value), Symbolic.class)));
            }
        } else {
            return null;
        }
    }

    /** SUBSECTION: Immutable ======================================================================================= */
    public boolean equalsZero() {
        return this.value == 0;
    }

    public boolean equalsOne() {
        return this.value == 1;
    }

    /** SECTION: Basic Operations =================================================================================== */
    public Constant<T> add(Constant<T> c) {
        if (c instanceof Real<T> re) {
            return new Real<>(this.value + re.value, TYPE);
        } else if (c instanceof Infinity<T> inf) {
            if (inf.degree > 0) {
                return inf;
            } else {
                return this;
            }
        } else {
            return null;
        }
    }

    public Constant<T> sub(Constant<T> c) {
        if (c instanceof Real<T> re) {
            return new Real<>(this.value - re.value, TYPE);
        } else if (c instanceof Infinity<T> inf) {
            if (inf.degree > 0) {
                return inf.negate();
            } else {
                return this;
            }
        } else {
            return null;
        }
    }

    public Constant<T> mul(Constant<T> c) {
        if (c instanceof Real<T> re) {
            return new Real<>(this.value * re.value, TYPE);
        } else if (c instanceof Infinity<T> inf) {
            return Infinity.create(this.value * inf.coefficient, inf.degree, TYPE);
        } else {
            return this;
        }
    }

    public Constant<T> div(Constant<T> c) {
        if (c instanceof Real<T> re) {
            if (re.value == 0) {
                return Infinity.create(this.value, 1, TYPE);
            } else {
                return new Real<>(this.value / re.value, TYPE);
            }
        } else if (c instanceof Infinity<T> inf) {
            return Infinity.create(this.value / inf.coefficient, -inf.degree, TYPE);
        } else {
            return null;
        }
    }

    public Constant<T> negate() {
        return new Real<>(-this.value, TYPE);
    }

    public Constant<T> invert() {
        if (this.value == 0) {
            return Infinity.create(TYPE);
        } else {
            return new Real<>(1 / this.value, TYPE);
        }
    }

    public Constant<T> pow(double x) {
        if (this.value == 0) {
            if (x > 0) {
                return this;
            } else {
                return Infinity.create(1, -x, TYPE);
            }
        } else {
            return new Real<>(Math.pow(this.value, x), TYPE);
        }
    }

    public Constant<T> gcd(Constant<T> c) {
        if (c instanceof Real<T> re) {
            return new Real<>(Utils.gcd(this.value, re.value), TYPE);
        } else if (c instanceof Infinity<T>) {
            return this;
        } else {
            return null;
        }
    }

    public boolean isInteger() {
        return this.value % 1 == 0;
    }
}
