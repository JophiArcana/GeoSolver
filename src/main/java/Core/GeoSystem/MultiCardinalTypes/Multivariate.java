package Core.GeoSystem.MultiCardinalTypes;

import Core.AlgeSystem.ExpressionTypes.Univariate;
import Core.EntityTypes.Mutable;

import java.util.ArrayList;

public abstract class Multivariate extends Mutable implements MultiCardinal {
    public ArrayList<Univariate> vars = new ArrayList<>();

    public Multivariate(String n) {
        super(n);
        for (String varType : this.getVarTypes()) {
            this.vars.add(new Univariate(this.name + varType));
        }
    }

    public int getNaturalDegreesOfFreedom() {
        return this.getVarTypes().length;
    }

    public String getName() {
        return this.name;
    }
}
