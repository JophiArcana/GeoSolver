package core.structure.unicardinal;

import core.structure.Entity;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.*;

public interface Unicardinal extends Entity {
    /** SECTION: Interface ========================================================================================== */
    SimpleDoubleProperty valueProperty();
    default double doubleValue() {
        return this.valueProperty().get();
    }
    void computeValue();

    // HashSet<EqualityPivot<Unicardinal>> computationalDependencies()
    HashSet<Unicardinal> reverseComputationalDependencies();

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

    static void reverseComputationalDependencyDFS(int[] clock, HashMap<Unicardinal, Integer> visited, Unicardinal u) {
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
}
