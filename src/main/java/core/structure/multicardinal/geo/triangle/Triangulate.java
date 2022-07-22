package core.structure.multicardinal.geo.triangle;

import core.structure.Entity;
import core.structure.multicardinal.Multicardinal;
import core.structure.multicardinal.geo.point.structure.Point;

import java.util.List;

public interface Triangulate extends Multicardinal {
    InputType<Point> VERTICES = new InputType<>();
    List<InputType<?>> inputTypes = List.of(Triangulate.VERTICES);

    default List<Entity.InputType<?>> getInputTypes() {
        return Triangle.inputTypes;
    }

}
