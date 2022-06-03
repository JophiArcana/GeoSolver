package Core.GeoSystem.Points.PointStructure;

import Core.EntityStructure.Immutable;
import Core.EntityStructure.UnicardinalStructure.Unicardinal;
import Core.EntityStructure.MulticardinalStructure.Multicardinal;

public interface Point extends Multicardinal {
    /** SECTION: Static Data ======================================================================================== */
    enum PointExpressionType implements ExpressionType {
        X, Y
    }
    int naturalDegreesOfFreedom = Point.PointExpressionType.values().length;

    static int compareConstants(Immutable c1, Immutable c2) {
        return -Coordinate.compare((Coordinate) c1, (Coordinate) c2);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    default Unicardinal expression(ExpressionType varType) {
        if (varType instanceof PointExpressionType t) {
            return this.symbolic().get(t.ordinal());
        } else {
            return null;
        }
    }

    default int getNaturalDegreesOfFreedom() {
        return Point.naturalDegreesOfFreedom;
    }
}
