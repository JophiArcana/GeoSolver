package Core.GeoSystem.Points.PointStructure;

import Core.AlgSystem.UnicardinalRings.Symbolic;
import Core.EntityStructure.UnicardinalStructure.Expression;
import Core.EntityStructure.Entity;
import Core.EntityStructure.MulticardinalStructure.DefinedMulticardinal;
import Core.Utilities.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

public abstract class DefinedPoint extends DefinedMulticardinal implements Point {
    /** SECTION: Instance Variables ================================================================================= */
    public ArrayList<Expression<Symbolic>> symbolic;

    /** SECTION: Abstract Constructor =============================================================================== */
    public DefinedPoint(String n) {
        super(n);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public ArrayList<Expression<Symbolic>> symbolic() {
        if (this.symbolic == null) {
            HashMap<InputType, ArrayList<ArrayList<Expression<Symbolic>>>> args = new HashMap<>();
            for (InputType inputType : this.getInputTypes()) {
                args.put(inputType, Utils.map(this.inputs.get(inputType), Entity::symbolic));
            }
            this.symbolic = this.getFormula().apply(args);
        }
        return this.symbolic;
    }

    /** SECTION: Interface ========================================================================================== */
    public abstract Function<HashMap<InputType, ArrayList<ArrayList<Expression<Symbolic>>>>, ArrayList<Expression<Symbolic>>> getFormula();
}
