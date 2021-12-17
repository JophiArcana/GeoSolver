package Core.GeoSystem.Points.PointTypes;

import Core.AlgeSystem.UnicardinalRings.*;
import Core.AlgeSystem.UnicardinalTypes.*;
import Core.GeoSystem.MulticardinalTypes.Multivariate;
import Core.Utilities.*;

import java.util.*;

public class Phantom extends Multivariate implements Point {
    public Univariate<Symbolic> var_x, var_y;

    public Phantom(String n) {
        super(n);
        this.var_x = new Univariate<>(this.name + "\u1D6A", Symbolic.class);
        this.var_y = new Univariate<>(this.name + "\u1D67", Symbolic.class);
    }

    public ArrayList<Unicardinal> expression() {
        AlgeEngine<Symbolic> ENGINE = Utils.getEngine(Symbolic.class);
        return new ArrayList<>(Collections.singletonList(ENGINE.add(this.var_x, ENGINE.mul(this.var_y, Constant.I(Symbolic.class)))));
    }
}
