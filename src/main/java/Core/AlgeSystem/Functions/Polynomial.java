package Core.AlgeSystem.Functions;

import Core.AlgeSystem.Constant;
import Core.Utilities.ASEngine;

public class Polynomial extends Add {
    public Polynomial(Constant c, Monomial ... args) {
        super(args);
        this.constant = (Constant) ASEngine.add(this.constant, c);
    }
}
