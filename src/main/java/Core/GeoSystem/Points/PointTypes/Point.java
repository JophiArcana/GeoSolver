package Core.GeoSystem.Points.PointTypes;

import Core.AlgeSystem.ExpressionTypes.Expression;
import Core.EntityTypes.Cardinals.MulticardinalTypes.Multicardinal;
import Core.Utilities.AlgeEngine;

import java.util.Arrays;

public interface Point extends Multicardinal {
    String[] varTypes = new String[] {Multicardinal.X, Multicardinal.Y};

    default Expression expression(String varType) {
        return switch (Arrays.asList(Point.varTypes).indexOf(varType)) {
            case 0 -> AlgeEngine.real(this.expression().get(0));
            case 1 -> AlgeEngine.imaginary(this.expression().get(0));
            default -> null;
        };
    }

    default String[] getVarTypes() {
        return Point.varTypes;
    }
}
