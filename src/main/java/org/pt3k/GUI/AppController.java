package org.pt3k.GUI;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.pt3k.Camera;
import org.pt3k.MultithreadRenderer;
import org.pt3k.Scene;
import org.pt3k.Vec3;
import org.pt3k.materials.ImageTexture;
import org.pt3k.shapes.hittable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Klasa obslugujaca wszystkie operacje w GUI aplikacji.
 */
public class AppController {

    Scene mainScene, aboutScene;

    @FXML TextField resolutionX;
    @FXML TextField resolutionY;
    @FXML ImageView imageView;
    @FXML TextField samples;
    @FXML TextField depth;
    @FXML VBox mainVBox;
    @FXML HBox HBox;
    @FXML Label statusLabel;
    @FXML TextField FOV;
    @FXML TextField lookFromX;
    @FXML TextField lookFromY;
    @FXML TextField lookFromZ;
    @FXML TextField lookAtX;
    @FXML TextField lookAtY;
    @FXML TextField lookAtZ;
    @FXML TextField tfBackgroundR;
    @FXML TextField tfBackgroundG;
    @FXML TextField tfBackgroundB;
    @FXML ChoiceBox<String> cbSceneSelector;

    int width, height;
    WritableImage image;
    String currScene;
    ArrayList<hittable> currSceneList;

    String[] scenes = new String[]{"Random spheres", "Five spheres", "Cornell box", "Earth"};

    public AppController() { }

    @FXML
    private void initialize() {

        makeTextFieldNumeraticOnly(resolutionX);
        makeTextFieldNumeraticOnly(resolutionY);
        makeTextFieldNumeraticOnly(samples);
        makeTextFieldNumeraticOnly(depth);
        makeTextFieldNumeraticOnly(FOV);
        makeTextFieldNumeraticOnly(lookFromX);
        makeTextFieldNumeraticOnly(lookFromY);
        makeTextFieldNumeraticOnly(lookFromZ);
        makeTextFieldNumeraticOnly(lookAtX);
        makeTextFieldNumeraticOnly(lookAtY);
        makeTextFieldNumeraticOnly(lookAtZ);
        makeTextFieldNumeraticOnly(tfBackgroundR);
        makeTextFieldNumeraticOnly(tfBackgroundG);
        makeTextFieldNumeraticOnly(tfBackgroundB);

        cbSceneSelector.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> adjustCamera(t1));

