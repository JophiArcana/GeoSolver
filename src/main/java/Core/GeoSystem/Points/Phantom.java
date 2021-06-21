package Core.GeoSystem.Points;

import Core.AlgeSystem.*;
import Core.EntityTypes.Mutable;
import Core.Utilities.ASEngine;

import java.util.*;

public class Phantom extends Mutable implements Point {
    public static final int naturalDegreesOfFreedom = 2;

    public Symbol var_x, var_y;

    public Phantom(String n) {
        super();
        this.name = n;
        this.var_x = new Symbol(n + "\u1D6A");
        this.var_y = new Symbol(n + "\u1D67");
    }

    public ArrayList<Expression> expression() {
        return new ArrayList<>(Collections.singletonList(ASEngine.add(this.var_x, ASEngine.mul(this.var_y, Constant.I))));
    }

    public int getNaturalDegreesOfFreedom() {
        return Phantom.naturalDegreesOfFreedom;
    }
}
