package Core.GeoSystem.Points;

import Core.EntityTypes.Entity;

public interface Point extends Entity {
    default Entity simplify() {
        return this;
    }
}
