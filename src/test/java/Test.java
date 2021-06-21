
import Core.AlgeSystem.*;
import Core.AlgeSystem.Functions.*;
import Core.GeoSystem.Points.Phantom;
import Core.Utilities.Utils;

import static Core.Utilities.ASEngine.*;

public class Test {
    public static void main(String[] args) {
        Symbol x = new Symbol("x");
        Symbol y = new Symbol("y");
        Expression expr1 = mul(x, y, y, x, log(x));
        Expression expr2 = pow(x, 2);
        Expression expr3 = exp(x);
        Expression expr4 = exp(mul(x, 2));
        Complex c1 = new Complex(1, 13);
        Complex c2 = new Complex(3, 19);
        Expression p = pow(mul(x, Constant.I), new Complex(0, -2));
        Expression q = add(mul(Constant.I, pow(x, 3)), x, Constant.I);
        Expression r = mul(x, Constant.I, -1);
        // System.out.println(add(expr1, expr2));
        Expression re = pow(real(q), 2);
        Expression im = pow(imaginary(q), 2);
        Expression k = ((Log) log(q).getInputs().get("Terms").firstEntry().getElement()).input;
        System.out.println(exp(add(mul(3, log(x)), mul(2, log(y)), 3)));
        System.out.println(add(imaginary(log(q)), mul(0.5, log(add(pow(real(k), 2), pow(imaginary(k), 2))))));
        System.out.println(log(q));
        Phantom point = new Phantom("P");
        Symbol px = new Symbol("P\u1D6A");
        Symbol py = new Symbol("P\u1D67");
    }
}
