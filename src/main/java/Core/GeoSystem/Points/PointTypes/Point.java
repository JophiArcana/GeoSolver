package Core.GeoSystem.Points.PointTypes;

import Core.AlgSystem.UnicardinalRings.*;
import Core.AlgSystem.UnicardinalTypes.*;
import Core.GeoSystem.MulticardinalTypes.Multicardinal;
import Core.Utilities.Utils;

public interface Point extends Multicardinal {
    enum PointExpressionType implements ExpressionType {
        X, Y
    }
    int naturalDegreesOfFreedom = Point.PointExpressionType.values().length;

    default Unicardinal expression(ExpressionType varType) {
        if (varType instanceof PointExpressionType t) {
            return switch (t) {
                case X -> Utils.getEngine(Symbolic.class).real(this.expression().get(0));
                case Y -> Utils.getEngine(Symbolic.class).imaginary(this.expression().get(0));
            };
        } else {
            return null;
        }
    }

    default int getNaturalDegreesOfFreedom() {
        return Point.naturalDegreesOfFreedom;
    }
}
