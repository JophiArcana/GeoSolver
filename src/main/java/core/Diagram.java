package core;

import core.Propositions.equalitypivot.multicardinal.*;
import core.Propositions.equalitypivot.unicardinal.*;
import core.structure.multicardinal.*;
import core.structure.multicardinal.geo.point.structure.Point;
import core.structure.multicardinal.geo.point.structure.PointVariable;
import core.structure.multicardinal.geo.triangle.Triangle;
import core.structure.unicardinal.Unicardinal;
import core.structure.unicardinal.alg.symbolic.*;
import core.structure.unicardinal.alg.directed.*;
import core.util.Utils;
import javafx.scene.layout.Pane;

import java.util.HashSet;
import java.util.TreeMap;

public class Diagram {
    public static Diagram currentDiagram;

    /** SECTION: Metadata =========================================================================================== */
    private final TreeMap<Unicardinal, UnicardinalPivot<?>> UNICARDINAL_SET = new TreeMap<>(Utils.UNICARDINAL_COMPARATOR);
    private final TreeMap<Multicardinal, MulticardinalPivot<?>> MULTICARDINAL_SET = new TreeMap<>(Utils.MULTICARDINAL_COMPARATOR);

    public final HashSet<String> nameSet = new HashSet<>();

    public final Pane root = new Pane();

    /** SECTION: Data Validation ==================================================================================== */
    public static <T extends Unicardinal> UnicardinalPivot<T> retrieve(T query) {
        UnicardinalPivot<?> result = Diagram.currentDiagram.UNICARDINAL_SET.getOrDefault(query, null);
        if (result == null) {
            result = switch (query) {
                case SymbolicConstant sc -> LockedUnicardinalPivot.<SymbolicExpression, SymbolicConstant>of(sc);
                case SymbolicVariable sv -> LockedUnicardinalPivot.<SymbolicExpression, SymbolicVariable>of(sv);
                case DirectedConstant dc -> LockedUnicardinalPivot.<DirectedExpression, DirectedConstant>of(dc);
                case DirectedVariable dv -> LockedUnicardinalPivot.<DirectedExpression, DirectedVariable>of(dv);
                default -> UnlockedUnicardinalPivot.of(query);
            };
            Diagram.currentDiagram.UNICARDINAL_SET.put(query, result);
            query.setEqualityPivot(result);
            query.computeValue();   // A query has not been computed yet if and only if it is unregistered
        }
        return (UnicardinalPivot<T>) result;
    }

    public static <T extends Multicardinal> MulticardinalPivot<T> retrieve(T query) {
        MulticardinalPivot<T> result = (MulticardinalPivot<T>) Diagram.currentDiagram.MULTICARDINAL_SET.getOrDefault(query, null);
        if (result == null) {
            if (query instanceof MultiConstant || query instanceof MultiVariable) {
                result = LockedMulticardinalPivot.of(query);
            } else {
                result = UnlockedMulticardinalPivot.of(query);
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


