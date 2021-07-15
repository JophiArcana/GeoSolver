package Core.GeoSystem.Lines;

import Core.AlgeSystem.ExpressionTypes.Expression;
import Core.EntityTypes.Cardinals.MulticardinalTypes.Multicardinal;

import java.util.Arrays;

public interface Line extends Multicardinal {
    String[] varTypes = new String[] {Multicardinal.R, Multicardinal.PHI};

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
