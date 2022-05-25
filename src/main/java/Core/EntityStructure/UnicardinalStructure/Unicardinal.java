package Core.EntityStructure.UnicardinalStructure;

import Core.AlgSystem.UnicardinalRings.*;
import Core.EntityStructure.Entity;

import java.util.*;

public interface Unicardinal extends Entity {
    /** SECTION: Static Data ======================================================================================== */
    enum UnicardinalExpressionType implements ExpressionType {
        SYMBOLIC,
        DIRECTED
    }

    HashMap<Class<?>, ExpressionType> RINGS = new HashMap<>() {{
        put(Symbolic.class, UnicardinalExpressionType.SYMBOLIC);
        put(DirectedAngle.class, UnicardinalExpressionType.DIRECTED);
    }};
}
