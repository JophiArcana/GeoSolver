package Core.GeoSystem.Points.PointTypes;

import Core.AlgeSystem.ExpressionTypes.Constant;
import Core.AlgeSystem.ExpressionTypes.Expression;
import Core.AlgeSystem.ExpressionTypes.Univariate;
import Core.GeoSystem.MulticardinalTypes.Multivariate;
import Core.Utilities.AlgeEngine;

import java.util.*;

public class Phantom extends Multivariate implements Point {
    public static final int naturalDegreesOfFreedom = 2;

    public Univariate var_x, var_y;

    public Phantom(String n) {
        super(n);
        this.var_x = this.vars.get(0);
        this.var_y = this.vars.get(1);
    }

    public ArrayList<Expression> expression() {
        return new ArrayList<>(Collections.singletonList(AlgeEngine.add(this.vars.get(0), AlgeEngine.mul(this.vars.get(1), Constant.I))));
    }

    public int getNaturalDegreesOfFreedom() {
        return Phantom.naturalDegreesOfFreedom;
    }
}
