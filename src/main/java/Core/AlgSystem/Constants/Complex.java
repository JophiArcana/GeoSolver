package Core.AlgSystem.Constants;

import Core.AlgSystem.UnicardinalRings.DirectedAngle;
import Core.AlgSystem.UnicardinalRings.Symbolic;
import Core.AlgSystem.UnicardinalTypes.*;
import Core.EntityTypes.*;
import Core.Utilities.*;

import java.util.ArrayList;
import java.util.Collections;

public class Complex<T extends Expression<T>> extends Constant<T> {
    public final Number re, im;

    public Complex(Number real, Number imag, Class<T> type) {
        super(type);
        this.re = real;
        this.im = imag;
    }

    public String toString() {
        String reString = (re.doubleValue() == Math.E) ? "E" : re.toString();
        if (im.doubleValue() == 0) {
            return reString + "";
        } else if (re.doubleValue() == 0) {
            if (im.doubleValue() == 1) {
                return "i";
            } else if (im.doubleValue() == -1) {
                return "-i";
            } else {
                return im + "i";
            }
        } else if (im.doubleValue() < 0) {
            if (im.doubleValue() == -1) {
                return "(" + reString + " - i)";
            } else if (im.getClass() == Integer.class) {
                return "(" + reString + " - " + -im.intValue() + "i)";
            } else {
                return "(" + reString + " - " + -im.doubleValue() + "i)";
            }
        } else {
            if (im.doubleValue() == 1) {
                return "(" + reString + " + i)";
            } else {
                return "(" + reString + " + " + im + "i)";
            }
        }
    }

    public ArrayList<Expression<Symbolic>> symbolic() {
        if (this.TYPE == Symbolic.class) {
            return new ArrayList<>(Collections.singletonList((Constant<Symbolic>) this));
        } else if (this.TYPE == DirectedAngle.class) {
            Constant<T> tan = this.tan();
            if (tan instanceof Infinity<T>) {
                return new ArrayList<>(Collections.singletonList(Infinity.create(Symbolic.class)));
            } else {
                Complex<T> cpx = (Complex<T>) tan;
                return new ArrayList<>(Collections.singletonList(new Complex<>(cpx.re, cpx.im, Symbolic.class)));
            }
        } else {
            return null;
        }
    }

    public Constant<T> add(Constant<T> x) {
        if (x instanceof Complex<T> cpx) {
            Number[] set = Utils.integerize(
                    re.doubleValue() + cpx.re.doubleValue(),
                    im.doubleValue() + cpx.im.doubleValue());
            return new Complex<>(set[0], set[1], TYPE);
        } else if (x instanceof Infinity<T> inf) {
            return Infinity.create(ENGINE.add(inf.expression, this), TYPE);
        } else {
            return this;
        }
    }

    public Constant<T> sub(Constant<T> x) {
        if (x instanceof Complex<T> cpx) {
            Number[] set = Utils.integerize(
                    re.doubleValue() - cpx.re.doubleValue(),
                    im.doubleValue() - cpx.im.doubleValue());
            return new Complex<>(set[0], set[1], TYPE);
        } else if (x instanceof Infinity<T> inf) {
            return Infinity.create(ENGINE.sub(this, inf.expression), TYPE);
        } else {
            return this;
        }
    }

    public Constant<T> mul(Constant<T> x) {
        if (x instanceof Complex<T> cpx) {
            Number[] set = Utils.integerize(
                    re.doubleValue() * cpx.re.doubleValue() - im.doubleValue() * cpx.im.doubleValue(),
                    re.doubleValue() * cpx.im.doubleValue() + im.doubleValue() * cpx.re.doubleValue());
            return new Complex<>(set[0], set[1], TYPE);
        } else if (x instanceof Infinity<T> inf) {
            return ENGINE.infinity(ENGINE.mul(this, inf.expression));
        } else {
            return this;
        }
    }

    public Constant<T> div(Constant<T> x) {
        return this.mul(x.inverse());
    }

    public Constant<T> inverse() {
        double k = Math.pow(re.doubleValue(), 2) + Math.pow(im.doubleValue(), 2);
        Number[] set = Utils.integerize(re.doubleValue() / k, -im.doubleValue() / k);
        return new Complex<>(set[0], set[1], TYPE);
    }

    public Constant<T> conjugate() {
        if (im.getClass() == Integer.class) {
            return new Complex<>(re, -im.intValue(), TYPE);
        } else {
            return new Complex<>(re, -im.doubleValue(), TYPE);
        }
    }

