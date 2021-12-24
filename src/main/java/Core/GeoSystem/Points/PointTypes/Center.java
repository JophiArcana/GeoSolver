package Core.GeoSystem.Points.PointTypes;

import java.util.Arrays;

public abstract class Center extends DefinedPoint {
    /** SECTION: Static Data ======================================================================================== */
    public enum Parameter implements InputType {
        POINTS
    }
    public static final InputType[] inputTypes = {Parameter.POINTS};

    /** SECTION: Abstract Constructor =============================================================================== */
    public Center(String n, Point ... args) {
        super(n);
        this.inputs.get(Parameter.POINTS).addAll(Arrays.asList(args));
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public InputType[] getInputTypes() {
        return Center.inputTypes;
    }
}
