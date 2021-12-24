package Core.GeoSystem.Lines.LineTypes;

import Core.AlgSystem.UnicardinalRings.Symbolic;
import Core.AlgSystem.UnicardinalTypes.*;
import Core.GeoSystem.DirectedAngles.Directed;
import Core.GeoSystem.MulticardinalTypes.Multicardinal;
import Core.GeoSystem.Points.PointTypes.Point;

import java.util.*;

public interface Line extends Multicardinal {
    /** SECTION: Static Data ======================================================================================== */
    enum LineExpressionType implements ExpressionType {
        R
    }
    int naturalDegreesOfFreedom = LineExpressionType.values().length;

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    default Unicardinal expression(ExpressionType varType) {
        if (varType instanceof LineExpressionType) {
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
