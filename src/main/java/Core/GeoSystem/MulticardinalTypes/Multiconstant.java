package Core.GeoSystem.MulticardinalTypes;

import Core.EntityTypes.Immutable;

public abstract class Multiconstant extends Immutable implements Multicardinal {
    public String name;

    public Multiconstant(String n) {
        super();
        this.name = n;
    }

    public String getName() {
        return this.name;
    }
}
