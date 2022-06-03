package Core.GeoSystem.DirectedAngles;

import Core.AlgSystem.UnicardinalRings.*;
import Core.EntityStructure.UnicardinalStructure.*;
import Core.GeoSystem.Lines.LineStructure.Line;
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

    /** SECTION: Protected Constructors ============================================================================= */
    protected Directed(Line l) {
        super(DirectedAngle.class);
        this.l = l;
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public ArrayList<Expression<Symbolic>> symbolic() {
        final AlgEngine<Symbolic> ENGINE = Utils.getEngine(Symbolic.class);
        ArrayList<Expression<Symbolic>> dualExpression = this.l.pointDual().symbolic();
        return new ArrayList<>(Collections.singletonList(ENGINE.negate(ENGINE.div(dualExpression.get(0), dualExpression.get(1)))));
    }

    public InputType[] getInputTypes() {
        return Directed.inputTypes;
    }

    /** SUBSECTION: Expression ====================================================================================== */
    public Expression<DirectedAngle> reduce() {
        return this;
    }

    public Expression<DirectedAngle> expand() {
        return this;
    }

    public Expression<DirectedAngle> close() {
        return this;
    }

    public int getDegree() {
        return 0;
    }
}
