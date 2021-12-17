package Core.GeoSystem.Lines.LineTypes;

import Core.AlgeSystem.UnicardinalTypes.Unicardinal;
import Core.GeoSystem.MulticardinalTypes.Multicardinal;

public interface Line extends Multicardinal {
    public enum LineExpressionType implements ExpressionType {
        R, PHI
    }

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
        return LineExpressionType.values().length;
    }
}
