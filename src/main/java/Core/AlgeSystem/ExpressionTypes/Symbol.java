package Core.AlgeSystem.ExpressionTypes;

import Core.EntityTypes.Cardinals.UnicardinalTypes.Univariate;
import Core.Utilities.Utils;

import java.util.TreeMap;

public class Symbol extends Univariate implements Expression {

    public Symbol(String n) {
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

    public Expression derivative(Symbol s) {
        if (this.name.equals(s.name)) {
            return Constant.ONE;
        } else {
            return Constant.ZERO;
        }
    }
}
