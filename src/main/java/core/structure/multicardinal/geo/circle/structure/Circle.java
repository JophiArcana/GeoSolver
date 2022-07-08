package core.structure.multicardinal.geo.circle.structure;

import core.structure.multicardinal.geo.Locus;
import core.structure.multicardinal.geo.point.structure.Point;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public interface Circle extends Locus {
    /** SECTION: Static Data ======================================================================================== */
    int naturalDegreesOfFreedom = 2;

    /** SECTION: Inner Classes ======================================================================================== */
    class CircleNode extends javafx.scene.shape.Circle implements GeometricNode {
        public CircleNode(Circle c) {
            super();
            this.translateXProperty().bind(c.center().getNode().translateXProperty());
            this.translateYProperty().bind(c.center().getNode().translateYProperty());
            this.radiusProperty().bind(c.radius().valueProperty());

            this.setFill(null);
            this.setStroke(Color.BLACK);
            this.setStrokeWidth(2);

            GeometricNode.setHoverGlow(this);
            this.setOnMousePressed(event -> this.setCursor(Cursor.CLOSED_HAND));
            this.setOnMouseReleased(event -> this.setCursor(Cursor.HAND));
        }
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    default int getNaturalDegreesOfFreedom() {
        return Circle.naturalDegreesOfFreedom;
    }

    /** SECTION: Interface ========================================================================================== */
    Point center();
    SymbolicExpression radius();
}
