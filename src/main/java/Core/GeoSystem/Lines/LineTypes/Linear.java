package Core.GeoSystem.Lines.LineTypes;

import Core.GeoSystem.MulticardinalTypes.Multivariate;
import Core.GeoSystem.Points.PointTypes.*;

public class Linear extends Multivariate implements Line {
    /** SECTION: Static Data ======================================================================================== */
    public static final int naturalDegreesOfFreedom = 2;

    /** SECTION: Instance Variables ================================================================================= */
    public Phantom pointDual;

    /** SECTION: Factory Methods ==================================================================================== */
    public static Linear create(String n) {
        return new Linear(n);
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected Linear(String n) {
        super(n);
        this.pointDual = Phantom.create(n + "\u209A");
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public int getNaturalDegreesOfFreedom() {
        return Linear.naturalDegreesOfFreedom;
    }

    /** SUBSECTION: Line ============================================================================================ */
    public Point pointDual() {
        return this.pointDual;
    }
}
