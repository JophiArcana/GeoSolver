package Core.GeoSystem.Lines;

import Core.AlgSystem.UnicardinalTypes.Unicardinal;
import Core.EntityTypes.Entity;
import Core.GeoSystem.Lines.LineTypes.Line;
import Core.GeoSystem.MulticardinalTypes.DefinedMulticardinal;
import Core.GeoSystem.Points.PointTypes.Point;

import java.util.*;

public class Connect extends DefinedMulticardinal implements Line {
    public enum Parameter implements InputType {
        POINTS
    }
    public static final InputType[] inputTypes = {Parameter.POINTS};

    public Entity createEntity(HashMap<InputType, ArrayList<Entity>> args) {
        return new Connect((Point) args.get(Parameter.POINTS).get(0), (Point) args.get(Parameter.POINTS).get(1));
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

    public InputType[] getInputTypes() {
        return Connect.inputTypes;
    }
}
