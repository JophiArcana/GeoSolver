package Core.EntityStructure.UnicardinalStructure;

import Core.AlgSystem.Constants.Real;
import Core.AlgSystem.UnicardinalRings.DirectedAngle;
import Core.AlgSystem.UnicardinalRings.Symbolic;
import Core.Diagram;
import Core.EntityStructure.Entity;
import Core.Utilities.Comparators.UnicardinalComparator;
import com.google.common.collect.TreeMultiset;

import java.util.HashMap;

public abstract class DefinedOperator<T> extends DefinedExpression<T> {
    /** SECTION: Instance Variables ================================================================================= */
    public final InputType[][] inputTypes;

    /** SECTION: Abstract Constructor =============================================================================== */
    public DefinedOperator(Diagram d, Class<T> type) {
        super(d, type);
        this.inputTypes = new InputType[5][];
        if (this.TYPE == Symbolic.class) {
            for (InputType inputType : (this.inputTypes[0] = this.getStaticInputTypes())) {
                this.inputs.put(inputType, TreeMultiset.create(new UnicardinalComparator<Symbolic>()));
            }
        } else if (this.TYPE == DirectedAngle.class) {
            for (InputType inputType : (this.inputTypes[1] = this.getStaticInputTypes())) {
                this.inputs.put(inputType, TreeMultiset.create(new UnicardinalComparator<DirectedAngle>()));
            }
        }
    }

    /** SECTION: Interface ========================================================================================== */
    protected abstract InputType[] getStaticInputTypes();

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public InputType[][] getInputTypes() {
        return this.inputTypes;
    }

}
