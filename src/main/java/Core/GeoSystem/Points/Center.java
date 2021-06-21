package Core.GeoSystem.Points;

import Core.EntityTypes.DefinedEntity;

import java.util.Arrays;

public abstract class Center extends DefinedEntity implements Point {
    public static final String[] inputTypes = new String[] {"Vertices"};

    public Center(Point ... args) {
        super();
        this.inputs.get("Vertices").addAll(Arrays.asList(args));
    }

    public String[] getInputTypes() {
        return Center.inputTypes;
    }
}
