package Core.GeoSystem.Circles.CircleStructure;

import Core.EntityStructure.Immutable;
import Core.EntityStructure.MulticardinalStructure.*;
import Core.EntityStructure.UnicardinalStructure.Unicardinal;
import Core.GeoSystem.Points.PointStructure.Coordinate;
import Core.GeoSystem.Points.PointStructure.Point;

public interface Circle extends Locus {
    /** SECTION: Static Data ======================================================================================== */
    enum CircleExpressionType implements ExpressionType {
        X, Y, R
    }
    int naturalDegreesOfFreedom = CircleExpressionType.values().length;

    static int compareConstants(Immutable c1, Immutable c2) {
        return -Disc.compare((Disc) c1, (Disc) c2);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    default Unicardinal expression(ExpressionType varType) {
        if (varType instanceof CircleExpressionType t) {
            return this.symbolic().get(t.ordinal());
        } else {
            return null;
        }
    }

    default int getNaturalDegreesOfFreedom() {
        return Circle.naturalDegreesOfFreedom;
    }

    /** SECTION: Interface ========================================================================================== */
    Point center();
}
