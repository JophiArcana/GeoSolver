package Core.GeoSystem.Points.Functions;

import Core.AlgSystem.Operators.*;
import Core.AlgSystem.UnicardinalRings.Symbolic;
import Core.AlgSystem.UnicardinalTypes.Expression;
import Core.EntityTypes.Entity;
import Core.GeoSystem.Points.PointTypes.*;
import Core.Utilities.Utils;

import java.util.*;
import java.util.function.Function;

public class Centroid extends Center {
    /** SECTION: Static Data ======================================================================================== */
    private static ArrayList<Expression<Symbolic>> formula(HashMap<InputType, ArrayList<ArrayList<Expression<Symbolic>>>> args) {
        ArrayList<Expression<Symbolic>> terms = Utils.map(args.get(Parameter.POINTS), arg -> arg.get(0));
        return new ArrayList<>(List.of(Scale.create(1.0 / terms.size(), Add.create(terms, Symbolic.class), Symbolic.class)));
    }


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
    public Function<HashMap<InputType, ArrayList<ArrayList<Expression<Symbolic>>>>, ArrayList<Expression<Symbolic>>> getFormula() {
        return Centroid::formula;
    }
}
