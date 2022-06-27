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

    HashSet<Unicardinal> reverseDependencies();

    static Unicardinal[] topologicalSort(Collection<Unicardinal> roots) {
        int[] clock = {1};
        HashMap<Unicardinal, Integer> visited = new HashMap<>();
        for (Unicardinal root : roots) {
            Unicardinal.reverseDependencyDFS(clock, visited, root);
        }
        int n = visited.size();
        Unicardinal[] result = new Unicardinal[n];
        visited.forEach((node, postNumber) -> result[n - postNumber] = node);
        return result;
    }

    static void reverseDependencyDFS(int[] clock, HashMap<Unicardinal, Integer> visited, Unicardinal u) {
        if (u.reverseDependencies() != null) {
            for (Unicardinal v : u.reverseDependencies()) {
                if (!visited.containsKey(v)) {
                    reverseDependencyDFS(clock, visited, v);
                }
            }
            visited.put(u, clock[0]);
            clock[0]++;
        }
    }
}
