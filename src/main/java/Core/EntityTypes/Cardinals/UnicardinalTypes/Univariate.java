package Core.EntityTypes.Cardinals.UnicardinalTypes;

import Core.AlgeSystem.ExpressionTypes.Symbol;
import Core.EntityTypes.Mutable;

public abstract class Univariate extends Mutable implements Unicardinal {
    public static final int naturalDegreesOfFreedom = 1;

    public Univariate(String n) {
        super(n);
        this.name = n + getVarType();
    }

    public int getNaturalDegreesOfFreedom() {
        return Symbol.naturalDegreesOfFreedom;
    }
}
