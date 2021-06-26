package Core.GeoSystem;

import Core.AlgeSystem.Univariate;
import Core.EntityTypes.Mutable;

import java.util.ArrayList;

public abstract class MultivariateMutable extends Mutable implements Multivariate {
    public ArrayList<Univariate> vars = new ArrayList<>();
    public String name;

    public MultivariateMutable(String n) {
        this.name = n;
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
