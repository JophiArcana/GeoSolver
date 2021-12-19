package Core.AlgSystem.UnicardinalTypes;

import Core.AlgSystem.UnicardinalRings.*;
import Core.EntityTypes.Entity;

import java.util.*;

public interface Unicardinal extends Entity {
    enum UnicardinalExpressionType implements ExpressionType {
        SYMBOLIC,
        DIRECTED
    }

    HashMap<Class<? extends Expression<?>>, ExpressionType> RINGS = new HashMap<>() {{
        put(Symbolic.class, UnicardinalExpressionType.SYMBOLIC);
        put(DirectedAngle.class, UnicardinalExpressionType.DIRECTED);
    }};
}
