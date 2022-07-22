package core.structure;

import core.structure.equalitypivot.EqualityPivot;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;
import core.Propositions.PropositionStructure.Proposition;
import com.google.common.collect.TreeMultiset;

import java.util.*;

public interface Entity {
    /** SECTION: Static Data ======================================================================================== */
    class InputType<T extends Entity> {
        public TreeMultiset<EqualityPivot<T>> create() {
            return TreeMultiset.create();
        }
    }

    /** SECTION: Constructor Setup ================================================================================== */
    default void inputSetup() {
        if (this.getInputTypes() != null) {
            HashMap<InputType<?>, TreeMultiset<EqualityPivot<?>>> inputs = this.getInputs();
            for (InputType<?> inputType : this.getInputTypes()) {
                inputs.put(inputType, (TreeMultiset<EqualityPivot<?>>) (TreeMultiset<? extends EqualityPivot>) inputType.create());
            }
        }
    }

    /** SECTION: Interface ========================================================================================== */
    List<EqualityPivot<SymbolicExpression>> symbolic();

    void updateLocalVariables(EqualityPivot<?> consumedPivot, EqualityPivot<?> consumerPivot);

    EqualityPivot<?> getEqualityPivot();
    void setEqualityPivot(EqualityPivot<?> pivot);

    default EqualityPivot<?> mergeResult(EqualityPivot<?> pivot) {
        return EqualityPivot.merge((EqualityPivot) this.getEqualityPivot(), pivot);
    }

    void deleteSymbolic();

    /** SECTION: Getters ============================================================================================ */
    int getNaturalDegreesOfFreedom();
    int getConstrainedDegreesOfFreedom();
    HashSet<Proposition> getConstraints();

    HashMap<InputType<?>, TreeMultiset<EqualityPivot<?>>> getInputs();
    default <T extends Entity> TreeMultiset<EqualityPivot<? extends T>> getInputs(InputType<T> inputType) {
        return (TreeMultiset<EqualityPivot<? extends T>>) (TreeMultiset<EqualityPivot<? extends Entity>>) this.getInputs().get(inputType);
    }

    List<InputType<?>> getInputTypes();
}

