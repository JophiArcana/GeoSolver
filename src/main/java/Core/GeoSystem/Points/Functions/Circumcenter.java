package Core.GeoSystem.Points.Functions;

import Core.AlgSystem.Constants.Complex;
import Core.AlgSystem.Operators.Mul;
import Core.AlgSystem.Operators.Scale;
import Core.AlgSystem.UnicardinalRings.*;
import Core.AlgSystem.UnicardinalTypes.*;
import Core.EntityTypes.Entity;
import Core.GeoSystem.Points.PointTypes.*;
import Core.Utilities.*;

import java.util.*;
import java.util.function.Function;

public class Circumcenter extends Center {
    /** SECTION: Static Data ============================================================================= */
    private static Expression<Symbolic> funcN(ArrayList<Expression<Symbolic>> args) {
        final AlgEngine<Symbolic> ENGINE = Utils.getEngine(Symbolic.class);
        return Mul.create(List.of(ENGINE.norm2(args.get(0)), ENGINE.sub(args.get(1), args.get(2))), Symbolic.class);
    }
    private static Expression<Symbolic> funcD(ArrayList<Expression<Symbolic>> args) {
        final AlgEngine<Symbolic> ENGINE = Utils.getEngine(Symbolic.class);
        return ENGINE.mul(ENGINE.real(args.get(0)), ENGINE.sub(ENGINE.imaginary(args.get(1)), ENGINE.imaginary(args.get(2))));
    }

    private static ArrayList<Expression<Symbolic>> formula(HashMap<InputType, ArrayList<ArrayList<Expression<Symbolic>>>> args) {
        final AlgEngine<Symbolic> ENGINE = Utils.getEngine(Symbolic.class);
        ArrayList<Expression<Symbolic>> terms = Utils.map(args.get(Parameter.POINTS), arg -> arg.get(0));
        Expression<Symbolic> numerator = ENGINE.cyclicSum(Circumcenter::funcN, terms);
        Expression<Symbolic> denominator = ENGINE.cyclicSum(Circumcenter::funcD, terms);
        return new ArrayList<>(List.of(
                Scale.create(Complex.create(0, -0.5, Symbolic.class), ENGINE.div(numerator, denominator), Symbolic.class)));
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
