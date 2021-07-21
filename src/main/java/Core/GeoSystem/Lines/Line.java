package Core.GeoSystem.Lines;

import Core.AlgeSystem.UnicardinalTypes.Unicardinal;
import Core.GeoSystem.MulticardinalTypes.Multicardinal;

import java.util.Arrays;

public interface Line extends Multicardinal {
    String[] varTypes = new String[] {Multicardinal.R, Multicardinal.PHI};

    default Unicardinal expression(String varType) {
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
