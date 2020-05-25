package org.pt3k;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;


public class App extends Application {

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {

        URL url = Paths.get("src/main/java/org/pt3k/Interface.fxml").toUri().toURL();
        Parent root = FXMLLoader.load(url);

        Scene scene = new Scene(root, 800, 600);

        stage.setTitle("Ray Tracer");
        stage.setScene(scene);
        stage.show();
    }
}