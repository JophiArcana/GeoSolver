package Core.GeoSystem.Points;

import Core.AlgeSystem.*;
import Core.EntityTypes.*;

import java.util.*;

public class Coordinate extends Immutable implements Point {
    public static final String[] inputTypes = new String[] {"Value"};

    public Constant value;

    public Coordinate(Constant v) {
        super();
        this.value = v;
        inputs.get("Value").add(this.value);
    }

    public ArrayList<Expression> expression() {
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
