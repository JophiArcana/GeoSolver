package Core.GeoSystem.Lines.LineStructure;

import Core.AlgSystem.UnicardinalRings.Symbolic;
import Core.EntityStructure.*;
import Core.EntityStructure.UnicardinalStructure.*;
import Core.GeoSystem.DirectedAngles.Directed;
import Core.EntityStructure.MulticardinalStructure.*;
import Core.GeoSystem.Points.PointStructure.Point;

import java.util.*;

public interface Line extends Locus {
    /** SECTION: Static Data ======================================================================================== */
    enum LineExpressionType implements ExpressionType {
        R, PHI
    }
    int naturalDegreesOfFreedom = LineExpressionType.values().length;

    static int compareConstants(Immutable c1, Immutable c2) {
        return -Axis.compare((Axis) c1, (Axis) c2);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    default Unicardinal expression(ExpressionType varType) {
        if (varType == LineExpressionType.PHI) {
            return Directed.create(this);
        } else {
            return null;
        }
    }

    default ArrayList<Expression<Symbolic>> symbolic() {
        return this.pointDual().symbolic();
    }

    default int getNaturalDegreesOfFreedom() {
        return Line.naturalDegreesOfFreedom;
    }

    /** SECTION: Interface ========================================================================================== */
    Point pointDual();
}
