package Core.GeoSystem.Lines.LineTypes;

import Core.AlgSystem.UnicardinalRings.Symbolic;
import Core.AlgSystem.UnicardinalTypes.*;
import Core.GeoSystem.MulticardinalTypes.Multicardinal;
import Core.GeoSystem.Points.PointTypes.Point;

import java.util.*;

public interface Line extends Multicardinal {
    enum LineExpressionType implements ExpressionType {
        R, PHI
    }
    int naturalDegreesOfFreedom = LineExpressionType.values().length;

    Point pointDual();

    default Unicardinal expression(ExpressionType varType) {
        if (varType instanceof LineExpressionType t) {
            return switch (t) {
                case R -> this.expression().get(0);
                case PHI -> this.expression().get(1);
            };
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
}
