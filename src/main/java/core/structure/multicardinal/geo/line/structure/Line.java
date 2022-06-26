package core.structure.multicardinal.geo.line.structure;

import core.structure.multicardinal.geo.Locus;
import core.structure.multicardinal.geo.point.structure.Point;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;

import java.util.*;

public interface Line extends Locus {
    /** SECTION: Static Data ======================================================================================== */
    int naturalDegreesOfFreedom = 2;

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    default List<SymbolicExpression> symbolic() {
        return this.pointDual().symbolic();
    }

    default int getNaturalDegreesOfFreedom() {
        return Line.naturalDegreesOfFreedom;
    }

    /** SECTION: Interface ========================================================================================== */
    Point pointDual();
}
