package core.structure.multicardinal.geo.circle.structure;

import core.structure.multicardinal.geo.point.structure.*;
import core.structure.unicardinal.alg.Constant;
import core.structure.multicardinal.MultiConstant;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;
import core.structure.unicardinal.alg.symbolic.constant.SymbolicReal;
import core.util.Utils;

import java.util.*;

public class Disc extends MultiConstant implements Circle {
    /** SECTION: Static Data ======================================================================================== */
    public static final InputType<Coordinate> CENTER = new InputType<>(Coordinate.class, Utils.MULTICARDINAL_COMPARATOR);
    public static final InputType<SymbolicReal> RADIUS = new InputType<>(SymbolicReal.class, Constant::compare);

    public static final List<InputType<?>> inputTypes = List.of(Disc.CENTER, Disc.RADIUS);

    /** SECTION: Instance Variables ================================================================================= */
    public final Coordinate center;
    public final SymbolicReal radius;

    /** SECTION: Factory Methods ==================================================================================== */
    public static Disc create(String n, double x, double y, double r) {
        return new Disc(n, Coordinate.create(n + "\u2092", x, y), SymbolicReal.create(r));
    }

    public static Disc create(String n, double x, double y, double r, boolean anon) {
        return new Disc(n, Coordinate.create(n + "\u2092", x, y), SymbolicReal.create(r), anon);
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected Disc(String n, Coordinate center, SymbolicReal radius) {
        this(n, center, radius, true);
    }

    protected Disc(String n, Coordinate center, SymbolicReal radius, boolean anon) {
        super(n, anon);
        this.center = center;
        this.radius = radius;
        this.getInputs(Disc.CENTER).add(this.center);
        this.getInputs(Disc.RADIUS).add(this.radius);
        if (!this.anonymous) {
            this.node = new CircleNode(this);
        }
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public List<SymbolicExpression> symbolic() {
        return List.of(this.center.x, this.center.y, this.radius);
    }

    public List<InputType<?>> getInputTypes() {
        return Disc.inputTypes;
    }

    /** SUBSECTION: Circle ========================================================================================== */
    public Point center() {
        return this.center;
    }

    public SymbolicExpression radius() {
        return this.radius;
    }
}
