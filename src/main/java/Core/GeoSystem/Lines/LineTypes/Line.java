package Core.GeoSystem.Lines.LineTypes;

import Core.AlgSystem.UnicardinalTypes.Unicardinal;
import Core.GeoSystem.MulticardinalTypes.Multicardinal;

public interface Line extends Multicardinal {
    enum LineExpressionType implements ExpressionType {
        R, PHI
    }
    int naturalDegreesOfFreedom = LineExpressionType.values().length;

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

    default int getNaturalDegreesOfFreedom() {
        return Line.naturalDegreesOfFreedom;
    }
}
