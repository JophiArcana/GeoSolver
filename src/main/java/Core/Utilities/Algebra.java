package Core.Utilities;

import Core.AlgSystem.Constants.Complex;
import Core.AlgSystem.Constants.Infinity;
import Core.AlgSystem.UnicardinalTypes.Constant;
import Core.AlgSystem.UnicardinalTypes.Expression;

public interface Algebra<T extends Expression<T>> {
    enum Constants {
        ZERO,
        ONE,
        NONE,
        I,
        INFINITY,
        E,
        PI
    }

    default Constant<T> get(Constant.Constants c) {
        return switch (c) {
            case ZERO -> Complex.create(0, 0, this.getType());
            case ONE -> Complex.create(1, 0, this.getType());
            case NONE -> Complex.create(-1, 0, this.getType());
            case I -> Complex.create(0, 1, this.getType());
            case INFINITY -> Infinity.create(this.getType());
            case E -> Complex.create(Math.E, 0, this.getType());
            case PI -> Complex.create(Math.PI, 0, this.getType());
        };
    }

    Class<T> getType();
}
