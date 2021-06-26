package Core.GeoSystem.Lines;

import Core.AlgeSystem.*;
import Core.GeoSystem.Multivariate;
import Core.GeoSystem.Points.Point;

import java.util.Arrays;

public interface Line extends Multivariate {
    String[] varTypes = new String[] {"\u1D63", "\u1D69"};

    default Expression expression(String varType) {
        return switch (Arrays.asList(Point.varTypes).indexOf(varType)) {
            case 0 -> this.expression().get(0);
            case 1 -> this.expression().get(1);
            default -> null;
        };
    }

    default String[] getVarTypes() {
        return Line.varTypes;
    }
}
