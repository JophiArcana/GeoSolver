package Core.AlgeSystem.UnicardinalTypes;

import Core.EntityTypes.Mutable;
import Core.Utilities.*;

import java.util.TreeMap;

public class Univariate<T extends Expression<T>> extends Mutable implements Expression<T> {
    public static final int naturalDegreesOfFreedom = 1;

    public final Class<T> TYPE;

    public Univariate(String n, Class<T> type) {
        super(n);
        this.TYPE = type;
    }

    public Expression<T> reduction() {
        return this;
    }

    public Expression<T> expand() {
        return this;
    }

    public Factorization<T> normalize() {
        TreeMap<Expression<T>, Constant<T>> factors = new TreeMap<>(Utils.PRIORITY_COMPARATOR);
        factors.put(this, Constant.ONE(TYPE));
        return new Factorization<>(Constant.ONE(TYPE), factors, TYPE);
    }

    public Expression<T> derivative(Univariate<T> s) {
        if (this.name.equals(s.name)) {
            return Constant.ONE(TYPE);
        } else {
            return Constant.ZERO(TYPE);
        }
    }

    public int getNaturalDegreesOfFreedom() {
        return Univariate.naturalDegreesOfFreedom;
    }

    public Class<T> getType() {
        return this.TYPE;
    }

    public AlgeEngine<T> getEngine() {
        return Utils.getEngine(TYPE);
    }
}
