package Core.GeoSystem.Points;

import java.util.Arrays;

public abstract class Center extends DefinedPoint {
    public static final String[] inputTypes = new String[] {"Points"};

    public Center(Point ... args) {
        super();
        this.inputs.get("Points").addAll(Arrays.asList(args));
    }

    public String[] getInputTypes() {
        return Center.inputTypes;
    }
}
