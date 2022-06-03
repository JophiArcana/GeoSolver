package Core;

import Core.EntityStructure.Entity;

import java.util.HashSet;

public class Diagram {
    public static Diagram currentDiagram;

    public HashSet<String> nameSet = new HashSet<>();

    public <T extends Entity> create(Class<T> cls, ) {
        return cls.getDeclaredMethods()
    }
}


