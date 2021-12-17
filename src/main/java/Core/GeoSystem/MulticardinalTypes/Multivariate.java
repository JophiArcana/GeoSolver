package Core.GeoSystem.MulticardinalTypes;

import Core.EntityTypes.Mutable;

public abstract class Multivariate extends Mutable implements Multicardinal {
    public Multivariate(String n) {
        super(n);
    }

    public String getName() {
        return this.name;
    }
}
