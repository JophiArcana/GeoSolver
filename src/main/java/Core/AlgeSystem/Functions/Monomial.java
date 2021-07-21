package Core.AlgeSystem.Functions;

import Core.AlgeSystem.Constants.*;
import Core.AlgeSystem.UnicardinalTypes.*;
import Core.EntityTypes.Entity;
import com.google.common.collect.TreeMultiset;

import java.util.*;

public class Monomial<T extends Expression<T>> extends Mul<T> {
    public Monomial(Constant<T> c, Map<Univariate<T>, Integer> m, Class<T> type) {
        super(new ArrayList<>(), type);
        TreeMultiset<Entity> inputTerms = this.inputs.get("Terms");
        for (Map.Entry<Univariate<T>, Integer> entry : m.entrySet()) {
            if (entry.getValue() != 0) {
                inputTerms.add(ENGINE.pow(entry.getKey(), entry.getValue()));
                this.terms.put(entry.getKey(), new Complex<>(entry.getValue(), 0, TYPE));
            }
        }
        this.inputs.get("Constant").add(c);
        this.constant = c;
    }

    public Factorization<T> normalize() {
        return new Factorization<>(this.constant, this.terms, TYPE);
    }
}
