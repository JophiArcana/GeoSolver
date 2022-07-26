package core.structure;

import core.Propositions.equalitypivot.EqualityPivot;
import core.Propositions.equalitypivot.multicardinal.MulticardinalPivot;
import core.Propositions.equalitypivot.unicardinal.UnicardinalPivot;
import core.structure.multicardinal.Multicardinal;
import core.structure.unicardinal.Unicardinal;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;
import core.Propositions.Proposition;
import com.google.common.collect.TreeMultiset;
import core.util.Utils;

import java.util.*;

public interface Entity {
    /** SECTION: Static Data ======================================================================================== */
    abstract class InputType<T extends Entity> implements Comparator<TreeMultiset<? extends EqualityPivot<?>>> {
        public abstract TreeMultiset<? extends EqualityPivot<T>> create();

        public abstract Comparator<? super T> comparator();

        public int compare(TreeMultiset<? extends EqualityPivot<?>> t1, TreeMultiset<? extends EqualityPivot<?>> t2) {
            Iterator<? extends EqualityPivot<? extends T>>
                    iter1 = ((TreeMultiset<? extends EqualityPivot<? extends T>>) t1).iterator(),
                    iter2 = ((TreeMultiset<? extends EqualityPivot<? extends T>>) t2).iterator();
            while (iter1.hasNext() && iter2.hasNext()) {
                int comparison = this.comparator().compare(iter1.next().element(), iter2.next().element());
                if (comparison != 0) {
                    return comparison;
                }
            }
            if (iter1.hasNext()) {
                return 1;
            } else if (iter2.hasNext()) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    class UnicardinalInputType<T extends Unicardinal> extends InputType<T> {
        public TreeMultiset<UnicardinalPivot<T>> create() {
            return TreeMultiset.create();
        }

        public Comparator<? super T> comparator() {
            return Utils.UNICARDINAL_COMPARATOR;
        }
    }

    class MulticardinalInputType<T extends Multicardinal> extends InputType<T> {
        public TreeMultiset<MulticardinalPivot<T>> create() {
            return TreeMultiset.create();
        }

        public Comparator<? super T> comparator() {
            return Utils.MULTICARDINAL_COMPARATOR;
        }
    }

    /** SECTION: Constructor Setup ================================================================================== */
    default void inputSetup() {
        if (this.getInputTypes() != null) {
            HashMap<InputType<?>, TreeMultiset<? extends EqualityPivot<?>>> inputs = this.getInputs();
            for (InputType<?> inputType : this.getInputTypes()) {
                inputs.put(inputType, inputType.create());
            }
        }
    }

    /** SECTION: Interface ========================================================================================== */
    List<UnicardinalPivot<SymbolicExpression>> symbolic();
    void deleteSymbolic();

    void updateLocalVariables(EqualityPivot<?> consumedPivot, EqualityPivot<?> consumerPivot);

    EqualityPivot<?> getEqualityPivot();
    void setEqualityPivot(EqualityPivot<?> pivot);

    /** SECTION: Getters ============================================================================================ */
    int getNaturalDegreesOfFreedom();
    int getConstrainedDegreesOfFreedom();
    HashSet<Proposition> getConstraints();

    HashMap<InputType<?>, TreeMultiset<? extends EqualityPivot<?>>> getInputs();
    /**
    default <T extends Entity> TreeMultiset<? extends EqualityPivot<? extends T>> getInputs(InputType<T> inputType) {
        return (TreeMultiset<? extends EqualityPivot<? extends T>>) this.getInputs().get(inputType);
    }
     */
    default <T extends Unicardinal> TreeMultiset<UnicardinalPivot<? extends T>> getInputs(UnicardinalInputType<T> inputType) {
        return (TreeMultiset<UnicardinalPivot<? extends T>>) this.getInputs().get(inputType);
    }
    default <T extends Multicardinal> TreeMultiset<MulticardinalPivot<? extends T>> getInputs(MulticardinalInputType<T> inputType) {
        return (TreeMultiset<MulticardinalPivot<? extends T>>) this.getInputs().get(inputType);
    }

    List<InputType<?>> getInputTypes();
}

