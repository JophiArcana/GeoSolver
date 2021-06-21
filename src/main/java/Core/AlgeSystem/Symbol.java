package Core.AlgeSystem;

import Core.EntityTypes.Mutable;
import Core.Utilities.Utils;

import java.util.TreeMap;

public class Symbol extends Mutable implements Expression {
    public static final int naturalDegreesOfFreedom = 1;

    public Symbol(String n) {
        super();
        this.name = n;
    }

    public Factorization normalize() {
        TreeMap<Expression, Expression> factors = new TreeMap<>(Utils.PRIORITY_COMPARATOR);
        factors.put(this, Constant.ONE);
        return new Factorization(Constant.ONE, factors);
    }

    public Expression derivative(Symbol s) {
        if (this.name.equals(s.name)) {
            return Constant.ONE;
        } else {
            return Constant.ZERO;
        }
    }

    public int getNaturalDegreesOfFreedom() {
        return Symbol.naturalDegreesOfFreedom;
    }
}
