package Core.GeoSystem.Lines.LineTypes;

import Core.EntityTypes.*;
import Core.GeoSystem.MulticardinalTypes.Multiconstant;
import Core.GeoSystem.Points.PointTypes.Coordinate;
import Core.GeoSystem.Points.PointTypes.Point;

public class Axis extends Multiconstant implements Line {
    public enum Parameter implements InputType {
        COORDINATE
    }
    public static final InputType[] inputTypes = {Parameter.COORDINATE};

    public final Coordinate pointDual;

    public Axis(String n, Coordinate c) {
        super(n);
        this.pointDual = c;
        this.inputs.get(Parameter.COORDINATE).add(this.pointDual);
    }

    public Point pointDual() {
        return this.pointDual;
    }

    public InputType[] getInputTypes() {
        return Axis.inputTypes;
    }

    public int compareTo(Immutable immutable) {
        if (immutable instanceof Axis axis) {
            return this.pointDual.compareTo(axis.pointDual);
        } else {
            return Integer.MIN_VALUE;
        }
    }
}
