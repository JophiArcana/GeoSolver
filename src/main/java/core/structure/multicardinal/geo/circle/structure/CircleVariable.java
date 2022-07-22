package core.structure.multicardinal.geo.circle.structure;

import core.structure.multicardinal.MultiVariable;
import core.structure.multicardinal.geo.point.structure.*;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;
import core.structure.unicardinal.alg.symbolic.SymbolicVariable;
import javafx.scene.Cursor;

import java.util.*;

public class CircleVariable extends MultiVariable implements Circle {
    /** SECTION: Inner Classes ====================================================================================== */
    public static class MoveableCircleNode extends CircleNode {
        public MoveableCircleNode(CircleVariable c) {
            super(c);
            this.setOnMouseDragged(event -> {
                this.setCursor(Cursor.CLOSED_HAND);
                double x = event.getSceneX() - this.getTranslateX();
                double y = event.getSceneY() - this.getTranslateY();
                c.var_r.value.set(Math.sqrt(x * x + y * y));
            });
        }
    }

    /** SECTION: Instance Variables ================================================================================= */
    public PointVariable center;
    public SymbolicVariable var_r;

    /** SECTION: Factory Methods ==================================================================================== */
    public static CircleVariable create(String n, double... args) {
        return new CircleVariable(n, true, args);
    }

    public static CircleVariable create(String n, boolean anon, double... args) {
        return new CircleVariable(n, anon, args);
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected CircleVariable(String n, boolean anon, double... args) {
        super(n, anon);
        this.center = PointVariable.create(n + "\u2092", anon, args[0], args[1]);
        this.var_r = SymbolicVariable.create(n + "\u1D63", args[2]);
        if (!this.anonymous) {
            this.node = new MoveableCircleNode(this);
        }
    }

    /** SECTION: Implementation ===================================================================================== */
    public List<SymbolicExpression> symbolic() {
        return List.of(this.center.var_x, this.center.var_y, this.var_r);
    }

    /** SUBSECTION: Circle ========================================================================================== */
    public Point center() {
        return this.center;
    }

    public SymbolicExpression radius() {
        return this.var_r;
    }
}
