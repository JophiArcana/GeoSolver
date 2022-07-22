package core.structure.unicardinal.alg.symbolic.operator;

import core.structure.equalitypivot.EqualityPivot;
import core.structure.unicardinal.Unicardinal;
import core.structure.unicardinal.alg.structure.Add;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;

import java.util.*;

public class SymbolicAdd extends Add implements SymbolicExpression {
    /** SECTION: Factory Methods ==================================================================================== */
    public static EqualityPivot<SymbolicExpression> create(Collection<EqualityPivot<SymbolicExpression>> args) {
        return (EqualityPivot<SymbolicExpression>) new SymbolicAdd(args).close();
    }

    @SafeVarargs
    public static EqualityPivot<SymbolicExpression> create(EqualityPivot<SymbolicExpression>... args) {
        return (EqualityPivot<SymbolicExpression>) new SymbolicAdd(List.of(args)).close();
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected SymbolicAdd(Collection<EqualityPivot<SymbolicExpression>> args) {
        super(args);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Add ============================================================================================= */
    protected Add createRawAdd(Collection<? extends EqualityPivot<? extends Unicardinal>> args) {
        return new SymbolicAdd((Collection<EqualityPivot<SymbolicExpression>>) args);
    }
}
