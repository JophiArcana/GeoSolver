package Core.GeoSystem.DirectedAngles;

import Core.AlgSystem.UnicardinalRings.*;
import Core.AlgSystem.UnicardinalTypes.*;
import Core.EntityTypes.Entity;
import Core.GeoSystem.Lines.LineTypes.Line;
import Core.Utilities.*;

import java.util.*;

public class Directed extends DefinedExpression<DirectedAngle> {
    /** SECTION: Static Data ======================================================================================== */
    public enum Parameter implements InputType {
        LINE
    }
    public static final InputType[] inputTypes = {Parameter.LINE};

    /** SECTION: Instance Variables ================================================================================= */
    public Line l;

    /** SECTION: Factory Methods ==================================================================================== */
    public static Directed create(Line l) {
        return new Directed(l);
    }

    public Entity createEntity(HashMap<InputType, ArrayList<Entity>> args) {
        return new Directed((Line) args.get(Parameter.LINE).get(0));
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected Directed(Line l) {
        super(DirectedAngle.class);
        this.l = l;
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public ArrayList<Expression<Symbolic>> symbolic() {
        final AlgEngine<Symbolic> ENGINE = Utils.getEngine(Symbolic.class);
        Expression<Symbolic> dualExpression = this.l.pointDual().symbolic().get(0);
        return new ArrayList<>(Collections.singletonList(ENGINE.div(ENGINE.real(dualExpression), ENGINE.imaginary(dualExpression))));
    }

    public InputType[] getInputTypes() {
        return Directed.inputTypes;
    }

    /** SUBSECTION: Expression ====================================================================================== */
    public Expression<DirectedAngle> close() {
        return this;
    }

    public Factorization<DirectedAngle> normalize() {
        return new Factorization<>(Constant.ONE(DirectedAngle.class), SingletonMap.of(this, Constant.ONE(DirectedAngle.class)), DirectedAngle.class);
    }

    public Expression<DirectedAngle> derivative(Univariate<DirectedAngle> var) {
        return Constant.ZERO(DirectedAngle.class);
    }
}
