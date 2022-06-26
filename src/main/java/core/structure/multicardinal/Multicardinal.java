package core.structure.multicardinal;

import core.structure.Entity;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.effect.Glow;

public interface Multicardinal extends Entity {
    interface GeometricNode {
        Glow GLOW = new Glow(1);

        static void setHoverGlow(Node node) {
            node.setOnMouseEntered(event -> {
                node.setCursor(Cursor.HAND);
                node.setEffect(GeometricNode.GLOW);
            });
            node.setOnMouseExited(event -> {
                node.setCursor(Cursor.DEFAULT);
                node.setEffect(null);
            });
        }
    }

    /** SECTION: Interface ========================================================================================== */
    String getName();
    Node getNode();
    boolean isAnonymous();
}
