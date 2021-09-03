package Core.GeoSystem.Lines.LineTypes;

import Core.AlgeSystem.UnicardinalRings.*;
import Core.AlgeSystem.UnicardinalTypes.*;
import Core.EntityTypes.*;
import Core.GeoSystem.MulticardinalTypes.Multiconstant;

import java.util.ArrayList;
import java.util.Arrays;

public class Axis extends Multiconstant implements Line {
    public static final String[] inputTypes = new String[] {"Radius", "Angle"};

    public final Constant<Symbolic> radius;
    public final Constant<DirectedAngle> angle;

    public Axis(String n, Constant<Symbolic> r, Constant<DirectedAngle> a) {
        super(n);
        this.radius = r;
        this.angle = a;
        this.inputs.get("Radius").add(this.radius);
        this.inputs.get("Angle").add(this.angle);
    }

    public Axis(Constant<Symbolic> r, Constant<DirectedAngle> a) {
        this("", r, a);
    }

    public ArrayList<Unicardinal> expression() {
        return new ArrayList<>(Arrays.asList(this.radius, this.angle));
    }

    public String[] getInputTypes() {
        return Axis.inputTypes;
    }

    public int compareTo(Immutable immutable) {
        if (immutable instanceof Axis axis) {
            int n = this.radius.compareTo(axis.radius);
            return (n == 0) ? this.angle.compareTo(axis.angle) : n;
        } else {
            return Integer.MIN_VALUE;
        }
    }
}
