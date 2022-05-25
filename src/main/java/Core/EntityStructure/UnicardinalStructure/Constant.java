package Core.EntityStructure.UnicardinalStructure;

import Core.AlgSystem.Constants.*;
import Core.EntityStructure.*;

public abstract class Constant<T> extends Immutable implements Expression<T> {
    /** SECTION: Static Data ======================================================================================== */
    public enum Parameter implements InputType {
        VAlUE
    }
    public static final InputType[] inputTypes = {Parameter.VAlUE};

    public static <T> Real<T> ZERO(Class<T> type) {
        return new Real<>(0, type);
    }

    public static <T> Real<T> ONE(Class<T> type) {
        return new Real<>(1, type);
    }

    public static <T> Real<T> NONE(Class<T> type) {
        return new Real<>(-1, type);
    }

    public static <T> Infinity<T> INFINITY(Class<T> type) {
        return Infinity.create(type);
    }

    public static <T> int compare(Constant<T> c1, Constant<T> c2) {
        if (c1 instanceof Infinity<T> inf1 && c2 instanceof Infinity<T> inf2) {
            if (inf1.degree != inf2.degree) {
                return Double.compare(inf1.degree, inf2.degree);
            } else {
                return Double.compare(inf1.coefficient, inf2.coefficient);
            }
        } else if (c1 instanceof Infinity<T>) {
            return 1;
        } else if (c2 instanceof Infinity<T>) {
            return -1;
        } else {
            return Double.compare(((Real<T>) c1).value, ((Real<T>) c2).value);
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
    public abstract Constant<T> invert();
    public abstract Constant<T> pow(double x);

    public abstract boolean isInteger();
}
