package core.structure.unicardinal.alg.directed.function;

import core.Diagram;
import core.Propositions.equalitypivot.EqualityPivot;
import core.Propositions.equalitypivot.multicardinal.MulticardinalPivot;
import core.Propositions.equalitypivot.unicardinal.UnicardinalPivot;
import core.structure.multicardinal.geo.line.structure.Line;
import core.structure.unicardinal.DefinedUnicardinal;
import core.structure.unicardinal.Unicardinal;
import core.structure.unicardinal.alg.directed.DirectedExpression;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;
import core.structure.unicardinal.alg.symbolic.operator.*;

import java.util.*;

public class Directed extends DefinedUnicardinal implements DirectedExpression {
    /** SECTION: Static Data ======================================================================================== */
    public static final MulticardinalInputType<Line> LINE = new MulticardinalInputType<>();

    public static final List<InputType<?>> inputTypes = List.of(Directed.LINE);

    /** SECTION: Instance Variables ================================================================================= */
    public MulticardinalPivot<Line> line;

    /** SECTION: Factory Methods ==================================================================================== */
    public static UnicardinalPivot<DirectedExpression> create(MulticardinalPivot<Line> line) {
        return new Directed(line).close();
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected Directed(MulticardinalPivot<Line> line) {
        super();
        this.line = line;
        this.getInputs(Directed.LINE).add(this.line);

        this.line.element().symbolic().forEach(arg -> Unicardinal.createComputationalEdge(this, arg));
        line.reverseDependencies().add(this);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public List<UnicardinalPivot<SymbolicExpression>> symbolic() {
        List<UnicardinalPivot<SymbolicExpression>> dualExpression = this.line.element().pointDual().symbolic();
        return List.of(SymbolicScale.create(-1, SymbolicMul.create(
                dualExpression.get(0),
                SymbolicPow.create(dualExpression.get(1), -1)
        )));
    }

    public void updateLocalVariables(EqualityPivot<?> consumedPivot, EqualityPivot<?> consumerPivot) {
        this.line = (MulticardinalPivot<Line>) consumerPivot;    // Only one local variable that can possibly hold consumedPivot
    }

    public List<InputType<?>> getInputTypes() {
        return Directed.inputTypes;
    }

    /** SUBSECTION: Unicardinal ===================================================================================== */
    public void computeValue() {
        List<UnicardinalPivot<SymbolicExpression>> lineExpression = this.line.element().symbolic();
        this.value.set(Math.atan2(lineExpression.get(1).doubleValue(), lineExpression.get(0).doubleValue()));
    }

    public UnicardinalPivot<DirectedExpression> expand() {
        return (UnicardinalPivot<DirectedExpression>) this.equalityPivot;
    }

    public UnicardinalPivot<DirectedExpression> close() {
        return Diagram.retrieve(this);
    }

    public int getDegree() {
        return 0;
    }
}
