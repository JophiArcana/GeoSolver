package Core.GeoSystem.Points.PointFunctions;

import Core.AlgSystem.Operators.AddReduction.Add;
import Core.AlgSystem.Operators.AddReduction.Scale;
import Core.AlgSystem.UnicardinalRings.Symbolic;
import Core.EntityStructure.UnicardinalStructure.Expression;
import Core.EntityStructure.Entity;
import Core.GeoSystem.Points.PointStructure.*;

import java.util.*;
import java.util.function.Function;

public class Centroid extends Center {
    /** SECTION: Static Data ======================================================================================== */
    private static ArrayList<Expression<Symbolic>> formula(HashMap<InputType, ArrayList<ArrayList<Expression<Symbolic>>>> args) {
        ArrayList<Expression<Symbolic>> x_terms = new ArrayList<>(), y_terms = new ArrayList<>();
        args.get(Parameter.POINTS).forEach(arg -> {
            x_terms.add(arg.get(0));
            y_terms.add(arg.get(1));
        });
        double k = 1.0 / x_terms.size();
        return new ArrayList<>(List.of(
                Scale.create(k, Add.create(x_terms, Symbolic.class), Symbolic.class),
                Scale.create(k, Add.create(y_terms, Symbolic.class), Symbolic.class)
        ));
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
