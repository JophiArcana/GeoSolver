package Core.GeoSystem.Points.PointTypes;

import Core.AlgeSystem.UnicardinalTypes.*;
import Core.EntityTypes.*;
import Core.AlgeSystem.UnicardinalRings.Distance;

import java.util.*;

public class Coordinate extends Immutable implements Point {
    public static final String[] inputTypes = new String[] {"Value"};

    public final Constant<Distance> value;

    public Coordinate(Constant<Distance> v) {
        super();
        this.value = v;
        this.inputs.get("Value").add(this.value);
    }

    public ArrayList<Unicardinal> expression() {
        return new ArrayList<>(Collections.singletonList(this.value));
    }

    public String[] getInputTypes() {
        return Coordinate.inputTypes;
    }

    public int compareTo(Immutable immutable) {
        if (immutable instanceof Coordinate coordinate) {
            return this.value.compareTo(coordinate.value);
        } else {
            return Integer.MIN_VALUE;
        }
    }
}
