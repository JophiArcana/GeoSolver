package Core.GeoSystem.Points.PointTypes;

import Core.GeoSystem.MultiCardinalTypes.DefinedMultiCardinal;

import java.util.Arrays;

public abstract class Center extends DefinedMultiCardinal implements Point {
    public static final String[] inputTypes = new String[] {"Points"};

    public Center(String n, Point ... args) {
        super(n);
        this.inputs.get("Points").addAll(Arrays.asList(args));
    }

    public String[] getInputTypes() {
        return Center.inputTypes;
    }
}
