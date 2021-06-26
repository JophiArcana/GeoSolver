package Core.GeoSystem;

import Core.AlgeSystem.*;
import Core.EntityTypes.Entity;

public interface Multivariate extends Entity {
    String[] getVarTypes();

    Expression expression(String varType);
}
