package Core.AlgeSystem.Functions;

import Core.AlgeSystem.UnicardinalTypes.*;
import Core.Utilities.Utils;

public class Polynomial<T extends Expression<T>> extends Add<T> {
    public Polynomial(Constant<T> c, Iterable<Monomial<T>> args, Class<T> type) {
        super(Utils.cast(args), type);
        this.constant = (Constant<T>) ENGINE.add(this.constant, c);
    }
}
