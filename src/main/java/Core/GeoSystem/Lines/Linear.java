package Core.GeoSystem.Lines;

import Core.AlgeSystem.UnicardinalTypes.*;
import Core.AlgeSystem.UnicardinalTypes.Unicardinal;
import Core.AlgeSystem.UnicardinalRings.DirectedAngle;
import Core.AlgeSystem.UnicardinalRings.Distance;
import Core.GeoSystem.MulticardinalTypes.Multivariate;

import java.util.*;

public class Linear extends Multivariate implements Line {
    public static final int naturalDegreesOfFreedom = 2;

    public Univariate<Distance> var_r;
    public Univariate<DirectedAngle> var_phi;

    public Linear(String n) {
        super(n);
        this.var_r = new Univariate<>(this.name + Line.varTypes[0], Distance.class);
        this.var_phi = new Univariate<>(this.name + Line.varTypes[1], DirectedAngle.class);
    }

    public ArrayList<Unicardinal> expression() {
        return new ArrayList<>(Arrays.asList(this.var_r, this.var_phi));
    }

    public int getNaturalDegreesOfFreedom() {
        return Linear.naturalDegreesOfFreedom;
    }
}
