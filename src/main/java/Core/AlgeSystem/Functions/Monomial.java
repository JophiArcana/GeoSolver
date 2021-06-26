package Core.AlgeSystem.Functions;

import Core.AlgeSystem.*;
import Core.EntityTypes.Entity;
import Core.Utilities.AlgeEngine;
import com.google.common.collect.TreeMultiset;

import java.util.*;

public class Monomial extends Mul {
    public Monomial(Constant c, Map<Univariate, Integer> m) {
        TreeMultiset<Entity> inputTerms = this.inputs.get("Terms");
        for (Map.Entry<Univariate, Integer> entry : m.entrySet()) {
            if (entry.getValue() != 0) {
                inputTerms.add(AlgeEngine.pow(entry.getKey(), entry.getValue()));
                this.terms.put(entry.getKey(), new Complex(entry.getValue(), 0));
            }
        }
        this.inputs.get("Constant").add(c);
        this.constant = c;
    }

    public Factorization normalize() {
        Expression simplified = (Expression) this.simplify();
        if (simplified instanceof Monomial) {
            return new Factorization(this.constant, this.terms);
        } else {
            return simplified.normalize();
        }
    }
}
