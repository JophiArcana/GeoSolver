package Core.GeoSystem.Points.Functions;

import Core.AlgSystem.UnicardinalRings.*;
import Core.AlgSystem.UnicardinalTypes.*;
import Core.EntityTypes.Entity;
import Core.GeoSystem.Points.PointTypes.*;
import Core.Utilities.*;

import java.util.*;

public class Circumcenter extends Center {
    /** SECTION: Static Data ============================================================================= */
    private static Expression<Symbolic> funcN(ArrayList<Expression<Symbolic>> terms) {
        final AlgEngine<Symbolic> ENGINE = Utils.getEngine(Symbolic.class);
        return ENGINE.mul(ENGINE.pow(ENGINE.abs(terms.get(0)), 2), ENGINE.sub(terms.get(1), terms.get(2)));
    }
    private static Expression<Symbolic> funcD(ArrayList<Expression<Symbolic>> terms) {
        final AlgEngine<Symbolic> ENGINE = Utils.getEngine(Symbolic.class);
        return ENGINE.imaginary(ENGINE.mul(ENGINE.conjugate(terms.get(0)), terms.get(1)));
    }

    /** SECTION: Factory Methods ==================================================================================== */
    public static Circumcenter create(String n, Point a, Point b, Point c) {
        return new Circumcenter(n, a, b, c);
    }

    public static Circumcenter create(Point a, Point b, Point c) {
        return new Circumcenter("", a, b, c);
    }

    public Entity createEntity(HashMap<InputType, ArrayList<Entity>> args) {
        ArrayList<Entity> points = args.get(Parameter.POINTS);
        return new Circumcenter(this.name, (Point) points.get(0), (Point) points.get(1), (Point) points.get(2));
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
    protected ArrayList<Expression<Symbolic>> computeSymbolic() {
        final AlgEngine<Symbolic> ENGINE = Utils.getEngine(Symbolic.class);
        ArrayList<Expression<Symbolic>> argTerms = Utils.map(this.inputs.get(Parameter.POINTS), arg -> arg.symbolic().get(0));
        Expression<Symbolic> numerator = ENGINE.cyclicSum(Circumcenter::funcN, argTerms);
        Expression<Symbolic> denominator = ENGINE.mul(2, Constant.I(Symbolic.class), ENGINE.cyclicSum(Circumcenter::funcD, argTerms));
        return new ArrayList<>(Collections.singletonList(ENGINE.div(numerator, denominator)));
    }
}
