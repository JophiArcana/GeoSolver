package Core.AlgeSystem.Functions;

import Core.AlgeSystem.Constants.*;
import Core.AlgeSystem.ExpressionTypes.*;
import Core.EntityTypes.Entity;
import Core.Utilities.AlgeEngine;
import com.google.common.collect.TreeMultiset;

import java.util.*;

public class Monomial extends Mul {
    public Monomial(Constant c, Map<Symbol, Integer> m) {
        TreeMultiset<Entity> inputTerms = this.inputs.get("Terms");
        for (Map.Entry<Symbol, Integer> entry : m.entrySet()) {
            if (entry.getValue() != 0) {
                inputTerms.add(AlgeEngine.pow(entry.getKey(), entry.getValue()));
                this.terms.put(entry.getKey(), new Complex(entry.getValue(), 0));
            }
        }
        this.inputs.get("Constant").add(c);
        this.constant = c;
    }

    public Factorization normalize() {
        return new Factorization(this.constant, this.terms);
    }
}
