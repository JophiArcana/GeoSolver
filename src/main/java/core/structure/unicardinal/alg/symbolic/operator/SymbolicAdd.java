package core.structure.unicardinal.alg.symbolic.operator;

import core.Propositions.equalitypivot.unicardinal.UnicardinalPivot;
import core.structure.unicardinal.alg.structure.Add;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;

import java.util.*;

public class SymbolicAdd extends Add implements SymbolicExpression {
    /** SECTION: Factory Methods ==================================================================================== */
    public static UnicardinalPivot<SymbolicExpression> create(Collection<UnicardinalPivot<SymbolicExpression>> args) {
        return (UnicardinalPivot<SymbolicExpression>) new SymbolicAdd(args).close();
    }

    @SafeVarargs
    public static UnicardinalPivot<SymbolicExpression> create(UnicardinalPivot<SymbolicExpression>... args) {
        return (UnicardinalPivot<SymbolicExpression>) new SymbolicAdd(List.of(args)).close();
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected SymbolicAdd(Collection<UnicardinalPivot<SymbolicExpression>> args) {
        super(args);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Add ============================================================================================= */
    protected Add createRawAdd(Collection<? extends UnicardinalPivot<?>> args) {
        return new SymbolicAdd((Collection<UnicardinalPivot<SymbolicExpression>>) args);
    }
}
