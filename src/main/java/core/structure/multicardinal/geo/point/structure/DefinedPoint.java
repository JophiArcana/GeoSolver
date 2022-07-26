package core.structure.multicardinal.geo.point.structure;

import core.Propositions.equalitypivot.EqualityPivot;
import core.structure.multicardinal.DefinedMulticardinal;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;

import java.util.*;

public abstract class DefinedPoint extends DefinedMulticardinal implements Point {
    /** SECTION: Instance Variables ================================================================================= */
    public List<EqualityPivot<SymbolicExpression>> symbolic;

    /** SECTION: Abstract Constructor =============================================================================== */
    public DefinedPoint(String n, boolean anon) {
        super(n, anon);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ===================================================================================== */
    public List<EqualityPivot<SymbolicExpression>> symbolic() {
        return this.symbolic;
    }

    public void deleteSymbolic() {
        this.symbolic = null;
    }

    /** SECTION: Implementation ===================================================================================== */
    public void setNode() {
        this.node = new PointNode(this);
    }
}
