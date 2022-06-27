package core.structure.multicardinal.geo.point.structure;

import core.structure.multicardinal.*;
import core.structure.unicardinal.Unicardinal;
import core.structure.unicardinal.alg.symbolic.*;
import javafx.scene.Cursor;

import java.util.*;

public class PointVariable extends MultiVariable implements Point {
    /** SECTION: Inner Classes ====================================================================================== */
    public static class MoveablePointNode extends PointNode {
        private static double x, y;
        private static Unicardinal[] updateOrder;

        public MoveablePointNode(PointVariable p) {
            super(p);
            this.setOnMousePressed(event -> {
                this.setCursor(Cursor.CLOSED_HAND);
                MoveablePointNode.x = event.getSceneX() - this.getTranslateX();
                MoveablePointNode.y = event.getSceneY() - this.getTranslateY();
                MoveablePointNode.updateOrder = Unicardinal.topologicalSort(List.of(p.var_x, p.var_y));
            });
            this.setOnMouseDragged(event -> {
                this.setCursor(Cursor.CLOSED_HAND);
                p.var_x.value.set(event.getSceneX() - MoveablePointNode.x);
                p.var_y.value.set(event.getSceneY() - MoveablePointNode.y);
                for (Unicardinal u : MoveablePointNode.updateOrder) {
                    u.computeValue();
                }
            });
        }
    }

    /** SECTION: Instance Variables ================================================================================= */
    public SymbolicVariable var_x, var_y;

    /** SECTION: Factory Methods ==================================================================================== */
    public static PointVariable create(String n, double xStart, double yStart) {
        return new PointVariable(n, xStart, yStart, true);
    }

    public static PointVariable create(String n, double xStart, double yStart, boolean anon) {
        return new PointVariable(n, xStart, yStart, anon);
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected PointVariable(String n, double xStart, double yStart, boolean anon) {
        super(n, anon);
        this.var_x = SymbolicVariable.create(this.name + "\u1D6A", xStart);
        this.var_y = SymbolicVariable.create(this.name + "\u1D67", yStart);
        if (!this.anonymous) {
            this.node = new MoveablePointNode(this);
        }
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public List<SymbolicExpression> symbolic() {
        return List.of(this.var_x, this.var_y);
    }
}
