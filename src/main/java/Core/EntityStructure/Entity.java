package Core.EntityStructure;

import Core.AlgSystem.UnicardinalRings.*;
import Core.Diagram;
import Core.EntityStructure.UnicardinalStructure.*;
import Core.GeoSystem.Circles.CircleStructure.Circle;
import Core.GeoSystem.Lines.LineStructure.Line;
import Core.GeoSystem.Points.PointStructure.Point;
import Core.Propositions.PropositionStructure.Proposition;
import Core.Utilities.Comparators.*;
import com.google.common.collect.TreeMultiset;

import java.util.*;
import java.util.function.*;

import javafx.util.*;

public interface Entity {
    /** SECTION: Static Data ======================================================================================== */
    HashSet<String> nameSet = new HashSet<>();

    class EntityType<T extends Entity> {
        public final Function<Entity, T> CASTER;
        public final Comparator<T> COMPARATOR;

        public EntityType(Function<Entity, T> caster, Comparator<T> comparator) {
            this.CASTER = caster;
            this.COMPARATOR = comparator;
        }

        public TreeMultiset<T> create() {
            return TreeMultiset.create(this.COMPARATOR);
        }

        public int compare(Entity e1, Entity e2) {
            return this.COMPARATOR.compare(this.CASTER.apply(e1), this.CASTER.apply(e2));
        }
    }

    enum FundamentalType {
        SYMBOLIC(new EntityType<>(o -> (Expression<Symbolic>) o, new UnicardinalComparator<>())),
        DIRECTED(new EntityType<>(o -> (Expression<DirectedAngle>) o, new UnicardinalComparator<>())),
        POINT(new EntityType<>(Point.class::cast, new MulticardinalComparator<>(Point::compareConstants))),
        LINE(new EntityType<>(Line.class::cast, new MulticardinalComparator<>(Line::compareConstants))),
        CIRCLE(new EntityType<>(Circle.class::cast, new MulticardinalComparator<>(Circle::compareConstants)));

        public final EntityType<?> CLASS;

        FundamentalType(EntityType<?> type) {
            this.CLASS = type;
        }
    }

    interface InputType {}
    interface ExpressionType {}

    /** SECTION: Constructor Setup ================================================================================== */
    default void inputSetup() {
        if (this.getInputTypes() != null) {
            HashMap<InputType, TreeMultiset<? extends Entity>> inputs = this.getInputs();
            for (FundamentalType type : FundamentalType.values()) {
                EntityType<?> entityType = type.CLASS;
                InputType[] inputArray = this.getInputTypes()[type.ordinal()];
                if (inputArray != null) {
                    for (InputType inputType : this.getInputTypes()[type.ordinal()]) {
                        inputs.put(inputType, entityType.create());
                    }
                }
            }
        }
    }

    /** SECTION: Interface ========================================================================================== */
    Entity simplify();
    Unicardinal expression(ExpressionType varType);
    ArrayList<Expression<Symbolic>> symbolic();

    Diagram getDiagram();

    int getNaturalDegreesOfFreedom();
    int getConstrainedDegreesOfFreedom();
    HashSet<Proposition> getConstraints();

    HashMap<InputType, TreeMultiset<? extends Entity>> getInputs();
    InputType[][] getInputTypes();

    /** SECTION: Functions ========================================================================================== */
    default <T extends Entity> void substitute(InputType inputType, Pair<T, T> pair) {
        TreeMultiset<T> elements = (TreeMultiset<T>) this.getInputs().get(inputType);
        if (elements.remove(pair.getKey())) {
            elements.add(pair.getValue());
        }
    }
}

