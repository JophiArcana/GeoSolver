package Core.GeoSystem.Lines;

import Core.AlgeSystem.UnicardinalTypes.Unicardinal;
import Core.EntityTypes.Entity;
import Core.GeoSystem.Lines.LineTypes.Line;
import Core.GeoSystem.MulticardinalTypes.DefinedMulticardinal;
import Core.GeoSystem.Points.PointTypes.Point;

import java.util.*;

public class Connect extends DefinedMulticardinal implements Line {
    public static final String[] inputTypes = {"Points"};

    public Entity create(HashMap<String, ArrayList<Entity>> args) {
        return new Connect((Point) args.get("Points").get(0), (Point) args.get("Points").get(1));
    }

    public Point a, b;

    public Connect(Point a, Point b) {
        super(a.getName() + b.getName());
    }

    public Entity simplify() {
        return this;
    }

    /** TODO: Fix linear representation */
    protected ArrayList<Unicardinal> getExpression() {
        return null;
    }

    public String[] getInputTypes() {
        return Connect.inputTypes;
    }
}
