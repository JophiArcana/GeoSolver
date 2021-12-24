package Core.GeoSystem.Points.PointTypes;

import Core.AlgSystem.UnicardinalRings.Symbolic;
import Core.AlgSystem.UnicardinalTypes.Expression;
import Core.GeoSystem.MulticardinalTypes.DefinedMulticardinal;

import java.util.ArrayList;

public abstract class DefinedPoint extends DefinedMulticardinal implements Point {
    /** SECTION: Instance Variables ================================================================================= */
    public ArrayList<Expression<Symbolic>> symbolic;

    /** SECTION: Abstract Constructor =============================================================================== */
    public DefinedPoint(String n) {
        super(n);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public ArrayList<Expression<Symbolic>> symbolic() {
        if (this.symbolic == null) {
            this.symbolic = this.computeSymbolic();
        }
        return this.symbolic;
    }

    /** SECTION: Interface ========================================================================================== */
    protected abstract ArrayList<Expression<Symbolic>> computeSymbolic();
}
