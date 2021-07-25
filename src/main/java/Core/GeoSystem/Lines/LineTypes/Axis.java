package Core.GeoSystem.Lines.LineTypes;

import Core.AlgeSystem.UnicardinalRings.*;
import Core.AlgeSystem.UnicardinalTypes.*;
import Core.EntityTypes.*;

import java.util.ArrayList;
import java.util.Arrays;

public class Axis extends Immutable implements Line {
    public static final String[] inputTypes = new String[] {"Radius", "Angle"};

    public final Constant<Distance> radius;
    public final Constant<DirectedAngle> angle;

    public Axis(Constant<Distance> r, Constant<DirectedAngle> a) {
        super();
        this.radius = r;
        this.angle = a;
        this.inputs.get("Radius").add(this.radius);
        this.inputs.get("Angle").add(this.angle);
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
