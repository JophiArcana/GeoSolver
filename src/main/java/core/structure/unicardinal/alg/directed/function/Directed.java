package core.structure.unicardinal.alg.directed.function;

import core.structure.multicardinal.geo.line.structure.Line;
import core.structure.unicardinal.alg.DefinedExpression;
import core.structure.unicardinal.alg.directed.DirectedExpression;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;
import core.structure.unicardinal.alg.symbolic.operator.SymbolicScale;
import core.util.*;

import java.util.*;

public class Directed extends DefinedExpression implements DirectedExpression {
    /** SECTION: Static Data ======================================================================================== */
    public static final InputType<Line> LINE = new InputType<>(Line.class, Utils.MULTICARDINAL_COMPARATOR);

    public static final List<InputType<?>> inputTypes = List.of(Directed.LINE);

    /** SECTION: Instance Variables ================================================================================= */
    public Line line;

    /** SECTION: Factory Methods ==================================================================================== */
    public static Directed create(Line line) {
        return new Directed(line);
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected Directed(Line line) {
        super();
        this.line = line;
        this.getInputs(Directed.LINE).add(this.line);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public void computeValue() {
        List<SymbolicExpression> lineExpression = this.line.symbolic();
        this.value.set(Math.atan2(lineExpression.get(1).doubleValue(), lineExpression.get(0).doubleValue()));
    }

    public List<SymbolicExpression> symbolic() {
        List<SymbolicExpression> dualExpression = this.line.pointDual().symbolic();
        return List.of(SymbolicScale.create(-1, Utils.ENGINE.div(dualExpression.get(0), dualExpression.get(1))));
    }

    public List<InputType<?>> getInputTypes() {
        return Directed.inputTypes;
    }

    public DirectedExpression expand() {
        return this;
    }

    public DirectedExpression close() {
        return this;
    }

    public int getDegree() {
        return 0;
    }
}
