package core.structure.unicardinal.alg.structure;

import core.Propositions.equalitypivot.EqualityPivot;
import core.Propositions.equalitypivot.unicardinal.UnicardinalPivot;
import core.structure.unicardinal.DefinedUnicardinal;
import core.structure.unicardinal.Unicardinal;

import java.util.*;

public abstract class Reduction extends DefinedUnicardinal {
    /** SECTION: Static Data ======================================================================================== */
    public static final UnicardinalInputType<Unicardinal> TERMS = new UnicardinalInputType<>();

    public static final List<InputType<?>> inputTypes = List.of(Reduction.TERMS);

    /** SECTION: Instance Variables ================================================================================= */
    public int degree = 0;

    /** SECTION: Abstract Constructor =============================================================================== */
    protected Reduction(Collection<? extends UnicardinalPivot<?>> args) {
        super();
        this.getInputs(Reduction.TERMS).addAll(args);

        args.forEach(arg -> {
            Unicardinal.createComputationalEdge(this, arg);
            arg.reverseDependencies().add(this);
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
