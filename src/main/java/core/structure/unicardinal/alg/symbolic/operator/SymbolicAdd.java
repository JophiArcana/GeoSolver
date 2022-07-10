package core.structure.unicardinal.alg.symbolic.operator;

import core.structure.unicardinal.alg.structure.Add;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;
import core.structure.unicardinal.alg.Expression;

import java.util.*;

public class SymbolicAdd extends Add implements SymbolicExpression {
    /** SECTION: Factory Methods ==================================================================================== */
    public static SymbolicExpression create(Collection<SymbolicExpression> args) {
        return (SymbolicExpression) new SymbolicAdd(args).close();
    }

    public static SymbolicExpression create(SymbolicExpression... args) {
        return (SymbolicExpression) new SymbolicAdd(List.of(args)).close();
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected SymbolicAdd(Collection<SymbolicExpression> args) {
        super(args);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Add ============================================================================================= */
    protected Add createRawAdd(Collection<? extends Expression> args) {
        return new SymbolicAdd((Collection<SymbolicExpression>) args);
    }
}
