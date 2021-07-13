package Core.AlgeSystem.Constants;

import Core.AlgeSystem.ExpressionTypes.*;
import Core.EntityTypes.*;
import Core.Utilities.*;

public class Complex extends Constant {
    public final Number re;
    public final Number im;

    public Complex(Number real, Number imag) {
        super();
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

    public Constant add(Constant x) {
        x = (Constant) x.simplify();
        if (x instanceof Complex cpx) {
            Number[] set = Utils.integerize(
                    re.doubleValue() + cpx.re.doubleValue(),
                    im.doubleValue() + cpx.im.doubleValue());
            return new Complex(set[0], set[1]);
        } else if (x instanceof Infinity inf) {
            return new Infinity(AlgeEngine.add(inf.expression, this));
        } else {
            return this;
        }
    }

    public Constant sub(Constant x) {
        x = (Constant) x.simplify();
        if (x instanceof Complex cpx) {
            Number[] set = Utils.integerize(
                    re.doubleValue() - cpx.re.doubleValue(),
                    im.doubleValue() - cpx.im.doubleValue());
            return new Complex(set[0], set[1]);
        } else if (x instanceof Infinity inf) {
            return new Infinity(AlgeEngine.sub(this, inf.expression));
        } else {
            return this;
        }
    }

    public Constant mul(Constant x) {
        x = (Constant) x.simplify();
        if (x instanceof Complex cpx) {
            Number[] set = Utils.integerize(
                    re.doubleValue() * cpx.re.doubleValue() - im.doubleValue() * cpx.im.doubleValue(),
                    re.doubleValue() * cpx.im.doubleValue() + im.doubleValue() * cpx.re.doubleValue());
            return new Complex(set[0], set[1]);
        } else if (x instanceof Infinity inf) {
            return (Constant) (new Infinity(AlgeEngine.mul(this, inf.expression))).simplify();
        } else {
            return this;
        }
    }

    public Constant div(Constant x) {
        x = (Constant) x.simplify();
        return this.mul(x.inverse());
    }

    public Constant inverse() {
        double k = Math.pow(re.doubleValue(), 2) + Math.pow(im.doubleValue(), 2);
        Number[] set = Utils.integerize(re.doubleValue() / k, -im.doubleValue() / k);
        return new Complex(set[0], set[1]);
    }

    public Constant conjugate() {
        if (im.getClass() == Integer.class) {
            return new Complex(re, -im.intValue());
        } else {
            return new Complex(re, -im.doubleValue());
        }
    }

    public Constant exp() {
        double k = Math.exp(re.doubleValue());
        Number[] set = Utils.integerize(k * Math.cos(im.doubleValue()), k * Math.sin(im.doubleValue()));
        return new Complex(set[0], set[1]);
    }

    public Constant log() {
        Number[] set = Utils.integerize(Math.log(this.abs()), this.phase());
        return new Complex(set[0], set[1]);
    }

    public Constant pow(Constant x) {
        x = (Constant) x.simplify();
        if (x instanceof Complex) {
            return this.log().mul(x).exp();
        } else if (x instanceof Infinity inf) {
            return (Constant) (new Infinity(AlgeEngine.pow(this, inf.expression))).simplify();
        } else {
            return this;
        }
    }

    public Constant sin() {
        Number[] set = Utils.integerize(
                Math.sin(re.doubleValue()) * Math.cosh(im.doubleValue()),
                Math.cos(re.doubleValue()) * Math.sinh(im.doubleValue()));
        return new Complex(set[0], set[1]);
    }

    public Constant cos() {
        Number[] set = Utils.integerize(
                Math.cos(re.doubleValue()) * Math.cosh(im.doubleValue()),
                -Math.sin(re.doubleValue()) * Math.sinh(im.doubleValue()));
        return new Complex(set[0], set[1]);
    }

    public Constant tan() {
        return sin().div(cos());
    }

    public double abs() {
        return Utils.integerize(Math.hypot(re.doubleValue(), im.doubleValue())).doubleValue();
    }

    public double phase() {
        return Utils.integerize(Math.atan2(im.doubleValue(), re.doubleValue())).doubleValue();
    }

    public Constant gcd(Constant c) {
        if (c instanceof Complex cpx) {
            if (this.gaussianInteger() && cpx.gaussianInteger()) {
                Complex upper = (this.abs() > c.abs()) ? this : cpx;
                Complex lower = (this.abs() > c.abs()) ? cpx : this;
                while (!((Complex) upper.div(lower)).gaussianInteger()) {
                    Complex remainder = (Complex) upper.sub(lower.mul(((Complex) upper.div(lower)).round()));
                    upper = lower;
                    lower = remainder;
                }
                return (lower.re.doubleValue() < 0) ? (Constant) AlgeEngine.negate(lower) : lower;
            } else if (((Complex) this.div(cpx)).gaussianInteger()) {
                return (cpx.re.doubleValue() < 0) ? (Constant) AlgeEngine.negate(cpx) : cpx;
            } else if (((Complex) cpx.div(this)).gaussianInteger()) {
                return (this.re.doubleValue() < 0) ? (Constant) AlgeEngine.negate(this) : this;
            } else {
                return Constant.ONE;
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

    public Complex round() {
        return new Complex((int) Math.round(this.re.doubleValue()), (int) Math.round(this.im.doubleValue()));
    }

    public int compareTo(Entity ent) {
        if (ent == null || this.getClass() != ent.getClass()) {
            return Integer.MIN_VALUE;
        } else {
            Complex scriptEnt = (Complex) this.sub((Complex) ent);
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