        for(String s : scenes) {
            cbSceneSelector.getItems().add(s);
        }
        cbSceneSelector.setValue(scenes[0]);
    }

    @FXML
    private void generateImage() throws InterruptedException, IOException {

        width = 200;
        height = 200;
        int nSamples = 10;
        int nDepth = 10;
        int FOVvalue = 20;
        int lookFromXvalue = 13, lookFromYvalue = 2, lookFromZvalue = 3;
        int lookAtXvalue = 0, lookAtYvalue = 0, lookAtZvalue = 0;
        float backgroundR = 0, backgroundG = 0, backgroundB = 0;

        try {
            width = Integer.parseInt(resolutionX.getText().trim());
            height = Integer.parseInt(resolutionY.getText().trim());
            nSamples = Integer.parseInt(samples.getText().trim());
            nDepth = Integer.parseInt(depth.getText().trim());
            FOVvalue = Integer.parseInt(FOV.getText().trim());
            lookFromXvalue = Integer.parseInt(lookFromX.getText().trim());
            lookFromYvalue = Integer.parseInt(lookFromY.getText().trim());
            lookFromZvalue = Integer.parseInt(lookFromZ.getText().trim());
            lookAtXvalue = Integer.parseInt(lookAtX.getText().trim());
            lookAtYvalue = Integer.parseInt(lookAtY.getText().trim());
            lookAtZvalue = Integer.parseInt(lookAtZ.getText().trim());
            backgroundR = Float.parseFloat(tfBackgroundR.getText().trim());
            backgroundG = Float.parseFloat(tfBackgroundG.getText().trim());
            backgroundB = Float.parseFloat(tfBackgroundB.getText().trim());
        } catch (NumberFormatException nfe) {
            System.out.println(nfe.getMessage());
        }

        Camera cam = new Camera(FOVvalue,(float) width/height,
                new Vec3(lookFromXvalue,lookFromYvalue,lookFromZvalue),
                new Vec3(lookAtXvalue,lookAtYvalue,lookAtZvalue),
                new Vec3(0,1,0));

        Vec3 backgroundColor = new Vec3(backgroundR, backgroundG, backgroundB);

        ArrayList<hittable> scene = getScene();


        long start = System.currentTimeMillis();
        byte[] pixels = (new MultithreadRenderer(width,height,nSamples,nDepth,cam,backgroundColor,scene)).render();
        long finish = System.currentTimeMillis();

        image = new WritableImage(width,height);
        PixelWriter pixelWriter = image.getPixelWriter();
        pixelWriter.setPixels(0,0,width,height, PixelFormat.getByteRgbInstance(),pixels,0,width*3);


        imageView.setImage(image);

        //zoom on scroll
        imageView.setOnScroll((ScrollEvent event) -> {
            double zoomFactor = 1.05;
            double deltaY = event.getDeltaY();
            if(deltaY < 0) {
                zoomFactor = 2.0 - zoomFactor;
            }
            imageView.setFitWidth(imageView.getFitWidth()*zoomFactor);
            imageView.setFitHeight(imageView.getFitHeight()*zoomFactor);

        });

        if((HBox.getWidth()-200) > HBox.getHeight()*(width/height)) {
            imageView.setFitHeight(HBox.getHeight());
        } else {
            imageView.setFitWidth(HBox.getWidth()-200);
        }

        statusLabel.setText("Rendered in " + (finish-start)/1000.0 + " secends");
    }

    @FXML
    private void saveImage() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.selectedExtensionFilterProperty();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG", "*.png"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null && image != null) {
            RenderedImage renderedImage = SwingFXUtils.fromFXImage(image, null);
            ImageIO.write(renderedImage, "png", file);
        }
    }

    public void makeTextFieldNumeraticOnly(TextField textField) {
        textField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if(!(newValue.matches("\\.?-?\\d*"))) {
                textField.setText(newValue.replaceAll("[^\\.\\-\\d]", ""));
            }
        });
    }

    public ArrayList<hittable> getScene() throws IOException {

        String selected = cbSceneSelector.getValue();

        if(selected.equals(currScene))
            return currSceneList;

        if(selected.equals(scenes[0])) {
            currSceneList = Scene.randomScene();
            currScene = scenes[0];
        }else if (selected.equals(scenes[1])) {
            currSceneList = Scene.fiveSpheres();
            currScene = scenes[1];
        }
        else if (selected.equals(scenes[2])){
            currSceneList = Scene.cornellBox();
            currScene = scenes[2];
        } else {
            currSceneList = Scene.earthScene();
            currScene = scenes[3];
        }

        return currSceneList;
    }

    void adjustCamera(String value) {

        if(value.equals(scenes[0])) {
            FOV.setText("20");
            lookFromX.setText("13");
            lookFromY.setText("6");
            lookFromZ.setText("6");
            lookAtX.setText("0");
            lookAtY.setText("0");
            lookAtZ.setText("0");
            tfBackgroundR.setText("0");
            tfBackgroundG.setText("0");
            tfBackgroundB.setText("0");
        }else if(value.equals(scenes[1])) {
            FOV.setText("20");
            lookFromX.setText("13");
            lookFromY.setText("10");
            lookFromZ.setText("13");
            lookAtX.setText("0");
            lookAtY.setText("0");
            lookAtZ.setText("0");
            tfBackgroundR.setText("1");
            tfBackgroundG.setText("1");
            tfBackgroundB.setText("1");
        }else if(value.equals(scenes[2])) {
            FOV.setText("40");
            lookFromX.setText("278");
            lookFromY.setText("278");
            lookFromZ.setText("-800");
            lookAtX.setText("278");
            lookAtY.setText("278");
            lookAtZ.setText("0");
            tfBackgroundR.setText("0");
            tfBackgroundG.setText("0");
            tfBackgroundB.setText("0");
        } else if(value.equals(scenes[3])) {
            FOV.setText("20");
            lookFromX.setText("30");
            lookFromY.setText("15");
            lookFromZ.setText("6");
            lookAtX.setText("0");
            lookAtY.setText("0");
            lookAtZ.setText("0");
            tfBackgroundR.setText("0.2");
            tfBackgroundG.setText("0.2");
            tfBackgroundB.setText("0.6");
        }
    }

    @FXML
    void changeScene() throws IOException {

        Stage stage = new Stage();

        URL url = Paths.get("src/main/java/org/pt3k/GUI/About.fxml").toUri().toURL();
        Parent root = FXMLLoader.load(url);

        javafx.scene.Scene aboutScene = new javafx.scene.Scene(root);
        stage.setScene(aboutScene);
        stage.showAndWait();
    }

    @FXML
    void exitApp() {
        Stage stage = (Stage) mainVBox.getScene().getWindow();
        stage.close();
    }
}