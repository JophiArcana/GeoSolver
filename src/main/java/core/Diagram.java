package core;

import core.structure.multicardinal.Multicardinal;
import core.structure.multicardinal.geo.point.structure.Point;
import core.structure.multicardinal.geo.point.structure.PointVariable;
import core.structure.multicardinal.geo.triangle.Triangle;
import core.structure.unicardinal.Unicardinal;
import core.util.Utils;
import javafx.scene.layout.Pane;

import java.util.HashSet;
import java.util.Optional;
import java.util.TreeMap;

public class Diagram {
    public static Diagram CURRENT_DIAGRAM;

    /** SECTION: Metadata =========================================================================================== */
    private final TreeMap<Unicardinal, Unicardinal> unicardinalSet = new TreeMap<>(Utils.UNICARDINAL_COMPARATOR);
    private final TreeMap<Multicardinal, Multicardinal> multicardinalSet = new TreeMap<>(Utils.MULTICARDINAL_COMPARATOR);

    public final HashSet<String> nameSet = new HashSet<>();

    public final Pane root = new Pane();

    /** SECTION: Data Validation ==================================================================================== */
    public static <T extends Unicardinal> T retrieve(T query) {
        Unicardinal result = Diagram.CURRENT_DIAGRAM.unicardinalSet.getOrDefault(query, null);
        if (result == null) {
            Diagram.CURRENT_DIAGRAM.unicardinalSet.put(query, query);
            query.computeValue(); // A query has not been computed yet if and only if it is unregistered
            return query;
        } else {
            return (T) result;
        }
    }

    public static <T extends Multicardinal> T retrieve(T query) {
        Multicardinal result = Diagram.CURRENT_DIAGRAM.multicardinalSet.getOrDefault(query, null);
        if (result == null) {
            Diagram.CURRENT_DIAGRAM.multicardinalSet.put(query, query);
            return query;
        } else {
            return (T) result;
        }
    }

    public static void validateName(String n) {
        assert Diagram.CURRENT_DIAGRAM.nameSet.add(n): "Name " + n + " already in use.";
    }

    /** SECTION: API ================================================================================================ */
    public static PointVariable createPointVariable(String n, double x, double y) {
        Diagram.validateName(n);
        PointVariable P = PointVariable.create(n, false, x, y);
        Diagram.CURRENT_DIAGRAM.root.getChildren().add(P.node);
        return P;
    }

    public static Triangle.Circumcenter createCircumcenter(String n, Point A, Point B, Point C) {
        Diagram.validateName(n);
        Triangle.Circumcenter O = Triangle.create(A, B, C).circumcenter(n, false);
        Diagram.CURRENT_DIAGRAM.root.getChildren().add(O.node);
        return O;
    }

    public static Triangle.Circumcircle createCircumcircle(String n, Point A, Point B, Point C) {
        Diagram.validateName(n);
        Triangle.Circumcircle L = Triangle.create(A, B, C).circumcircle(n, false);
        Diagram.CURRENT_DIAGRAM.root.getChildren().add(L.node);
        Diagram.CURRENT_DIAGRAM.root.getChildren().add(L.center.getNode());
        L.node.toBack();
        return L;
    }

    public static Triangle.Orthocenter createOrthocenter(String n, Point A, Point B, Point C) {
        Diagram.validateName(n);
        Triangle.Orthocenter H = Triangle.create(A, B, C).orthocenter(n, false);
        Diagram.CURRENT_DIAGRAM.root.getChildren().add(H.node);
        return H;
    }
}


