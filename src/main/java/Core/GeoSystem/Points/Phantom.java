package Core.GeoSystem.Points;

import Core.AlgeSystem.*;
import Core.GeoSystem.MutableMultivariate;
import Core.Utilities.AlgeEngine;

import java.util.*;

public class Phantom extends MutableMultivariate implements Point {
    public static final int naturalDegreesOfFreedom = 2;

    public Phantom(String n) {
        super(n);
    }

    public ArrayList<Expression> expression() {
        return new ArrayList<>(Collections.singletonList(AlgeEngine.add(this.vars.get(0), AlgeEngine.mul(this.vars.get(1), Constant.I))));
    }

    public int getNaturalDegreesOfFreedom() {
        return Phantom.naturalDegreesOfFreedom;
    }
}
