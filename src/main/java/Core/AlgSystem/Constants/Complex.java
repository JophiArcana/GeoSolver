package Core.AlgSystem.Constants;

import Core.AlgSystem.Operators.Add;
import Core.AlgSystem.Operators.Scale;
import Core.AlgSystem.UnicardinalRings.DirectedAngle;
import Core.AlgSystem.UnicardinalRings.Symbolic;
import Core.AlgSystem.UnicardinalTypes.*;
import Core.Utilities.*;

import java.util.*;

public class Complex<T> extends Constant<T> {
    /** SECTION: Instance Variables ================================================================================= */
    public final double re, im;

    /** SECTION: Factory Methods ==================================================================================== */
    public static <T> Complex<T> create(double real, double imag, Class<T> type) {
        return new Complex<>(real, imag, type);
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected Complex(double real, double imag, Class<T> type) {
        super(type);
        if (Math.abs(real - Math.round(real)) < AlgEngine.EPSILON) {
            this.re = Math.round(real);
        } else {
            this.re = real;
        }
        if (Math.abs(imag - Math.round(imag)) < AlgEngine.EPSILON) {
            this.im = (long) imag;
        } else {
            this.im = imag;
        }
        assert !(Double.isNaN(this.re) || Double.isNaN(this.im)): real + ", " + imag;
    }

    /** SECTION: Print Format ======================================================================================= */
    public String toString() {
        String reString = "" + this.re;
        if (this.im == 0) {
            return reString + "";
        } else if (this.re == 0) {
            if (this.im == 1) {
                return "i";
            } else if (this.im == -1) {
                return "-i";
            } else {
                return this.im + "i";
            }
        } else if (this.im < 0) {
            if (this.im == -1) {
                return "(" + reString + " - i)";
            } else {
                return "(" + reString + " - " + -this.im + "i)";
            }
        } else {
            if (this.im == 1) {
                return "(" + reString + " + i)";
            } else {
                return "(" + reString + " + " + this.im + "i)";
            }
        }
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public ArrayList<Expression<Symbolic>> symbolic() {
        if (this.TYPE == Symbolic.class) {
            return new ArrayList<>(List.of((Constant<Symbolic>) this));
        } else if (this.TYPE == DirectedAngle.class) {
            assert this.im == 0 : "Nonzero imaginary in DirectedAngle expression";
            if (Math.cos(this.re) == 0) {
                return new ArrayList<>(List.of(Infinity.create(Symbolic.class)));
            } else {
                return new ArrayList<>(List.of(Complex.create(Math.tan(this.re), 0, Symbolic.class)));
            }
        } else {
            return null;
        }
    }

    /** SUBSECTION: Immutable ======================================================================================= */
    public boolean equalsZero() {
        return this.re == 0 && this.im == 0;
    }

    public boolean equalsOne() {
        return this.re == 1 && this.im == 0;
    }

    /** SECTION: Basic Operations =================================================================================== */
    public Constant<T> add(Constant<T> x) {
        if (x instanceof Complex<T> cpx) {
            return new Complex<>(this.re + cpx.re, this.im + cpx.im, TYPE);
        } else {
            if (((Infinity<T>) x).degree > 0) {
                return x;
            } else {
                return this;
            }
        }
    }

    public Constant<T> sub(Constant<T> x) {
        if (x instanceof Complex<T> cpx) {
            return new Complex<>(this.re - cpx.re, this.im - cpx.im, TYPE);
        } else {
            if (((Infinity<T>) x).degree > 0) {
                return x.negate();
            } else {
                return this;
            }
        }
    }

    public Constant<T> mul(Constant<T> x) {
        if (x instanceof Complex<T> cpx) {
            return new Complex<>(this.re * cpx.re - this.im * cpx.im, this.re * cpx.im + this.im * cpx.re, TYPE);
        } else if (x instanceof Infinity<T> inf) {
            return Infinity.create((Complex<T>) this.mul(inf.coefficient), inf.degree, TYPE);
        } else {
            return this;
        }
    }

    public Constant<T> div(Constant<T> x) {
        return this.mul(x.inverse());
    }

    public Constant<T> negate() {
        return new Complex<>(-this.re, -this.im, TYPE);
    }

    public Constant<T> inverse() {
        double k = this.re * this.re + this.im * this.im;
        return new Complex<>(this.re / k, -this.im / k, TYPE);
    }

    public Constant<T> conjugate() {
        return new Complex<>(this.re, -this.im, TYPE);
    }

    public Complex<T> exp() {
        double k = Math.exp(this.re);
        return new Complex<>(k * Math.cos(this.im), k * Math.sin(this.im), TYPE);
    }

    public Constant<T> log() {
        return new Complex<>(Math.log(this.abs()), Math.atan2(this.im, this.re), TYPE);
    }

    public Constant<T> pow(double x) {
        if (this.equalsZero()) {
            if (x > 0) {
                return this;
            } else {
                return Infinity.create(Constant.ONE(TYPE), -x, TYPE);
            }
        } else {
            return ((Complex<T>) this.log().mul(Complex.create(x, 0, TYPE))).exp();
        }
    }

    public double abs() {
        return Math.hypot(this.re, this.im);
    }

    public Constant<T> gcd(Constant<T> c) {
        if (this.equalsOne() && c.equalsOne()) {
            return this;
        } else if (c instanceof Complex<T> cpx) {
            Complex<T> result;
            if (this.isGaussianInteger() && cpx.isGaussianInteger()) {
                Complex<T> upper = (this.abs() > c.abs()) ? this : cpx;
                Complex<T> lower = (this.abs() > c.abs()) ? cpx : this;
                while (!upper.div(lower).isGaussianInteger()) {
                    Complex<T> remainder = (Complex<T>) upper.sub(lower.mul(((Complex<T>) upper.div(lower)).round()));
                    upper = lower;
                    lower = remainder;
                }
                result = lower;
            } else {
                return Constant.ONE(TYPE);
            }
            if (result.re > 0) {
                return result;
            } else if (result.re == 0) {
                return new Complex<>(Math.abs(result.im), 0, TYPE);
            } else {
                return result.mul(Constant.NONE(TYPE));
            }
        } else {
            return this;
        }
    }

    public boolean isGaussianInteger() {
        return this.re % 1 == 0 && this.im % 1 == 0;
    }

    public boolean isInteger() {
        return this.re % 1 == 0 && this.im == 0;
    }

    public Complex<T> round() {
        return new Complex<>((int) Math.round(this.re), (int) Math.round(this.im), TYPE);
    }
}
