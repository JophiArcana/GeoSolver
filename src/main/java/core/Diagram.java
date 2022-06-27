package core;

import core.structure.multicardinal.geo.point.structure.Point;
import core.structure.multicardinal.geo.point.structure.PointVariable;
import core.structure.multicardinal.geo.triangle.Triangle;
import javafx.scene.layout.Pane;

import java.util.HashSet;

public class Diagram {
    public final HashSet<String> nameSet = new HashSet<>();
    public final Pane root = new Pane();

    public PointVariable createPointVariable(String n, double x, double y) {
        PointVariable P = PointVariable.create(n, x, y, false);
        this.root.getChildren().add(P.node);
        return P;
    }

    public Triangle.Circumcenter createCircumcenter(String n, Point A, Point B, Point C) {
        Triangle.Circumcenter O = Triangle.create(A, B, C).circumcenter(n, false);
        this.root.getChildren().add(O.node);
        return O;
    }

    public Triangle.Circumcircle createCircumcircle(String n, Point A, Point B, Point C) {
        Triangle.Circumcircle L = Triangle.create(A, B, C).circumcircle(n, false);
        this.root.getChildren().add(L.node);
        this.root.getChildren().add(L.center().getNode());
        return L;
    }
}


