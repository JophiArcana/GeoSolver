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

    private final static HashMap<String, HashMap<Class<?>, Constant<?>>> constants = new HashMap<>() {{
        put("ZERO", new HashMap<>() {{
            put(Symbolic.class, Complex.create(0, 0, Symbolic.class));
            put(DirectedAngle.class, Complex.create(0, 0, DirectedAngle.class));
        }});
        put("ONE", new HashMap<>() {{
            put(Symbolic.class, Complex.create(1, 0, Symbolic.class));
            put(DirectedAngle.class, Complex.create(1, 0, DirectedAngle.class));
        }});
        put("NONE", new HashMap<>() {{
            put(Symbolic.class, Complex.create(-1, 0, Symbolic.class));
            put(DirectedAngle.class, Complex.create(-1, 0, DirectedAngle.class));
        }});
        put("I", new HashMap<>() {{
            put(Symbolic.class, Complex.create(0, 1, Symbolic.class));
            put(DirectedAngle.class, Complex.create(0, 1, DirectedAngle.class));
        }});
        put("INFINITY", new HashMap<>() {{
            put(Symbolic.class, Infinity.create(Symbolic.class));
            put(DirectedAngle.class, Infinity.create(DirectedAngle.class));
        }});
        put("E", new HashMap<>() {{
            put(Symbolic.class, Complex.create(Math.E, 0, Symbolic.class));
            put(DirectedAngle.class, Complex.create(Math.E, 0, DirectedAngle.class));
        }});
        put("PI", new HashMap<>() {{
            put(Symbolic.class, Complex.create(Math.PI, 0, Symbolic.class));
            put(DirectedAngle.class, Complex.create(Math.PI, 0, DirectedAngle.class));
        }});
    }};

    public static <T> Constant<T> ZERO(Class<T> type) {
        return (Constant<T>) Constant.constants.get("ZERO").getOrDefault(type, null);
    }
    public static <T> Constant<T> ONE(Class<T> type) {
        return (Constant<T>) Constant.constants.get("ONE").getOrDefault(type, null);
    }
    public static <T> Constant<T> NONE(Class<T> type) {
        return (Constant<T>) Constant.constants.get("NONE").getOrDefault(type, null);
    }
    public static <T> Constant<T> I(Class<T> type) {
        return (Constant<T>) Constant.constants.get("I").getOrDefault(type, null);
    }
    public static <T> Constant<T> INFINITY(Class<T> type) {
        return (Constant<T>) Constant.constants.get("INFINITY").getOrDefault(type, null);
    }
    public static <T> Constant<T> E(Class<T> type) {
        return (Constant<T>) Constant.constants.get("E").getOrDefault(type, null);
    }
    public static <T> Constant<T> PI(Class<T> type) {
        return (Constant<T>) Constant.constants.get("PI").getOrDefault(type, null);
    }

    public static <T> int compare(Constant<T> c1, Constant<T> c2) {
        if (c1 instanceof Infinity<T> inf1 && c2 instanceof Infinity<T> inf2) {
            return inf1.expression.compareTo(inf2.expression);
        } else if (c1 instanceof Infinity<T>) {
            return 1;
        } else if (c2 instanceof Infinity<T>) {
            return -1;
        } else {
            Complex<T> cpx1 = (Complex<T>) c1, cpx2 = (Complex<T>) c2;
            if (cpx1.re.doubleValue() != cpx2.re.doubleValue()) {
                return Double.compare(cpx1.re.doubleValue(), cpx2.re.doubleValue());
            } else if (cpx1.im.doubleValue() != cpx2.im.doubleValue()) {
                return Double.compare(cpx1.im.doubleValue(), cpx2.im.doubleValue());
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
    public abstract Constant<T> inverse();
    public abstract Constant<T> conjugate();
    public abstract Constant<T> exp();
    public abstract Constant<T> pow(double x);
    public abstract Constant<T> sin();
    public abstract Constant<T> cos();
    public abstract Constant<T> tan();
    public abstract double abs();
    public abstract double phase();

    public abstract Constant<T> gcd(Constant<T> c);

    public abstract boolean isGaussianInteger();
    public abstract boolean isInteger();
}
