package core.structure.unicardinal.alg.symbolic;

import core.structure.unicardinal.Constant;

public abstract class SymbolicConstant extends Constant implements SymbolicExpression{
    /** SECTION: Abstract Constructor =============================================================================== */
    protected SymbolicConstant(double value) {
        super(value);
    }
}
