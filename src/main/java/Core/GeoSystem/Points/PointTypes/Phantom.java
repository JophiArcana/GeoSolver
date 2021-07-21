package Core.GeoSystem.Points.PointTypes;

import Core.AlgeSystem.UnicardinalTypes.*;
import Core.AlgeSystem.UnicardinalRings.Distance;
import Core.GeoSystem.MulticardinalTypes.Multivariate;
import Core.Utilities.AlgeEngine;
import Core.Utilities.Utils;

import java.util.*;

public class Phantom extends Multivariate implements Point {
    public static final int naturalDegreesOfFreedom = 2;

    public Univariate<Distance> var_x, var_y;

    public Phantom(String n) {
        super(n);
        this.var_x = new Univariate<>(this.name + Point.varTypes[0], Distance.class);
        this.var_y = new Univariate<>(this.name + Point.varTypes[1], Distance.class);
    }

    public ArrayList<Unicardinal> expression() {
        AlgeEngine<Distance> ENGINE = Utils.getEngine(Distance.class);
        return new ArrayList<>(Collections.singletonList(ENGINE.add(this.var_x, ENGINE.mul(this.var_y, Constant.I(Distance.class)))));
    }

    public int getNaturalDegreesOfFreedom() {
        return Phantom.naturalDegreesOfFreedom;
    }
}
