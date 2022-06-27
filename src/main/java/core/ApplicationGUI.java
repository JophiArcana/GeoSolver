package core;

import core.structure.multicardinal.geo.point.structure.PointVariable;
import core.structure.multicardinal.geo.triangle.Triangle;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Random;

public class ApplicationGUI extends Application {
    public static Diagram DIAGRAM;

    public void start(Stage stage) {
        Scene scene = new Scene(ApplicationGUI.DIAGRAM.root, 800, 800);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Random r = new Random();
        Diagram d = new Diagram();
        PointVariable A = d.createPointVariable("A", r.nextInt(100, 700), r.nextInt(100, 700));
        PointVariable B = d.createPointVariable("B", r.nextInt(100, 700), r.nextInt(100, 700));
        PointVariable C = d.createPointVariable("C", r.nextInt(100, 700), r.nextInt(100, 700));
        // Triangle.Circumcenter O = d.createCircumcenter("O", A, B, C);
        Triangle.Circumcircle L = d.createCircumcircle("L", A, B, C);
        ApplicationGUI.DIAGRAM = d;
        Application.launch(ApplicationGUI.class, args);
    }
}
