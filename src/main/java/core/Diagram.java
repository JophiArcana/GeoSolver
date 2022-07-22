package core;

import core.structure.Immutable;
import core.structure.equalitypivot.EqualityPivot;
import core.structure.equalitypivot.LockedEqualityPivot;
import core.structure.multicardinal.MultiConstant;
import core.structure.multicardinal.MultiVariable;
import core.structure.multicardinal.Multicardinal;
import core.structure.multicardinal.geo.point.structure.Point;
import core.structure.multicardinal.geo.point.structure.PointVariable;
import core.structure.multicardinal.geo.triangle.Triangle;
import core.structure.unicardinal.Constant;
import core.structure.unicardinal.Unicardinal;
import core.structure.unicardinal.Variable;
import core.util.Utils;
import javafx.scene.layout.Pane;

import java.util.HashSet;
import java.util.TreeMap;

public class Diagram {
    public static Diagram currentDiagram;

    /** SECTION: Metadata =========================================================================================== */
    private final TreeMap<Unicardinal, EqualityPivot<? extends Unicardinal>> UNICARDINAL_SET = new TreeMap<>(Utils.UNICARDINAL_COMPARATOR);
    private final TreeMap<Multicardinal, EqualityPivot<? extends Multicardinal>> MULTICARDINAL_SET = new TreeMap<>(Utils.MULTICARDINAL_COMPARATOR);

    public final HashSet<String> nameSet = new HashSet<>();

    public final Pane root = new Pane();

    /** SECTION: Data Validation ==================================================================================== */
    public static <T extends Unicardinal> EqualityPivot<T> retrieve(T query) {
        EqualityPivot<T> result = (EqualityPivot<T>) Diagram.currentDiagram.UNICARDINAL_SET.getOrDefault(query, null);
        if (result == null) {
            if (query instanceof Constant || query instanceof Variable) {
                result = LockedEqualityPivot.of(query);
            } else {
                result = EqualityPivot.of(query);
            }
            Diagram.currentDiagram.UNICARDINAL_SET.put(query, result);
            query.setEqualityPivot(result);
            query.computeValue();   // A query has not been computed yet if and only if it is unregistered
        }
        return result;
    }

    public static <T extends Multicardinal> EqualityPivot<T> retrieve(T query) {
        EqualityPivot<T> result = (EqualityPivot<T>) Diagram.currentDiagram.MULTICARDINAL_SET.getOrDefault(query, null);
        if (result == null) {
            if (query instanceof MultiConstant || query instanceof MultiVariable) {
                result = LockedEqualityPivot.of(query);
            } else {
                result = EqualityPivot.of(query);
            }
            Diagram.currentDiagram.MULTICARDINAL_SET.put(query, result);
            query.setEqualityPivot(result);
        }
        return result;
    }

    public static void validateName(String n) {
        assert Diagram.currentDiagram.nameSet.add(n): "Name " + n + " already in use.";
    }

    /** SECTION: API ================================================================================================ */
    public static PointVariable createPointVariable(String n, double x, double y) {
        Diagram.validateName(n);
        PointVariable P = PointVariable.create(n, false, x, y);
        Diagram.currentDiagram.root.getChildren().add(P.node);
        return P;
    }

    public static Triangle.Circumcenter createCircumcenter(String n, Point A, Point B, Point C) {
        Diagram.validateName(n);
        Triangle.Circumcenter O = Triangle.create(A, B, C).circumcenter(n, false);
        Diagram.currentDiagram.root.getChildren().add(O.node);
        return O;
    }

    public static Triangle.Circumcircle createCircumcircle(String n, Point A, Point B, Point C) {
        Diagram.validateName(n);
        Triangle.Circumcircle L = Triangle.create(A, B, C).circumcircle(n, false);
        Diagram.currentDiagram.root.getChildren().add(L.node);
        Diagram.currentDiagram.root.getChildren().add(L.center.getNode());
        L.node.toBack();
        return L;
    }

    public static Triangle.Orthocenter createOrthocenter(String n, Point A, Point B, Point C) {
        Diagram.validateName(n);
        Triangle.Orthocenter H = Triangle.create(A, B, C).orthocenter(n, false);
        Diagram.currentDiagram.root.getChildren().add(H.node);
        return H;
    }
}


