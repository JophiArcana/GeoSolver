package Core.GeoSystem.Circles.CircleStructure;

import Core.AlgSystem.UnicardinalRings.Symbolic;
import Core.EntityStructure.MulticardinalStructure.MultiVariable;
import Core.EntityStructure.UnicardinalStructure.Expression;
import Core.EntityStructure.UnicardinalStructure.Variable;
import Core.GeoSystem.Points.PointStructure.*;

import java.util.ArrayList;
import java.util.List;

public class CircleVariable extends MultiVariable implements Circle {
    /** SECTION: Instance Variables ================================================================================= */
    public PointVariable center;
    public Variable<Symbolic> var_r;

    /** SECTION: Factory Methods ==================================================================================== */
    public static CircleVariable create(String n) {
        return new CircleVariable(n);
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected CircleVariable(String n) {
        super(n);
        this.center = PointVariable.create(n + "\u2092");
        this.var_r = Variable.create(n + "\u1D63", Symbolic.class);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public ArrayList<Expression<Symbolic>> symbolic() {
        return new ArrayList<>(List.of(this.center.var_x, this.center.var_y, this.var_r));
    }

    /** SUBSECTION: Circle ========================================================================================== */
    public Point center() {
        return this.center;
    }
}
