package Core.AlgSystem.UnicardinalTypes;

import Core.AlgSystem.Constants.*;
import Core.AlgSystem.UnicardinalRings.*;
import Core.EntityTypes.*;

import java.util.*;

public abstract class Constant<T> extends Immutable implements Expression<T> {
    /** SECTION: Static Data ======================================================================================== */
    public enum Parameter implements InputType {
        VAlUE
    }
    public static final InputType[] inputTypes = {Parameter.VAlUE};

    public static <T> Complex<T> ZERO(Class<T> type) {
        return Complex.create(0, 0, type);
    }

    public static <T> Complex<T> ONE(Class<T> type) {
        return Complex.create(1, 0, type);
    }

    public static <T> Complex<T> NONE(Class<T> type) {
        return Complex.create(-1, 0, type);
    }

    public static <T> Complex<T> I(Class<T> type) {
        return Complex.create(0, 1, type);
    }

    public static <T> Infinity<T> INFINITY(Class<T> type) {
        return Infinity.create(type);
    }

    public static <T> int compare(Constant<T> c1, Constant<T> c2) {
        if (c1 instanceof Infinity<T> inf1 && c2 instanceof Infinity<T> inf2) {
            if (inf1.degree != inf2.degree) {
                return Double.compare(inf1.degree, inf2.degree);
            } else {
                return Constant.compare(inf1.coefficient, inf2.coefficient);
            }
        } else if (c1 instanceof Infinity<T>) {
            return 1;
        } else if (c2 instanceof Infinity<T>) {
            return -1;
        } else {
            Complex<T> cpx1 = (Complex<T>) c1, cpx2 = (Complex<T>) c2;
            if (cpx1.re != cpx2.re) {
                return Double.compare(cpx1.re, cpx2.re);
            } else if (cpx1.im != cpx2.im) {
                return Double.compare(cpx1.im, cpx2.im);
            } else {
                return 0;
            }
        }
    }

    /** SECTION: Instance Variables ================================================================================= */
    public final Class<T> TYPE;

    /** SECTION: Abstract Constructor =============================================================================== */
    public Constant(Class<T> type) {
        super();
        this.TYPE = type;
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public InputType[] getInputTypes() {
        return Constant.inputTypes;
    }

    /** SUBSECTION: Expression ====================================================================================== */
    public Expression<T> reduce() {
        return this;
    }

    public Expression<T> expand() {
        return this;
    }

    public Expression<T> close() {
        return this;
    }

    public int getDegree() {
        return 0;
    }

    public Class<T> getType() {
        return this.TYPE;
    }

    /** SECTION: Basic Operations =================================================================================== */
    public abstract Constant<T> add(Constant<T> x);
    public abstract Constant<T> sub(Constant<T> x);
    public abstract Constant<T> mul(Constant<T> x);
    public abstract Constant<T> div(Constant<T> x);
    public abstract Constant<T> negate();
    public abstract Constant<T> inverse();
    public abstract Constant<T> conjugate();
    public abstract Constant<T> pow(double x);
    public abstract double abs();

    public abstract Constant<T> gcd(Constant<T> c);

    public abstract boolean isGaussianInteger();
    public abstract boolean isInteger();
}
