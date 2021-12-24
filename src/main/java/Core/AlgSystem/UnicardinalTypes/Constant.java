package Core.AlgSystem.UnicardinalTypes;

import Core.AlgSystem.Constants.*;
import Core.EntityTypes.*;
import Core.Utilities.*;

import java.util.*;

public abstract class Constant<T extends Expression<T>> extends Immutable implements Expression<T> {
    public enum Parameter implements InputType {
        VAlUE
    }
    public static final InputType[] inputTypes = {Parameter.VAlUE};

    /** SECTION: Instance Variables ================================================================================= */

    public final Class<T> TYPE;
    public final AlgEngine<T> ENGINE;

    public Constant(Class<T> type) {
        super();
        this.TYPE = type;
        this.ENGINE = Utils.getEngine(TYPE);
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
        return Complex.create(0, 0, TYPE);
    }

    public InputType[] getInputTypes() {
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

    public abstract boolean isGaussianInteger();
    public abstract boolean isInteger();
    public abstract boolean isPositiveInteger();

    public Class<T> getType() {
        return this.TYPE;
    }

    public AlgEngine<T> getEngine() {
        return this.ENGINE;
    }
}
