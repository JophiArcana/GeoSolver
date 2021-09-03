package Core.GeoSystem.Points.PointTypes;

import Core.AlgeSystem.UnicardinalRings.*;
import Core.AlgeSystem.UnicardinalTypes.*;
import Core.EntityTypes.*;
import Core.GeoSystem.MulticardinalTypes.Multiconstant;

import java.util.*;

public class Coordinate extends Multiconstant implements Point {
    public static final String[] inputTypes = new String[] {"Value"};

    public final Constant<Symbolic> value;

    public Coordinate(String n, Constant<Symbolic> v) {
        super(n);
        this.value = v;
        this.inputs.get("Value").add(this.value);
    }

    public Coordinate(Constant<Symbolic> v) {
        this("", v);
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
