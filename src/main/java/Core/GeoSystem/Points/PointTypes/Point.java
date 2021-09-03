package Core.GeoSystem.Points.PointTypes;

import Core.AlgeSystem.UnicardinalRings.*;
import Core.AlgeSystem.UnicardinalTypes.*;
import Core.GeoSystem.MulticardinalTypes.Multicardinal;
import Core.Utilities.Utils;

import java.util.*;

public interface Point extends Multicardinal {
    String[] varTypes = new String[] {Multicardinal.X, Multicardinal.Y};

    default Unicardinal expression(String varType) {
        return switch (Arrays.asList(Point.varTypes).indexOf(varType)) {
            case 0 -> Utils.getEngine(Symbolic.class).real(this.expression().get(0));
            case 1 -> Utils.getEngine(Symbolic.class).imaginary(this.expression().get(0));
            default -> (varType.equals(Multicardinal.VALUE)) ? this.expression().get(0) : null;
        };
    }

    default String[] getVarTypes() {
        return Point.varTypes;
    }
}
