package Core.GeoSystem.Points.PointTypes;

import Core.AlgeSystem.UnicardinalTypes.Unicardinal;
import Core.AlgeSystem.UnicardinalRings.Distance;
import Core.GeoSystem.MulticardinalTypes.Multicardinal;
import Core.Utilities.AlgeEngine;
import Core.Utilities.Utils;

import java.util.Arrays;

public interface Point extends Multicardinal {
    String[] varTypes = new String[] {Multicardinal.X, Multicardinal.Y};

    default Unicardinal expression(String varType) {
        return switch (Arrays.asList(Point.varTypes).indexOf(varType)) {
            case 0 -> Utils.getEngine(Distance.class).real(this.expression().get(0));
            case 1 -> Utils.getEngine(Distance.class).imaginary(this.expression().get(0));
            default -> (varType.equals(Multicardinal.VALUE)) ? this.expression().get(0) : null;
        };
    }

    default String[] getVarTypes() {
        return Point.varTypes;
    }
}
