package core.structure.multicardinal.geo.point.structure;

import core.structure.multicardinal.Multicardinal;
import javafx.scene.Cursor;
import javafx.scene.paint.Color;

public interface Point extends Multicardinal {
    /** SECTION: Static Data ======================================================================================== */
    int naturalDegreesOfFreedom = 2;

    /** SECTION: Inner Classes ====================================================================================== */
    class PointNode extends javafx.scene.shape.Circle implements GeometricNode {
        protected static double x, y;

        public PointNode(Point p) {
            super(10);
            this.translateXProperty().bind(p.symbolic().get(0));
            this.translateYProperty().bind(p.symbolic().get(1));

            this.setStyle("-fx-fill: rgb(69,11,215)");
            this.setStroke(Color.rgb(127, 129, 245));
            this.setStrokeWidth(2);

            GeometricNode.setHoverGlow(this);
            this.setOnMousePressed(event -> {
                this.setCursor(Cursor.CLOSED_HAND);
                PointNode.x = event.getSceneX() - this.getTranslateX();
                PointNode.y = event.getSceneY() - this.getTranslateY();
            });
            this.setOnMouseReleased(event -> this.setCursor(Cursor.HAND));
        }
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    default int getNaturalDegreesOfFreedom() {
        return Point.naturalDegreesOfFreedom;
    }
}
