package Core.GeoSystem.Points.PointTypes;

import Core.AlgeSystem.ExpressionTypes.Constant;
import Core.AlgeSystem.ExpressionTypes.Expression;
import Core.AlgeSystem.ExpressionTypes.Symbol;
import Core.EntityTypes.Cardinals.MulticardinalTypes.Multivariate;
import Core.Utilities.AlgeEngine;

import java.util.*;

public class Phantom extends Multivariate implements Point {
    public static final int naturalDegreesOfFreedom = 2;

    public Symbol var_x, var_y;

    public Phantom(String n) {
        super(n);
        this.var_x = new Symbol(this.name + Point.varTypes[0]);
        this.var_y = new Symbol(this.name + Point.varTypes[1]);
    }

    public ArrayList<Expression> expression() {
        return new ArrayList<>(Collections.singletonList(AlgeEngine.add(this.var_x, AlgeEngine.mul(this.var_y, Constant.I))));
    }

    public int getNaturalDegreesOfFreedom() {
        return Phantom.naturalDegreesOfFreedom;
    }
}
