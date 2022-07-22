package core.structure.unicardinal.alg.directed.operator;

import core.structure.equalitypivot.EqualityPivot;
import core.structure.unicardinal.Unicardinal;
import core.structure.unicardinal.alg.directed.DirectedExpression;
import core.structure.unicardinal.alg.structure.*;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;
import core.structure.unicardinal.alg.symbolic.constant.SymbolicReal;
import core.structure.unicardinal.alg.symbolic.operator.*;
import core.util.Utils;

import java.util.*;

public class DirectedAdd extends Add implements DirectedExpression {
    /** SECTION: Factory Methods ==================================================================================== */
    public static EqualityPivot<DirectedExpression> create(Collection<EqualityPivot<DirectedExpression>> args) {
        return (EqualityPivot<DirectedExpression>) new DirectedAdd(args).close();
    }

    @SafeVarargs
    public static EqualityPivot<DirectedExpression> create(EqualityPivot<DirectedExpression>... args) {
        return (EqualityPivot<DirectedExpression>) new DirectedAdd(List.of(args)).close();
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected DirectedAdd(Collection<EqualityPivot<DirectedExpression>> args) {
        super(args);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public List<EqualityPivot<SymbolicExpression>> symbolic() {
        List<EqualityPivot<DirectedExpression>> terms = List.copyOf(Utils.cast(this.getInputs(Reduction.TERMS)));
        ArrayList<List<HashSet<EqualityPivot<DirectedExpression>>>> subsets = Utils.sortedSubsets(terms);

        ArrayList<EqualityPivot<SymbolicExpression>> numeratorTerms = new ArrayList<>();
        ArrayList<EqualityPivot<SymbolicExpression>> denominatorTerms = new ArrayList<>(List.of(SymbolicReal.ONE));

        for (int i = 1; i < subsets.size(); i++) {
            List<EqualityPivot<SymbolicExpression>> symbolics = Utils.map(subsets.get(i),
                    subset -> SymbolicMul.create(Utils.map(subset, arg -> arg.simplestElement.symbolic().get(0))));
            EqualityPivot<SymbolicExpression> symmetricSum = SymbolicAdd.create(symbolics);
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
    protected Add createRawAdd(Collection<? extends EqualityPivot<? extends Unicardinal>> args) {
        return new DirectedAdd((Collection<EqualityPivot<DirectedExpression>>) args);
    }
}
