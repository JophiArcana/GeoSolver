package core.structure;

import core.structure.unicardinal.alg.symbolic.SymbolicExpression;
import core.Propositions.PropositionStructure.Proposition;
import com.google.common.collect.TreeMultiset;

import java.util.*;

public interface Entity extends Proposition {
    /** SECTION: Static Data ======================================================================================== */
    class InputType<T extends Entity> {
        private final Class<T> CLASS;
        private final Comparator<? super T> COMPARATOR;

        public InputType(Class<T> cls, Comparator<? super T> comparator) {
            this.CLASS = cls;
            this.COMPARATOR = comparator;
        }

        public TreeMultiset<T> create() {
            return TreeMultiset.create(this.COMPARATOR);
        }

        public int compare(Entity e1, Entity e2) {
            return this.COMPARATOR.compare(this.CLASS.cast(e1), this.CLASS.cast(e2));
        }
    }

    /** SECTION: Constructor Setup ================================================================================== */
    default void inputSetup() {
        if (this.getInputTypes() != null) {
            HashMap<InputType<?>, TreeMultiset<? extends Entity>> inputs = this.getInputs();
            for (InputType<?> inputType : this.getInputTypes()) {
                inputs.put(inputType, inputType.create());
            }
        }
    }

    /** SECTION: Interface ========================================================================================== */
    List<SymbolicExpression> symbolic();

    int getNaturalDegreesOfFreedom();
    int getConstrainedDegreesOfFreedom();
    HashSet<Proposition> getConstraints();

    HashMap<InputType<?>, TreeMultiset<? extends Entity>> getInputs();
    default <T extends Entity> TreeMultiset<T> getInputs(InputType<T> inputType) {
        return (TreeMultiset<T>) this.getInputs().get(inputType);
    }

    List<InputType<?>> getInputTypes();

    /** SECTION: Equality Proposition Reduction ===================================================================== */
    default void mergeEquality(Entity a, Entity b) {

    }

    EqualityPivot<?> getEqualityPivot();
    void setEqualityPivot();

    void deleteSymbolic();
}

