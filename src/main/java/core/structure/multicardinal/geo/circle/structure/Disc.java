package core.structure.multicardinal.geo.circle.structure;

import core.structure.multicardinal.geo.point.structure.*;
import core.structure.multicardinal.MultiConstant;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;
import core.structure.unicardinal.alg.symbolic.SymbolicConstant;

import java.util.*;

public class Disc extends MultiConstant implements Circle {
    /** SECTION: Static Data ======================================================================================== */
    public static final InputType<Coordinate> CENTER = new InputType<>();
    public static final InputType<SymbolicConstant> RADIUS = new InputType<>();

    public static final List<InputType<?>> inputTypes = List.of(Disc.CENTER, Disc.RADIUS);

    /** SECTION: Instance Variables ================================================================================= */
    public final Coordinate center;
    public final SymbolicConstant radius;

    /** SECTION: Factory Methods ==================================================================================== */
    public static Disc create(String n, double x, double y, double r) {
        return new Disc(n, Coordinate.create(n + "\u2092", x, y), SymbolicConstant.create(r), true);
    }

    public static Disc create(String n, double x, double y, double r, boolean anon) {
        return new Disc(n, Coordinate.create(n + "\u2092", x, y), SymbolicConstant.create(r), anon);
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected Disc(String n, Coordinate center, SymbolicConstant radius, boolean anon) {
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
