package Core.GeoSystem.Angles;

import Core.EntityTypes.Cardinals.UnicardinalTypes.Unicardinal;

public interface DirectedAngle extends Unicardinal {
    String varType = Unicardinal.T;

    default String getVarType() {
        return DirectedAngle.varType;
    }
}
