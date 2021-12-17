package Core.GeoSystem.MulticardinalTypes;

import Core.AlgSystem.UnicardinalRings.Symbolic;
import Core.AlgSystem.UnicardinalTypes.Expression;
import Core.EntityTypes.Entity;
import Core.Utilities.Utils;

import java.util.ArrayList;

public interface Multicardinal extends Entity {
    default ArrayList<Expression<Symbolic>> symbolic() {
        return Utils.map(this.expression(), arg -> arg.symbolic().get(0));
    }

    String getName();
}
