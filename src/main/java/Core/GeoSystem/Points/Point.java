package Core.GeoSystem.Points;

import Core.AlgeSystem.*;
import Core.GeoSystem.Multivariate;
import Core.Utilities.AlgeEngine;

import java.util.Arrays;

public interface Point extends Multivariate {
    String[] varTypes = new String[] {"\u1D6A", "\u1D67"};

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
