package core.structure.unicardinal.alg.directed.function;

import core.Diagram;
import core.structure.equalitypivot.*;
import core.structure.multicardinal.geo.line.structure.Line;
import core.structure.unicardinal.DefinedUnicardinal;
import core.structure.unicardinal.Unicardinal;
import core.structure.unicardinal.alg.directed.DirectedExpression;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;
import core.structure.unicardinal.alg.symbolic.operator.*;

import java.util.*;

public class Directed extends DefinedUnicardinal implements DirectedExpression {
    /** SECTION: Static Data ======================================================================================== */
    public static final InputType<Line> LINE = new InputType<>();

    public static final List<InputType<?>> inputTypes = List.of(Directed.LINE);

    /** SECTION: Instance Variables ================================================================================= */
    public EqualityPivot<Line> line;

    /** SECTION: Factory Methods ==================================================================================== */
    public static UnicardinalPivot<DirectedExpression> create(MulticardinalPivot<Line> line) {
        return (UnicardinalPivot<DirectedExpression>) new Directed(line).close();
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected Directed(MulticardinalPivot<Line> line) {
        super();
        this.line = line;
        this.getInputs(Directed.LINE).add(this.line);

        this.line.simplestElement.symbolic().forEach(arg -> Unicardinal.createComputationalEdge(this, arg));
        line.reverseDependencies.add(this);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public List<UnicardinalPivot<SymbolicExpression>> symbolic() {
        List<UnicardinalPivot<SymbolicExpression>> dualExpression = this.line.simplestElement.pointDual().symbolic();
        return List.of(SymbolicScale.create(-1, SymbolicMul.create(
                dualExpression.get(0),
                SymbolicPow.create(dualExpression.get(1), -1)
        )));
    }

    public void updateLocalVariables(EqualityPivot<?> consumedPivot, EqualityPivot<?> consumerPivot) {
        this.line = (MulticardinalPivot<Line>) consumerPivot;   // Only one local variable that can possibly hold consumedPivot
    }

    public List<InputType<?>> getInputTypes() {
        return Directed.inputTypes;
    }

    /** SUBSECTION: Unicardinal ===================================================================================== */
    public void computeValue() {
        List<UnicardinalPivot<SymbolicExpression>> lineExpression = this.line.simplestElement.symbolic();
        this.value.set(Math.atan2(lineExpression.get(1).doubleValue(), lineExpression.get(0).doubleValue()));
    }

    public UnicardinalPivot<?> expand() {
        return (UnicardinalPivot<?>) this.equalityPivot;
    }

    public UnicardinalPivot<?> close() {
        if (this.equalityPivot == null) {
            return Diagram.retrieve(this);
        } else {
            return (UnicardinalPivot<?>) this.equalityPivot;
        }
    }

    public int getDegree() {
        return 0;
    }
}
