package core.Propositions.equalitypivot.multicardinal;

import core.Propositions.equalitypivot.DefinedPivot;
import core.structure.multicardinal.Multicardinal;

public class UnlockedMulticardinalPivot<T extends Multicardinal> extends DefinedPivot<T> implements MulticardinalPivot<T> {
    public static <T extends Multicardinal> UnlockedMulticardinalPivot<T> of(T element) {
        return new UnlockedMulticardinalPivot<>(element);
    }

    protected UnlockedMulticardinalPivot(T element) {
        super(element);
    }
}
