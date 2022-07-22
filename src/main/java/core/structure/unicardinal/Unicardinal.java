package core.structure.unicardinal;

import core.structure.Entity;
import core.structure.Immutable;
import core.structure.equalitypivot.EqualityPivot;
import core.structure.equalitypivot.LockedEqualityPivot;
import core.structure.unicardinal.alg.structure.Real;
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
    HashSet<EqualityPivot<? extends Unicardinal>> computationalDependencies();

    static void createComputationalEdge(Unicardinal u, EqualityPivot<? extends Unicardinal> p) {
        u.computationalDependencies().add(p);
        p.reverseComputationalDependencies.add(u);
    }

    default void removeComputationalEdges() {
        for (EqualityPivot<?> p : this.computationalDependencies()) {
            p.reverseComputationalDependencies.remove(this);
        }
        this.computationalDependencies().clear();
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
    EqualityPivot<? extends Unicardinal> expand();
    EqualityPivot<? extends Unicardinal> close();    // Returning EqualityPivot containing the result of close makes it easier to merge

    LockedEqualityPivot<? extends Unicardinal, ? extends Real> createReal(double value);
    EqualityPivot<? extends Unicardinal> createAdd(Collection<? extends EqualityPivot<? extends Unicardinal>> args);
    EqualityPivot<? extends Unicardinal> createScale(double coefficient, EqualityPivot<? extends Unicardinal> expr);

    int getDegree();

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    default void deleteSymbolic() {
    }
}
