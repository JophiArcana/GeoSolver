package Core.AlgeSystem.UnicardinalTypes;

import Core.AlgeSystem.UnicardinalRings.*;
import Core.EntityTypes.Entity;

import java.util.HashMap;

public interface Unicardinal extends Entity {
    HashMap<Class<? extends Expression<?>>, String> RINGS = new HashMap<>() {{
        put(Symbolic.class, "\u2C7D");
        put(DirectedAngle.class, "\u1D40");
    }};

    default Unicardinal expression(String varType) {
        return (varType.equals(this.getVarType())) ? this : null;
    }

    String getVarType();
}
