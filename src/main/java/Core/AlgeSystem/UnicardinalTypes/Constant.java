package Core.AlgeSystem.UnicardinalTypes;

import Core.AlgeSystem.Constants.*;
import Core.AlgeSystem.UnicardinalRings.*;
import Core.EntityTypes.*;
import Core.Utilities.*;

import java.util.*;

public abstract class Constant<T extends Expression<T>> extends Immutable implements Expression<T> {
    public static final String[] inputTypes = new String[] {"Value"};

    public final Class<T> TYPE;
    public final AlgeEngine<T> ENGINE;

    public Constant(Class<T> type) {
        super();
        this.TYPE = type;
        this.ENGINE = Utils.getEngine(TYPE);
    }

    private final static HashMap<String, HashMap<Class<? extends Expression<?>>, Constant<? extends Expression<?>>>> constants = new HashMap<>() {{
        put("ZERO", new HashMap<>() {{
            put(Symbolic.class, new Complex<>(0, 0, Symbolic.class));
            put(DirectedAngle.class, new Complex<>(0, 0, DirectedAngle.class));
        }});
        put("ONE", new HashMap<>() {{
            put(Symbolic.class, new Complex<>(1, 0, Symbolic.class));
            put(DirectedAngle.class, new Complex<>(1, 0, DirectedAngle.class));
        }});
        put("NONE", new HashMap<>() {{
            put(Symbolic.class, new Complex<>(-1, 0, Symbolic.class));
            put(DirectedAngle.class, new Complex<>(-1, 0, DirectedAngle.class));
        }});
        put("I", new HashMap<>() {{
            put(Symbolic.class, new Complex<>(0, 1, Symbolic.class));
            put(DirectedAngle.class, new Complex<>(0, 1, DirectedAngle.class));
        }});
        put("INFINITY", new HashMap<>() {{
            put(Symbolic.class, new Infinity<>(Symbolic.class));
            put(DirectedAngle.class, new Infinity<>(DirectedAngle.class));
        }});
        put("E", new HashMap<>() {{
            put(Symbolic.class, new Complex<>(Math.E, 0, Symbolic.class));
            put(DirectedAngle.class, new Complex<>(Math.E, 0, DirectedAngle.class));
        }});
        put("PI", new HashMap<>() {{
            put(Symbolic.class, new Complex<>(Math.PI, 0, Symbolic.class));
            put(DirectedAngle.class, new Complex<>(Math.PI, 0, DirectedAngle.class));
        }});
    }};

    public static <U extends Expression<U>> Constant<U> ZERO(Class<U> type) {
        return (Constant<U>) Constant.constants.get("ZERO").getOrDefault(type, null);
    }
    public static <U extends Expression<U>> Constant<U> ONE(Class<U> type) {
        return (Constant<U>) Constant.constants.get("ONE").getOrDefault(type, null);
    }
    public static <U extends Expression<U>> Constant<U> NONE(Class<U> type) {
        return (Constant<U>) Constant.constants.get("NONE").getOrDefault(type, null);
    }
    public static <U extends Expression<U>> Constant<U> I(Class<U> type) {
        return (Constant<U>) Constant.constants.get("I").getOrDefault(type, null);
    }
    public static <U extends Expression<U>> Constant<U> INFINITY(Class<U> type) {
        return (Constant<U>) Constant.constants.get("INFINITY").getOrDefault(type, null);
    }
    public static <U extends Expression<U>> Constant<U> E(Class<U> type) {
        return (Constant<U>) Constant.constants.get("E").getOrDefault(type, null);
    }
    public static <U extends Expression<U>> Constant<U> PI(Class<U> type) {
        return (Constant<U>) Constant.constants.get("PI").getOrDefault(type, null);
    }

    public Expression<T> reduce() {
        return this;
    }

    public Expression<T> expand() {
        return this;
    }

    public Expression<T> close() {
        return this;
    }

    public Factorization<T> normalize() {
        return new Factorization<>(this, new TreeMap<>(Utils.PRIORITY_COMPARATOR), TYPE);
    }

    public Expression<T> derivative(Univariate<T> var) {
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
    public abstract Constant<T> pow(Constant<T> x);
    public abstract Constant<T> sin();
    public abstract Constant<T> cos();
    public abstract Constant<T> tan();
    public abstract double abs();
    public abstract double phase();

    public abstract Constant<T> gcd(Constant<T> c);

    public abstract boolean gaussianInteger();
    public abstract boolean integer();
    public abstract boolean positiveInteger();

    public Class<T> getType() {
        return this.TYPE;
    }

    public AlgeEngine<T> getEngine() {
        return this.ENGINE;
    }
}
