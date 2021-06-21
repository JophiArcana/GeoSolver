package Core.AlgeSystem;

import Core.EntityTypes.Immutable;
import Core.Utilities.Utils;

import java.util.TreeMap;

public abstract class Constant extends Immutable implements Expression {
    public static final String[] inputTypes = new String[] {"Value"};

    public static final Constant ZERO = new Complex(0, 0);
    public static final Constant ONE = new Complex(1, 0);
    public static final Constant NONE = new Complex(-1, 0);
    public static final Constant I = new Complex(0, 1);
    public static final Constant INFINITY = new Infinity();
    public static final Constant E = new Complex(Math.E, 0);
    public static final Constant PI = new Complex(Math.PI, 0);

    public Factorization normalize() {
        return new Factorization(this, new TreeMap<>(Utils.PRIORITY_COMPARATOR));
    }

    public Expression derivative(Symbol s) {
        return Constant.ZERO;
    }

    public String[] getInputTypes() {
        return Constant.inputTypes;
    }

    public abstract Constant add(Constant x);
    public abstract Constant sub(Constant x);
    public abstract Constant mul(Constant x);
    public abstract Constant div(Constant x);
    public abstract Constant inverse();
    public abstract Constant conjugate();
    public abstract Constant exp();
    public abstract Constant log();
    public abstract Constant pow(Constant x);
    public abstract Constant sin();
    public abstract Constant cos();
    public abstract Constant tan();
    public abstract double abs();
    public abstract double phase();

    public abstract Constant gcd(Constant c);
}
