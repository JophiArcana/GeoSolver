package Core.Utilities;

import Core.EntityTypes.*;
import com.google.common.collect.TreeMultiset;

import java.util.*;

public class PriorityComparator implements Comparator<Entity> {
    public int compare(Entity e1, Entity e2) {
        if (Utils.classCode(e1) != Utils.classCode(e2)) {
            return Utils.classCode(e1) - Utils.classCode(e2);
        } else if (e1 == e2) {
            return 0;
        } else if (e1 instanceof Immutable const1) {
            return const1.compareTo(e2);
        } else if (e1 instanceof Mutable var1) {
            return var1.compareTo(e2);
        }
        else {
            HashMap<String, TreeMultiset<Entity>> e1Inputs = e1.getInputs();
            HashMap<String, TreeMultiset<Entity>> e2Inputs = e2.getInputs();
            for (String inputType : e1.getInputTypes()) {
                Iterator<Entity> e1Descending = e1Inputs.get(inputType).descendingMultiset().iterator();
                Iterator<Entity> e2Descending = e2Inputs.get(inputType).descendingMultiset().iterator();
                while (e1Descending.hasNext() && e2Descending.hasNext()) {
                    int inputComparison = compare(e1Descending.next(), e2Descending.next());
                    if (inputComparison != 0) {
                        return inputComparison;
                    }
                }
                if (e1Descending.hasNext()) {
                    return 1;
                } else if (e2Descending.hasNext()) {
                    return -1;
                }
            }
            return 0;
        }
    }
}