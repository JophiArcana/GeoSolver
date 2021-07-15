package Core.EntityTypes.Cardinals.MulticardinalTypes;

import Core.AlgeSystem.ExpressionTypes.Symbol;
import Core.EntityTypes.Cardinals.UnicardinalTypes.Univariate;
import Core.EntityTypes.Mutable;

import java.util.ArrayList;

public abstract class Multivariate extends Mutable implements Multicardinal {

    public Multivariate(String n) {
        super(n);
    }

    public int getNaturalDegreesOfFreedom() {
        return this.getVarTypes().length;
    }
}
