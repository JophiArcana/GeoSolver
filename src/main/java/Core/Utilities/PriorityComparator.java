package Core.Utilities;

import Core.AlgSystem.UnicardinalRings.*;
import Core.AlgSystem.UnicardinalTypes.*;
import Core.EntityTypes.*;
import Core.GeoSystem.Lines.LineTypes.Axis;
import Core.GeoSystem.Points.PointTypes.Coordinate;
import com.google.common.collect.TreeMultiset;
import javafx.util.Pair;

import java.util.*;

public class PriorityComparator implements Comparator<Entity> {
    public int compare(Entity e1, Entity e2) {
        if (e1 == e2) {
            return 0;
        } else {
            if (e1 instanceof Expression) {
                if (((Expression<?>) e1).getType() == Symbolic.class) {
                    final AlgEngine<Symbolic> ENGINE = Utils.getEngine(Symbolic.class);

                    Expression<Symbolic> expr1 = (Expression<Symbolic>) e1, expr2 = (Expression<Symbolic>) e2;
                    if (expr1 instanceof Constant<Symbolic> c1 && expr2 instanceof Constant<Symbolic> c2) {
                        return Constant.compare(c1, c2);
                    } else {
                        Pair<Constant<Symbolic>, Double> o1 = ENGINE.orderOfGrowth(expr1), o2 = ENGINE.orderOfGrowth(expr2);
                        if (Double.compare(o1.getValue(), o2.getValue()) != 0) {
                            return -Double.compare(o1.getValue(), o2.getValue());
                        } else if (Constant.compare(o1.getKey(), o2.getKey()) != 0) {
                            return -Constant.compare(o1.getKey(), o2.getKey());
                        }
                    }
                } else {
                    final AlgEngine<DirectedAngle> ENGINE = Utils.getEngine(DirectedAngle.class);

                    Expression<DirectedAngle> expr1 = (Expression<DirectedAngle>) e1, expr2 = (Expression<DirectedAngle>) e2;
                    if (expr1 instanceof Constant<DirectedAngle> c1 && expr2 instanceof Constant<DirectedAngle> c2) {
                        return Constant.compare(c1, c2);
                    } else {
                        Pair<Constant<DirectedAngle>, Double> o1 = ENGINE.orderOfGrowth(expr1), o2 = ENGINE.orderOfGrowth(expr2);
                        if (Double.compare(o1.getValue(), o2.getValue()) != 0) {
                            return -Double.compare(o1.getValue(), o2.getValue());
                        } else if (Constant.compare(o1.getKey(), o2.getKey()) != 0) {
                            return -Constant.compare(o1.getKey(), o2.getKey());
                        }
                    }
                }
            }
            if (e1.getClass() != e2.getClass()) {
                return -(Utils.classCode(e1) - Utils.classCode(e2));
            } else if (e1 instanceof Coordinate) {
                return -Coordinate.compare((Coordinate) e1, (Coordinate) e2);
            } else if (e1 instanceof Axis) {
                return -Axis.compare((Axis) e1, (Axis) e2);
            } else if (e1 instanceof Mutable) {
                return ((Mutable) e1).name.compareTo(((Mutable) e2).name);
            } else {
                HashMap<Entity.InputType, TreeMultiset<Entity>> e1Inputs = e1.getInputs();
                HashMap<Entity.InputType, TreeMultiset<Entity>> e2Inputs = e2.getInputs();
                for (Entity.InputType inputType : e1.getInputTypes()) {
                    Iterator<Entity> e1Descending = e1Inputs.get(inputType).iterator();
                    Iterator<Entity> e2Descending = e2Inputs.get(inputType).iterator();
                    while (e1Descending.hasNext() && e2Descending.hasNext()) {
                        int inputComparison = compare(e1Descending.next(), e2Descending.next());
                        if (inputComparison != 0) {
                            return inputComparison;
                        }
                    }
                    if (e1Descending.hasNext()) {
                        return -1;
                    } else if (e2Descending.hasNext()) {
                        return 1;
                    }
                }
                return 0;
            }
        }
    }
}