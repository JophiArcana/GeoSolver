package Core.GeoSystem.Points.PointTypes;

import Core.AlgeSystem.UnicardinalTypes.*;
import Core.EntityTypes.*;
import Core.AlgeSystem.UnicardinalRings.Distance;

import java.util.*;

public class Coordinate extends Immutable implements Point {
    public static final String[] inputTypes = new String[] {"Value"};

    public Constant<Distance> value;

    public Coordinate(Constant<Distance> v) {
        super();
        this.value = v;
        inputs.get("Value").add(this.value);
    }

    public ArrayList<Unicardinal> expression() {
        return new ArrayList<>(Collections.singletonList(this.value));
    }

    public String[] getInputTypes() {
        return Coordinate.inputTypes;
    }

    public int compareTo(Entity ent) {
        if (ent == null || this.getClass() != ent.getClass()) {
            return Integer.MIN_VALUE;
        } else {
            return this.value.compareTo(((Coordinate) ent).value);
        }
    }
}
