package core.structure.multicardinal.geo.point.structure;

import core.Diagram;
import core.structure.unicardinal.alg.Constant;
import core.structure.multicardinal.*;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;
import core.structure.unicardinal.alg.symbolic.constant.SymbolicReal;

import java.util.*;

public class Coordinate extends MultiConstant implements Point {
    /** SECTION: Static Data ======================================================================================== */
    public static final InputType<SymbolicReal> X = new InputType<>(SymbolicReal.class, Constant::compare);
    public static final InputType<SymbolicReal> Y = new InputType<>(SymbolicReal.class, Constant::compare);

    public static final List<InputType<?>> inputTypes = List.of(Coordinate.X, Coordinate.Y);

    public static int compare(Coordinate c1, Coordinate c2) {
        if (c1.x.value != c2.x.value) {
            return Double.compare(c1.x.value, c2.x.value);
        } else {
            return Double.compare(c1.y.value, c2.y.value);
        }
    }

    /** SECTION: Instance Variables ================================================================================= */
    public final SymbolicReal x, y;

    /** SECTION: Factory Methods ==================================================================================== */
    public static Coordinate create(Diagram d, String n, double x, double y) {
        return new Coordinate(d, n, SymbolicReal.create(d, x), SymbolicReal.create(d, y));
    }

    public static Coordinate create(Diagram d, String n, double x, double y, boolean anon) {
        return new Coordinate(d, n, SymbolicReal.create(d, x), SymbolicReal.create(d, y), anon);
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected Coordinate(Diagram d, String n, SymbolicReal x, SymbolicReal y) {
        this(d, n, x, y, true);
    }

    protected Coordinate(Diagram d, String n, SymbolicReal x, SymbolicReal y, boolean anon) {
        super(d, n, anon);
        this.x = x;
        this.y = y;
        this.getInputs(Coordinate.X).add(this.x);
        this.getInputs(Coordinate.Y).add(this.y);
        if (!this.anonymous) {
            this.node = new PointNode(this);
            this.diagram.root.getChildren().add(this.node);
        }
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public List<SymbolicExpression> symbolic() {
        return List.of(this.x, this.y);
    }

    public List<InputType<?>> getInputTypes() {
        return Coordinate.inputTypes;
    }
}
