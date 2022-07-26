package core.structure.unicardinal.alg.directed.operator;

import core.Propositions.equalitypivot.unicardinal.UnicardinalPivot;
import core.structure.unicardinal.alg.directed.DirectedExpression;
import core.structure.unicardinal.alg.structure.*;
import core.structure.unicardinal.alg.symbolic.*;
import core.structure.unicardinal.alg.symbolic.operator.*;
import core.util.Utils;

import java.util.*;

public class DirectedAdd extends Add implements DirectedExpression {
    /** SECTION: Factory Methods ==================================================================================== */
    public static UnicardinalPivot<DirectedExpression> create(Collection<UnicardinalPivot<DirectedExpression>> args) {
        return (UnicardinalPivot<DirectedExpression>) new DirectedAdd(args).close();
    }

    @SafeVarargs
    public static UnicardinalPivot<DirectedExpression> create(UnicardinalPivot<DirectedExpression>... args) {
        return (UnicardinalPivot<DirectedExpression>) new DirectedAdd(List.of(args)).close();
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected DirectedAdd(Collection<UnicardinalPivot<DirectedExpression>> args) {
        super(args);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public List<UnicardinalPivot<SymbolicExpression>> symbolic() {
        List<UnicardinalPivot<DirectedExpression>> terms = List.copyOf(Utils.cast(this.getInputs(Reduction.TERMS)));
        ArrayList<List<HashSet<UnicardinalPivot<DirectedExpression>>>> subsets = Utils.sortedSubsets(terms);

        ArrayList<UnicardinalPivot<SymbolicExpression>> numeratorTerms = new ArrayList<>();
        ArrayList<UnicardinalPivot<SymbolicExpression>> denominatorTerms = new ArrayList<>(List.of(SymbolicConstant.ONE));

        for (int i = 1; i < subsets.size(); i++) {
            List<UnicardinalPivot<SymbolicExpression>> symbolics = Utils.map(subsets.get(i),
                    subset -> SymbolicMul.create(Utils.map(subset, arg -> arg.element().symbolic().get(0))));
            UnicardinalPivot<SymbolicExpression> symmetricSum = SymbolicAdd.create(symbolics);
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
        return List.of(SymbolicMul.create(
                SymbolicAdd.create(numeratorTerms),
                SymbolicPow.create(SymbolicAdd.create(denominatorTerms), -1)
        ));
    }

    /** SUBSECTION: Add ============================================================================================= */
    protected Add createRawAdd(Collection<? extends UnicardinalPivot<?>> args) {
        return new DirectedAdd((Collection<UnicardinalPivot<DirectedExpression>>) args);
    }
}
