package Core.GeoSystem.Points.PointTypes;

import Core.AlgeSystem.UnicardinalRings.*;
import Core.AlgeSystem.UnicardinalTypes.*;
import Core.GeoSystem.MulticardinalTypes.Multicardinal;
import Core.Utilities.Utils;

public interface Point extends Multicardinal {
    public enum PointExpressionType implements ExpressionType {
        X, Y
    }

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
        return Point.PointExpressionType.values().length;
    }
}
