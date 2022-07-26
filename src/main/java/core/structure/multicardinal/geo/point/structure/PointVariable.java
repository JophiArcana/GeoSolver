package core.structure.multicardinal.geo.point.structure;

import core.Propositions.equalitypivot.EqualityPivot;
import core.Propositions.equalitypivot.LockedPivot;
import core.structure.multicardinal.*;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;
import core.structure.unicardinal.alg.symbolic.SymbolicVariable;
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
    public LockedPivot<SymbolicExpression, SymbolicVariable> var_x, var_y;

    /** SECTION: Factory Methods ==================================================================================== */
    public static PointVariable create(String n, double... args) {
        return new PointVariable(n, true, args);
    }

    public static PointVariable create(String n, boolean anon, double... args) {
        return new PointVariable(n, anon, args);
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected PointVariable(String n, boolean anon, double... args) {
        super(n, anon);
        this.var_x = SymbolicVariable.create(this.name + "\u1D6A", args[0]);
        this.var_y = SymbolicVariable.create(this.name + "\u1D67", args[1]);
        if (!this.anonymous) {
            this.node = new MoveablePointNode(this);
        }
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public List<EqualityPivot<SymbolicExpression>> symbolic() {
        return List.of(this.var_x, this.var_y);
    }
}
