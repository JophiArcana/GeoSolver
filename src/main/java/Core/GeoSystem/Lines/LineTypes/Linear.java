package Core.GeoSystem.Lines.LineTypes;

import Core.GeoSystem.MulticardinalTypes.Multivariate;
import Core.GeoSystem.Points.PointTypes.*;

public class Linear extends Multivariate implements Line {
    public static final int naturalDegreesOfFreedom = 2;

    public Phantom pointDual;

    public Linear(String n) {
        super(n);
        this.pointDual = new Phantom(n + "\u209A");
    }

    public Point pointDual() {
        return this.pointDual;
    }

    public int getNaturalDegreesOfFreedom() {
        return Linear.naturalDegreesOfFreedom;
    }
}
