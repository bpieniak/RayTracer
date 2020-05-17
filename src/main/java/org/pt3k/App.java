package org.pt3k;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
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

    /*

    public void addMouseScrolling(Node node) {
        node.setOnScroll((ScrollEvent event) -> {
            double zoomFactor = 1.05;
            double deltaY = event.getDeltaY();
            if(deltaY < 0) {
                zoomFactor = 2.0 - zoomFactor;
            }
            node.setScaleX(node.getScaleX()*zoomFactor);
            node.setScaleY(node.getScaleY()*zoomFactor);

        });
    } */
}