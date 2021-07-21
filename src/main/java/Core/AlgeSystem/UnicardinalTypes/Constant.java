package Core.AlgeSystem.UnicardinalTypes;

import Core.AlgeSystem.Constants.*;
import Core.EntityTypes.*;
import Core.Utilities.*;

import java.util.TreeMap;

public abstract class Constant<T extends Expression<T>> extends Immutable implements Expression<T> {
    public static final String[] inputTypes = new String[] {"Value"};

    public final Class<T> TYPE;
    public final AlgeEngine<T> ENGINE;

    public Constant(Class<T> type) {
        super();
        this.TYPE = type;
        this.ENGINE = Utils.getEngine(TYPE);
    }

    public static <U extends Expression<U>> Constant<U> ZERO(Class<U> type) {
        return new Complex<>(0, 0, type);
    }
    public static <U extends Expression<U>> Constant<U> ONE(Class<U> type) {
        return new Complex<>(1, 0, type);
    }
    public static <U extends Expression<U>> Constant<U> NONE(Class<U> type) {
        return new Complex<>(-1, 0, type);
    }
    public static <U extends Expression<U>> Constant<U> I(Class<U> type) {
        return new Complex<>(0, 1, type);
    }
    public static <U extends Expression<U>> Constant<U> INFINITY(Class<U> type) {
        return new Infinity<>(type);
    }
    public static <U extends Expression<U>> Constant<U> E(Class<U> type) {
        return new Complex<>(Math.E, 0, type);
    }
    public static <U extends Expression<U>> Constant<U> PI(Class<U> type) {
        return new Complex<>(Math.PI, 0, type);
    }

    public Expression<T> reduction() {
        return this;
    }

    public Expression<T> expand() {
        return this;
    }

    public Factorization<T> normalize() {
        return new Factorization<>(this, new TreeMap<>(Utils.PRIORITY_COMPARATOR), TYPE);
    }

    public Expression<T> derivative(Univariate<T> s) {
        return Constant.ZERO(TYPE);
    }

    public String[] getInputTypes() {
        return Constant.inputTypes;
    }

    public abstract Constant<T> add(Constant<T> x);
    public abstract Constant<T> sub(Constant<T> x);
    public abstract Constant<T> mul(Constant<T> x);
    public abstract Constant<T> div(Constant<T> x);
    public abstract Constant<T> inverse();
    public abstract Constant<T> conjugate();
    public abstract Constant<T> exp();
    public abstract Constant<T> log();
    public abstract Constant<T> pow(Constant<T> x);
    public abstract Constant<T> sin();
    public abstract Constant<T> cos();
    public abstract Constant<T> tan();
    public abstract double abs();
    public abstract double phase();

    public abstract Constant<T> gcd(Constant<T> c);

    public Class<T> getType() {
        return this.TYPE;
    }

    public AlgeEngine<T> getEngine() {
        return this.ENGINE;
    }
}
