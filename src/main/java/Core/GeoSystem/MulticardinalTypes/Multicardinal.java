package Core.GeoSystem.MulticardinalTypes;

import Core.EntityTypes.Entity;

public interface Multicardinal extends Entity {
    String VALUE = "V";
    String X = "\u1D6A";
    String Y = "\u1D67";
    String R = "\u1D63";
    String PHI = "\u1D69";

    String[] getVarTypes();
}
