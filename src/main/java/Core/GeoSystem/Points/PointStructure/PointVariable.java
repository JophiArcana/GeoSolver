package Core.GeoSystem.Points.PointStructure;

import Core.AlgSystem.UnicardinalRings.*;
import Core.EntityStructure.UnicardinalStructure.*;
import Core.EntityStructure.MulticardinalStructure.*;
import Core.Utilities.Utils;

import java.util.*;

public class PointVariable extends MultiVariable implements Point {
    /** SECTION: Instance Variables ================================================================================= */
    public Variable<Symbolic> var_x, var_y;

    /** SECTION: Factory Methods ==================================================================================== */
    public static PointVariable create(String n) {
        return new PointVariable(n);
    }

    public static PointVariable create() {
        return new PointVariable(Utils.randomHash());
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected PointVariable(String n) {
        super(n);
        this.var_x = Variable.create(this.name + "\u1D6A", Symbolic.class);
        this.var_y = Variable.create(this.name + "\u1D67", Symbolic.class);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public ArrayList<Expression<Symbolic>> symbolic() {
        return new ArrayList<>(List.of(this.var_x, this.var_y));
    }
}
