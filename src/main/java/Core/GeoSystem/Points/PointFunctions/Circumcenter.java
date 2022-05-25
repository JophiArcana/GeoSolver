package Core.GeoSystem.Points.PointFunctions;

import Core.AlgSystem.Operators.AddReduction.Scale;
import Core.AlgSystem.Operators.MulReduction.Pow;
import Core.AlgSystem.UnicardinalRings.*;
import Core.AlgSystem.UnicardinalStructure.*;
import Core.EntityStructure.Entity;
import Core.EntityStructure.UnicardinalStructure.Expression;
import Core.GeoSystem.Points.PointStructure.*;
import Core.Utilities.*;

import java.util.*;
import java.util.function.Function;

public class Circumcenter extends Center {
    /** SECTION: Static Data ============================================================================= */
    private static ArrayList<Expression<Symbolic>> formula(HashMap<InputType, ArrayList<ArrayList<Expression<Symbolic>>>> args) {
        final AlgEngine<Symbolic> ENGINE = Utils.getEngine(Symbolic.class);
        ArrayList<ArrayList<Expression<Symbolic>>> terms = args.get(Parameter.POINTS);
        ArrayList<Expression<Symbolic>> n2Vector = Utils.map(terms, arg -> ENGINE.norm2(arg.get(0), arg.get(1)));

        ArrayList<Expression<Symbolic>> xVector = Utils.map(terms, arg -> arg.get(0));
        ArrayList<Expression<Symbolic>> dyVector = Utils.cyclic(terms, arg -> ENGINE.sub(arg.get(1).get(1), arg.get(2).get(1)));
        ArrayList<Expression<Symbolic>> dxVector = Utils.cyclic(terms, arg -> ENGINE.sub(arg.get(1).get(0), arg.get(2).get(0)));

        Expression<Symbolic> denominator = Pow.create(ENGINE.dot(xVector, dyVector), -1, Symbolic.class);
        return new ArrayList<>(List.of(
                Scale.create(0.5, ENGINE.mul(ENGINE.dot(n2Vector, dyVector), denominator), Symbolic.class),
                Scale.create(-0.5, ENGINE.mul(ENGINE.dot(n2Vector, dxVector), denominator), Symbolic.class)
        ));
    }

    /** SECTION: Factory Methods ==================================================================================== */
    public static Circumcenter create(String n, Point a, Point b, Point c) {
        return new Circumcenter(n, a, b, c);
    }

    public static Circumcenter create(Point a, Point b, Point c) {
        return new Circumcenter("", a, b, c);
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected Circumcenter(String n, Point a, Point b, Point c) {
        super(n, a, b, c);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public Entity simplify() {
        return this;
    }

    /** SUBSECTION: DefinedPoint ==================================================================================== */
    public Function<HashMap<InputType, ArrayList<ArrayList<Expression<Symbolic>>>>, ArrayList<Expression<Symbolic>>> getFormula() {
        return Circumcenter::formula;
    }
}
