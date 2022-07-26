package core.structure.unicardinal;

import core.Propositions.equalitypivot.unicardinal.LockedUnicardinalPivot;
import core.Propositions.equalitypivot.unicardinal.UnicardinalPivot;
import core.structure.Entity;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.*;

public interface Unicardinal extends Entity {
    /** SECTION: Interface ========================================================================================== */
    SimpleDoubleProperty valueProperty();
    default double doubleValue() {
        return this.valueProperty().get();
    }

    /** SECTION: GUI Value Computation ============================================================================== */
    void computeValue();
    HashSet<UnicardinalPivot<?>> computationalDependencies();
    void deleteComputationalDependencies();

    static void createComputationalEdge(Unicardinal u, UnicardinalPivot<?> p) {
        u.computationalDependencies().add(p);
        p.reverseComputationalDependencies().add(u);
    }

    default void deleteComputationalEdges() {
        this.computationalDependencies().forEach(arg -> arg.reverseComputationalDependencies().remove(this));
        this.deleteComputationalDependencies();
    }

    /**
     * To move to UnicardinalPivot<>
    static Unicardinal[] topologicalSort(Collection<Unicardinal> roots) {
        int[] clock = {1};
        HashMap<Unicardinal, Integer> visited = new HashMap<>();
        for (Unicardinal root : roots) {
            Unicardinal.reverseComputationalDependencyDFS(clock, visited, root);
        }
        int n = visited.size();
        Unicardinal[] result = new Unicardinal[n];
        visited.forEach((node, postNumber) -> result[n - postNumber] = node);
        return result;
    }

    static void reverseComputationalDependencyDFS(int[] clock, HashMap<UnicardinalPivot<?>, Integer> visited, Unicardinal u) {
        if (u.reverseComputationalDependencies() != null) {
            for (Unicardinal v : u.reverseComputationalDependencies()) {
                if (!visited.containsKey(v)) {
                    reverseComputationalDependencyDFS(clock, visited, v);
                }
            }
            visited.put(u, clock[0]);
            clock[0]++;
        }
    }
     */

    /** SECTION: Algebra ========================================================================================== */
    UnicardinalPivot<?> expand();
    UnicardinalPivot<?> close();    // Returning EqualityPivot containing the result of close makes it easier to merge

    LockedUnicardinalPivot<?, ? extends Constant> createConstant(double value);
    UnicardinalPivot<?> createAdd(Collection<? extends UnicardinalPivot<?>> args);
    UnicardinalPivot<?> createScale(double coefficient, UnicardinalPivot<?> expr);

    int getDegree();

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    default void deleteSymbolic() {
    }
}
