package Core.GeoSystem.Circles.CircleStructure;

import Core.AlgSystem.UnicardinalRings.Symbolic;
import Core.EntityStructure.MulticardinalStructure.MultiVariable;
import Core.EntityStructure.UnicardinalStructure.Variable;
import Core.GeoSystem.Points.PointStructure.PointVariable;

public class CircleVariable extends MultiVariable implements Circle {
    /** SECTION: Instance Variables ================================================================================= */
    public PointVariable center;
    public Variable<Symbolic> radius;

    /** SECTION: Protected Constructors ============================================================================= */
    protected CircleVariable(String n) {
        super(n);
        this.center = PointVariable.create(n + "\u2092");
        this.radius = Variable.create(n + "\u1D63", Symbolic.class);
    }
}
