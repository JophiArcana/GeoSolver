package core.structure.multicardinal.geo.point.structure;

import core.Diagram;
import core.structure.multicardinal.DefinedMulticardinal;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;

import java.util.*;

public abstract class DefinedPoint extends DefinedMulticardinal implements Point {
    /** SECTION: Instance Variables ================================================================================= */
    public List<SymbolicExpression> symbolic;

    /** SECTION: Abstract Constructor =============================================================================== */
    public DefinedPoint(Diagram d, String n) {
        super(d, n);
    }

    public DefinedPoint(Diagram d, String n, boolean anon) {
        super(d, n, anon);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ===================================================================================== */
    public List<SymbolicExpression> symbolic() {
        return this.symbolic;
    }

    /** SECTION: Implementation ===================================================================================== */
    public void setNode() {
        this.node = new PointNode(this);
        this.diagram.root.getChildren().add(this.node);
    }
}
