package core.structure.unicardinal;

import core.structure.Entity;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.ArrayList;

public interface Unicardinal extends Entity, ObservableValue<Number> {
    /** SECTION: Interface ========================================================================================== */
    double value();
    void computeValue();

    ArrayList<ChangeListener<? super Number>> getChangeListeners();
    ArrayList<InvalidationListener> getInvalidationListeners();

    @Override
    default void addListener(ChangeListener<? super Number> changeListener) {
        this.getChangeListeners().add(changeListener);
    }

    @Override
    default void addListener(InvalidationListener invalidationListener) {
        this.getInvalidationListeners().add(invalidationListener);
    }

    @Override
    default void removeListener(ChangeListener<? super Number> changeListener) {
        this.getChangeListeners().remove(changeListener);
    }

    @Override
    default void removeListener(InvalidationListener invalidationListener) {
        this.getInvalidationListeners().remove(invalidationListener);
    }

    @Override
    default Double getValue() {
        return this.value();
    }
}
