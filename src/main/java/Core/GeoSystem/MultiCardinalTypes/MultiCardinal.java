package Core.GeoSystem.MultiCardinalTypes;

import Core.AlgeSystem.ExpressionTypes.Expression;
import Core.EntityTypes.Entity;

public interface MultiCardinal extends Entity {
    String X = "\u1D6A";
    String Y = "\u1D67";
    String R = "\u1D63";
    String PHI = "\u1D69";

    String[] getVarTypes();

    Expression expression(String varType);
}
