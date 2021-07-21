package Core.AlgeSystem.Constants;

import Core.AlgeSystem.UnicardinalTypes.*;
import Core.EntityTypes.*;
import Core.Utilities.*;

public class Complex<T extends Expression<T>> extends Constant<T> {
    public final Number re;
    public final Number im;

    public Complex(Number real, Number imag, Class<T> type) {
        super(type);
        re = real;
        im = imag;
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

    public Constant<T> add(Constant<T> x) {
        if (x instanceof Complex<T> cpx) {
            Number[] set = Utils.integerize(
                    re.doubleValue() + cpx.re.doubleValue(),
                    im.doubleValue() + cpx.im.doubleValue());
            return new Complex<>(set[0], set[1], TYPE);
        } else if (x instanceof Infinity<T> inf) {
            return new Infinity<>(ENGINE.add(inf.expression, this), TYPE);
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
            return new Infinity<>(ENGINE.sub(this, inf.expression), TYPE);
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
        Number[] set = Utils.integerize(
                Math.sin(re.doubleValue()) * Math.cosh(im.doubleValue()),
                Math.cos(re.doubleValue()) * Math.sinh(im.doubleValue()));
        return new Complex<>(set[0], set[1], TYPE);
    }

    public Constant<T> cos() {
        Number[] set = Utils.integerize(
                Math.cos(re.doubleValue()) * Math.cosh(im.doubleValue()),
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
        if (c instanceof Complex<T> cpx) {
            if (this.gaussianInteger() && cpx.gaussianInteger()) {
                Complex<T> upper = (this.abs() > c.abs()) ? this : cpx;
                Complex<T> lower = (this.abs() > c.abs()) ? cpx : this;
                while (!((Complex<T>) upper.div(lower)).gaussianInteger()) {
                    Complex<T> remainder = (Complex<T>) upper.sub(lower.mul(((Complex<T>) upper.div(lower)).round()));
                    upper = lower;
                    lower = remainder;
                }
                return (lower.re.doubleValue() < 0) ? lower.mul(Constant.NONE(TYPE)) : lower;
            } else if (((Complex<T>) this.div(cpx)).gaussianInteger()) {
                return (cpx.re.doubleValue() < 0) ? cpx.mul(Constant.NONE(TYPE)) : cpx;
            } else if (((Complex<T>) cpx.div(this)).gaussianInteger()) {
                return (this.re.doubleValue() < 0) ? this.mul(Constant.NONE(TYPE)) : this;
            } else {
                return Constant.ONE(TYPE);
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

    public Complex<T> round() {
        return new Complex<>((int) Math.round(this.re.doubleValue()), (int) Math.round(this.im.doubleValue()), TYPE);
    }

    public int compareTo(Entity ent) {
        if (ent == null || this.getClass() != ent.getClass()) {
            return Integer.MIN_VALUE;
        } else {
            Complex<T> scriptEnt = (Complex<T>) this.sub((Complex<T>) ent);
            if (scriptEnt.abs() < AlgeEngine.EPSILON) {
                return 0;
            } else {
                if (Math.abs(scriptEnt.re.doubleValue()) > AlgeEngine.EPSILON) {
                    return (int) Math.signum(scriptEnt.re.doubleValue());
                } else {
                    return (int) Math.signum(scriptEnt.im.doubleValue());
                }
            }
        }
    }
}
