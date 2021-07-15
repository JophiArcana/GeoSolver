package Core.GeoSystem.Angles;

import Core.AlgeSystem.ExpressionTypes.*;
import Core.EntityTypes.Cardinals.UnicardinalTypes.Univariate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Direction extends Univariate implements DirectedAngle {
    public Symbol var;

    public Direction(String n) {
        super(n);
        this.var = new Symbol(this.name);
    }

    public ArrayList<Expression> expression() {
        return new ArrayList<>(Collections.singletonList(this.var));
    }

    public Expression expression(String varType) {
        return (varType.equals(DirectedAngle.varType)) ? this.var : null;
    }
}
