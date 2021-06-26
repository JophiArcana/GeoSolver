
import Core.AlgeSystem.*;
import Core.AlgeSystem.Functions.*;
import Core.GeoSystem.Points.*;
import Core.GeoSystem.Points.Functions.*;

import static Core.Utilities.AlgeEngine.*;
import static Core.AlgeSystem.Constant.*;

import edu.jas.arith.*;
import edu.jas.poly.*;

public class Test {
    public static void main(String[] args) {
        Univariate x = new Univariate("x");
        Univariate y = new Univariate("y");
        Univariate z = new Univariate("z");
        Expression expr1 = mul(x, y, y, x, log(x));
        Expression expr2 = pow(x, 2);
        Expression expr3 = exp(x);
        Expression expr4 = exp(mul(x, 2));
        Expression p = pow(mul(x, I), complex(0, -2));
        Expression q = add(mul(I, pow(x, 3)), x, I);
        Expression r = mul(x, I, -1);
        System.out.println(add(expr1, expr2));
        Expression k = ((Log) log(q).getInputs().get("Terms").firstEntry().getElement()).input;
        System.out.println(exp(add(mul(3, log(x)), mul(2, log(y)), 3)));
        System.out.println(add(imaginary(log(q)), mul(0.5, log(add(pow(real(k), 2), pow(imaginary(k), 2))))));
        System.out.println(log(q));
        Phantom pointP = new Phantom("P");
        Phantom pointQ = new Phantom("Q");
        Phantom pointR = new Phantom("R");
        Centroid m = new Centroid(pointP, pointQ);
        Centroid c = new Centroid(pointP, m);
        Circumcenter o = new Circumcenter(pointP, pointQ, pointR);
        System.out.println(c.expression());
        System.out.println(c);
        System.out.println(o.expression());
        String[] vars = new String[] {"z", "y", "x", "w"};
        GenPolynomialRing<BigComplex> ring = new GenPolynomialRing<>(new BigComplex(), 4, vars);
        System.out.println(ring.parse("x ^ 2 + 2 * x * y + y^2"));
    }
}
