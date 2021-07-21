package Core.AlgeSystem.UnicardinalTypes;

import Core.AlgeSystem.UnicardinalRings.*;
import Core.EntityTypes.Entity;

import java.util.HashMap;

public interface Unicardinal extends Entity {
    HashMap<Class<? extends Expression<?>>, String> RINGS = new HashMap<>() {{
        put(Symbolic.class, "\u2C7D");
        put(Distance.class, "\u1D30");
        put(DirectedAngle.class, "\u1D40");
    }};

    default Unicardinal expression(String varType) {
        if (varType.equals(this.getVarType())) {
            return this;
        } else {
            return null;
        }
    }

    String getVarType();
}
