package Core.GeoSystem.Lines;

import Core.AlgeSystem.ExpressionTypes.Expression;
import Core.GeoSystem.MulticardinalTypes.Multivariate;

import java.util.*;

public class Linear extends Multivariate implements Line {
    public static final int naturalDegreesOfFreedom = 2;

    public Linear(String n) {
        super(n);
    }

    public ArrayList<Expression> expression() {
        return new ArrayList<>(Arrays.asList(this.vars.get(0), this.vars.get(1)));
    }

    public int getNaturalDegreesOfFreedom() {
        return Linear.naturalDegreesOfFreedom;
    }
}
