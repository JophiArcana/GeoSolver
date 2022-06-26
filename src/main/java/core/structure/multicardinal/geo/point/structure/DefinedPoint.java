package core.structure.multicardinal.geo.point.structure;

import core.structure.multicardinal.DefinedMulticardinal;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;

import java.util.*;

public abstract class DefinedPoint extends DefinedMulticardinal implements Point {
    /** SECTION: Instance Variables ================================================================================= */
    public List<SymbolicExpression> symbolic;

    /** SECTION: Abstract Constructor =============================================================================== */
    public DefinedPoint(String n) {
        super(n);
    }

    public DefinedPoint(String n, boolean anon) {
        super(n, anon);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ===================================================================================== */
    public List<SymbolicExpression> symbolic() {
        return this.symbolic;
    }

    /** SECTION: Implementation ===================================================================================== */
    public void setNode() {
        this.node = new PointNode(this);
    }
}
