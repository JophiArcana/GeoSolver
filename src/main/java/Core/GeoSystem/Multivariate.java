package Core.GeoSystem;

import Core.AlgeSystem.*;
import Core.EntityTypes.Entity;

public interface Multivariate extends Entity {
    String[] getVarTypes();
    String getName();

    Expression expression(String varType);
}
