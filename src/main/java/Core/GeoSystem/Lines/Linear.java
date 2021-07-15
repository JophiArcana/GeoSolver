package Core.GeoSystem.Lines;

import Core.AlgeSystem.ExpressionTypes.*;
import Core.EntityTypes.Cardinals.MulticardinalTypes.Multivariate;
import Core.GeoSystem.Angles.Direction;

import java.util.*;

public class Linear extends Multivariate implements Line {
    public static final int naturalDegreesOfFreedom = 2;

    public Symbol var_r;
    public Direction var_phi;

    public Linear(String n) {
        super(n);
        this.var_r = new Symbol(this.name + Line.varTypes[0]);
        this.var_phi = new Direction(this.name + Line.varTypes[1]);
    }

    public ArrayList<Expression> expression() {
        return new ArrayList<>(Arrays.asList(this.var_r, this.var_phi.var));
    }

    public int getNaturalDegreesOfFreedom() {
        return Linear.naturalDegreesOfFreedom;
    }
}
