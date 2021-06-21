package Core.GeoSystem.Points;

import Core.EntityTypes.DefinedEntity;

import java.util.Arrays;

public abstract class Center extends DefinedEntity implements Point {
    public static final String[] inputTypes = new String[] {"Points"};

    public Center(Point ... args) {
        super();
        this.inputs.get("Points").addAll(Arrays.asList(args));
    }

    public String[] getInputTypes() {
        return Center.inputTypes;
    }
}
