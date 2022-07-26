package core.Propositions.equalitypivot.unicardinal;

import core.Propositions.equalitypivot.DefinedPivot;
import core.structure.unicardinal.Unicardinal;

import java.util.HashSet;

public class UnlockedUnicardinalPivot<T extends Unicardinal> extends DefinedPivot<T> implements UnicardinalPivot<T> {
    public static <T extends Unicardinal> UnlockedUnicardinalPivot<T> of(T element) {
        return new UnlockedUnicardinalPivot<>(element);
    }

    public final HashSet<Unicardinal> reverseComputationalDependencies = new HashSet<>();

    protected UnlockedUnicardinalPivot(T element) {
        super(element);
    }

    public HashSet<Unicardinal> reverseComputationalDependencies() {
        return this.reverseComputationalDependencies;
    }
}
