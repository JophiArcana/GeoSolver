package Core.GeoSystem.MulticardinalTypes;

import Core.AlgeSystem.UnicardinalRings.Symbolic;
import Core.AlgeSystem.UnicardinalTypes.Expression;
import Core.EntityTypes.Entity;
import Core.Utilities.Utils;

import java.util.ArrayList;

public interface Multicardinal extends Entity {
    String VALUE = "V";
    String X = "\u1D6A";
    String Y = "\u1D67";
    String R = "\u1D63";
    String PHI = "\u1D69";

    String[] getVarTypes();

    default ArrayList<Expression<Symbolic>> symbolic() {
        return Utils.map(this.expression(), arg -> arg.symbolic().get(0));
    }

    String getName();
}