    public Constant<T> exp() {
        double k = Math.exp(re.doubleValue());
        Number[] set = Utils.integerize(k * Math.cos(im.doubleValue()), k * Math.sin(im.doubleValue()));
        return new Complex<>(set[0], set[1], TYPE);
    }

    public Constant<T> log() {
        Number[] set = Utils.integerize(Math.log(this.abs()), this.phase());
        return new Complex<>(set[0], set[1], TYPE);
    }

    public Constant<T> pow(Constant<T> x) {
        if (x instanceof Complex) {
            return this.log().mul(x).exp();
        } else if (x instanceof Infinity<T> inf) {
            return ENGINE.infinity(ENGINE.pow(this, inf.expression));
        } else {
            return this;
        }
    }

    public Constant<T> sin() {
        Number[] set = Utils.integerize(Math.sin(re.doubleValue()) * Math.cosh(im.doubleValue()),
                Math.cos(re.doubleValue()) * Math.sinh(im.doubleValue()));
        return new Complex<>(set[0], set[1], TYPE);
    }

    public Constant<T> cos() {
        Number[] set = Utils.integerize(Math.cos(re.doubleValue()) * Math.cosh(im.doubleValue()),
                -Math.sin(re.doubleValue()) * Math.sinh(im.doubleValue()));
        return new Complex<>(set[0], set[1], TYPE);
    }

    public Constant<T> tan() {
        return sin().div(cos());
    }

    public double abs() {
        return Utils.integerize(Math.hypot(re.doubleValue(), im.doubleValue())).doubleValue();
    }

    public double phase() {
        return Utils.integerize(Math.atan2(im.doubleValue(), re.doubleValue())).doubleValue();
    }

    public Constant<T> gcd(Constant<T> c) {
        if (this.equalsOne() && c.equalsOne()) {
            return this;
        } else if (c instanceof Complex<T> cpx) {
            Complex<T> result;
            if (this.gaussianInteger() && cpx.gaussianInteger()) {
                Complex<T> upper = (this.abs() > c.abs()) ? this : cpx;
                Complex<T> lower = (this.abs() > c.abs()) ? cpx : this;
                while (!upper.div(lower).gaussianInteger()) {
                    Complex<T> remainder = (Complex<T>) upper.sub(lower.mul(((Complex<T>) upper.div(lower)).round()));
                    upper = lower;
                    lower = remainder;
                }
                result = lower;
            } else if ((this.div(cpx)).gaussianInteger()) {
                result = cpx;
            } else if (cpx.div(this).gaussianInteger()) {
                result = this;
            } else {
                return Constant.ONE(TYPE);
            }
            if (result.re.doubleValue() > 0) {
                return result;
            } else if (result.re.doubleValue() == 0) {
                return new Complex<>(Math.abs(result.im.doubleValue()), 0, TYPE);
            } else {
                return result.mul(Constant.NONE(TYPE));
            }
        } else {
            return this;
        }
    }

    public boolean gaussianInteger() {
        return this.re instanceof Integer && this.im instanceof Integer;
    }

    public boolean integer() {
        return this.re instanceof Integer && this.im.equals(0);
    }

    public boolean positiveInteger() {
        return this.re instanceof Integer && this.re.intValue() > 0 && this.im.equals(0);
    }

    public Complex<T> round() {
        return new Complex<>((int) Math.round(this.re.doubleValue()), (int) Math.round(this.im.doubleValue()), TYPE);
    }

    public int signum() {
        if (this.re.doubleValue() != 0) {
            return (int) Math.signum(this.re.doubleValue());
        } else {
            return (int) Math.signum(this.im.doubleValue());
        }
    }

    public int compareTo(Immutable immutable) {
        if (immutable instanceof Constant constant && this.TYPE == constant.TYPE) {
            Complex<T> scriptEnt = (Complex<T>) this.sub((Complex<T>) immutable);
            if (scriptEnt.abs() < AlgeEngine.EPSILON) {
                return 0;
            } else {
                if (Math.abs(scriptEnt.re.doubleValue()) > AlgeEngine.EPSILON) {
                    return (int) Math.signum(scriptEnt.re.doubleValue());
                } else {
                    return (int) Math.signum(scriptEnt.im.doubleValue());
                }
            }
        } else {
            return Integer.MIN_VALUE;
        }
    }
}
