package Core.AlgeSystem.ExpressionTypes;

import Core.EntityTypes.Mutable;
import Core.Utilities.Utils;

import java.util.TreeMap;

public class Univariate extends Mutable implements Expression {
    public static final int naturalDegreesOfFreedom = 1;

    public Univariate(String n) {
        super(n);
    }

    public Expression reduction() {
        return this;
    }

    public Expression expand() {
        return this;
    }

    public Factorization normalize() {
        TreeMap<Expression, Expression> factors = new TreeMap<>(Utils.PRIORITY_COMPARATOR);
        factors.put(this, Constant.ONE);
        return new Factorization(Constant.ONE, factors);
    }

    public Expression derivative(Univariate s) {
        if (this.name.equals(s.name)) {
            return Constant.ONE;
        } else {
            return Constant.ZERO;
        }
    }

    public int getNaturalDegreesOfFreedom() {
        return Univariate.naturalDegreesOfFreedom;
    }
}
