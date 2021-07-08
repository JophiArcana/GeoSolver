package Core.GeoSystem;

import Core.AlgeSystem.*;
import Core.EntityTypes.Entity;

public interface Multivariate extends Entity {
    String X = "\u1D6A";
    String Y = "\u1D67";
    String R = "\u1D63";
    String PHI = "\u1D69";

    String[] getVarTypes();

    Expression expression(String varType);
}
