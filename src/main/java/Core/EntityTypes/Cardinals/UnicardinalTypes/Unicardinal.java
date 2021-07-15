package Core.EntityTypes.Cardinals.UnicardinalTypes;

import Core.AlgeSystem.ExpressionTypes.Expression;
import Core.EntityTypes.Entity;

public interface Unicardinal extends Entity {
    String V = "";
    String T = "\u209C";

    default Expression expression(String varType) {
        if (varType.equals(this.getVarType())) {
            return this.expression().get(0);
        } else {
            return null;
        }
    }

    String getVarType();
}
