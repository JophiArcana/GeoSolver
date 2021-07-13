package Core.GeoSystem.Lines;

import Core.AlgeSystem.ExpressionTypes.Expression;
import Core.GeoSystem.MulticardinalTypes.Multicardinal;

import java.util.Arrays;

public interface Line extends Multicardinal {
    String[] varTypes = new String[] {"\u1D63", "\u1D69"};

    default Expression expression(String varType) {
        return switch (Arrays.asList(Line.varTypes).indexOf(varType)) {
            case 0 -> this.expression().get(0);
            case 1 -> this.expression().get(1);
            default -> null;
        };
    }

    default String[] getVarTypes() {
        return Line.varTypes;
    }
}
