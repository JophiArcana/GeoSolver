package Core.GeoSystem.Lines.LineTypes;

import Core.AlgeSystem.UnicardinalTypes.*;
import Core.AlgeSystem.UnicardinalRings.*;
import Core.GeoSystem.MulticardinalTypes.Multivariate;

import java.util.*;

public class Linear extends Multivariate implements Line {
    public static final int naturalDegreesOfFreedom = 2;

    public Univariate<Symbolic> var_r;
    public Univariate<DirectedAngle> var_phi;

    public Linear(String n) {
        super(n);
        this.var_r = new Univariate<>(this.name + "\u1D63", Symbolic.class);
        this.var_phi = new Univariate<>(this.name + "\u1D69", DirectedAngle.class);
    }

    public ArrayList<Unicardinal> expression() {
        return new ArrayList<>(Arrays.asList(this.var_r, this.var_phi));
    }

    public int getNaturalDegreesOfFreedom() {
        return Linear.naturalDegreesOfFreedom;
    }
}
