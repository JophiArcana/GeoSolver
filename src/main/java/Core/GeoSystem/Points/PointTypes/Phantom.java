package Core.GeoSystem.Points.PointTypes;

import Core.AlgSystem.UnicardinalRings.*;
import Core.AlgSystem.UnicardinalTypes.*;
import Core.GeoSystem.MulticardinalTypes.Multivariate;
import Core.Utilities.*;

import java.util.*;

public class Phantom extends Multivariate implements Point {
    /** SECTION: Instance Variables ================================================================================= */
    public Univariate<Symbolic> var_x, var_y;

    /** SECTION: Factory Methods ==================================================================================== */
    public static Phantom create(String n) {
        return new Phantom(n);
    }

    public static Phantom create() {
        return new Phantom("");
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected Phantom(String n) {
        super(n);
        this.var_x = Univariate.create(this.name + "\u1D6A", Symbolic.class);
        this.var_y = Univariate.create(this.name + "\u1D67", Symbolic.class);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public ArrayList<Expression<Symbolic>> symbolic() {
        AlgEngine<Symbolic> ENGINE = Utils.getEngine(Symbolic.class);
        return new ArrayList<>(Collections.singletonList(ENGINE.add(this.var_x, ENGINE.mul(this.var_y, Constant.I(Symbolic.class)))));
    }
}
