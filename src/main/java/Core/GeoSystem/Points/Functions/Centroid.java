package Core.GeoSystem.Points.Functions;

import Core.AlgSystem.Operators.Add;
import Core.AlgSystem.Operators.Scale;
import Core.AlgSystem.UnicardinalRings.*;
import Core.AlgSystem.UnicardinalTypes.*;
import Core.EntityTypes.Entity;
import Core.GeoSystem.Points.PointTypes.*;
import Core.Utilities.*;

import java.util.*;

public class Centroid extends Center {
    /** SECTION: Factory Methods ==================================================================================== */
    public static Centroid create(String n, Point ... args) {
        return (Centroid) new Centroid(n, args).simplify();
    }

    public static Centroid create(Point ... args) {
        return (Centroid) new Centroid("", args).simplify();
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected Centroid(String n, Point ... args) {
        super(n, args);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public Entity simplify() {
        if (this.inputs.get(Parameter.POINTS).size() == 2) {
            TreeSet<Entity> pointSet = new TreeSet<>(this.inputs.get(Parameter.POINTS).elementSet());
            return new Midpoint(this.name, (Point) pointSet.first(), (Point) pointSet.last());
        } else {
            return this;
        }
    }

    /** SUBSECTION: DefinedPoint ==================================================================================== */
    protected ArrayList<Expression<Symbolic>> computeSymbolic() {
        AlgEngine<Symbolic> ENGINE = Utils.getEngine(Symbolic.class);
        ArrayList<Expression<Symbolic>> argTerms = Utils.map(this.inputs.get(Parameter.POINTS), arg -> arg.symbolic().get(0));
        return new ArrayList<>(List.of(Scale.create(1.0 / argTerms.size(), Add.create(argTerms, Symbolic.class), Symbolic.class)));
    }
}
