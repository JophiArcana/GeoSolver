package core.structure.unicardinal.alg.directed.operator;

import core.structure.unicardinal.alg.structure.Add;
import core.structure.unicardinal.alg.*;
import core.structure.unicardinal.alg.directed.DirectedExpression;
import core.structure.unicardinal.alg.structure.Reduction;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;
import core.structure.unicardinal.alg.symbolic.constant.SymbolicReal;
import core.structure.unicardinal.alg.symbolic.operator.*;
import core.util.Utils;

import java.util.*;

public class DirectedAdd extends Add implements DirectedExpression {
    /** SECTION: Factory Methods ==================================================================================== */
    public static DirectedExpression create(Collection<DirectedExpression> args) {
        return (DirectedExpression) new DirectedAdd(args).close();
    }

    public static DirectedExpression create(DirectedExpression... args) {
        return (DirectedExpression) new DirectedAdd(List.of(args)).close();
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected DirectedAdd(Collection<DirectedExpression> args) {
        super(args);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public List<SymbolicExpression> symbolic() {
        List<DirectedExpression> terms = List.copyOf(Utils.cast(this.getInputs(Reduction.TERMS)));
        ArrayList<List<HashSet<DirectedExpression>>> subsets = Utils.sortedSubsets(terms);

        ArrayList<SymbolicExpression> numeratorTerms = new ArrayList<>();
        ArrayList<SymbolicExpression> denominatorTerms = new ArrayList<>(List.of(SymbolicReal.create(1)));

        for (int i = 1; i < subsets.size(); i++) {
            List<SymbolicExpression> symbolics = Utils.map(subsets.get(i),
                    subset -> SymbolicMul.create(Utils.map(subset, arg -> arg.symbolic().get(0))));
            SymbolicExpression symmetricSum = SymbolicAdd.create(symbolics);
            switch (i % 4) {
                case 0:
                    denominatorTerms.add(symmetricSum);
                case 1:
                    numeratorTerms.add(symmetricSum);
                case 2:
                    denominatorTerms.add(SymbolicScale.create(-1, symmetricSum));
                case 3:
                    numeratorTerms.add(SymbolicScale.create(-1, symmetricSum));
            }
        }
        return List.of(Utils.ENGINE.div(
                SymbolicAdd.create(numeratorTerms),
                SymbolicAdd.create(denominatorTerms)
        ));
    }

    /** SUBSECTION: Add ============================================================================================= */
    protected Add createRawAdd(Collection<? extends Expression> args) {
        return new DirectedAdd((Collection<DirectedExpression>) args);
    }
}
