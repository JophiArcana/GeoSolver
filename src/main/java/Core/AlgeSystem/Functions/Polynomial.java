package Core.AlgeSystem.Functions;

import Core.AlgeSystem.Constant;
import Core.Utilities.AlgeEngine;

public class Polynomial extends Add {
    public Polynomial(Constant c, Monomial ... args) {
        super(args);
        this.constant = (Constant) AlgeEngine.add(this.constant, c);
    }
}
