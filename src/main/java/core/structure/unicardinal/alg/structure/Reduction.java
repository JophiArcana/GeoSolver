package core.structure.unicardinal.alg.structure;

import core.structure.equalitypivot.EqualityPivot;
import core.structure.unicardinal.DefinedUnicardinal;
import core.structure.unicardinal.Unicardinal;

import java.util.*;

public abstract class Reduction extends DefinedUnicardinal {
    /** SECTION: Static Data ======================================================================================== */
    public static final InputType<Unicardinal> TERMS = new InputType<>();

    public static final List<InputType<?>> inputTypes = List.of(Reduction.TERMS);

    /** SECTION: Instance Variables ================================================================================= */
    public int degree = 0;

    /** SECTION: Abstract Constructor =============================================================================== */
    protected Reduction(Collection<? extends EqualityPivot<? extends Unicardinal>> args) {
        super();
        this.getInputs(Reduction.TERMS).addAll(args);
        args.forEach(arg -> {
            Unicardinal.createComputationalEdge(this, arg);
            arg.reverseDependencies.add(this);
        });
    }

    /** SECTION: Interface ========================================================================================== */
    protected abstract int identity();

    /** SECTION: Implementation ===================================================================================== */
    public void updateLocalVariables(EqualityPivot<?> consumedPivot, EqualityPivot<?> consumerPivot) {
    }

    /** SUBSECTION: Entity ========================================================================================== */


    public List<InputType<?>> getInputTypes() {
        return Reduction.inputTypes;
    }

    /** SUBSECTION: Expression ====================================================================================== */
    public int getDegree() {
        return this.degree;
    }
}
