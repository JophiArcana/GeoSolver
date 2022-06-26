import core.structure.multicardinal.geo.point.structure.PointVariable;

import java.util.*;

public class HashSetTest {
    public static void main(String[] args) {
        PointVariable p = PointVariable.create("P");
        PointVariable p2 = PointVariable.create("P");
        HashSet<PointVariable> hashSet = new HashSet<>();
        TreeSet<PointVariable> treeSet = new TreeSet<>();
        hashSet.add(p);
        hashSet.add(p2);
        treeSet.add(p);
        treeSet.add(p2);
        System.out.println(hashSet);
        System.out.println(treeSet);
        // System.out.println(p.equals(p2));
    }
}
