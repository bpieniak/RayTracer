package org.pt3k.GUI;

import com.sun.javafx.logging.PlatformLogger;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
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
import org.pt3k.MultithreadRenderer;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class AppController {


    @FXML TextField resolutionX;
    @FXML TextField resolutionY;
    @FXML ImageView imageView;
    @FXML TextField samples;
    @FXML TextField depth;
    @FXML VBox mainVBox;
    @FXML HBox HBox;
    @FXML Label statusLabel;

    int width, height;
    WritableImage image;

    public AppController() {

    }

    @FXML
    private void initialize() {
        makeTextFieldNumeraticOnly(resolutionX);
        makeTextFieldNumeraticOnly(resolutionY);
        makeTextFieldNumeraticOnly(samples);
        makeTextFieldNumeraticOnly(depth);
    }

    @FXML
    private void generateImage() throws InterruptedException {

        width = 200;
        height = 200;
        int nSamples = 10;
        int nDepth = 10;

        try {
            width = Integer.parseInt(resolutionX.getText().trim());
            height = Integer.parseInt(resolutionY.getText().trim());
            nSamples = Integer.parseInt(samples.getText().trim());
            nDepth = Integer.parseInt(depth.getText().trim());
        } catch (NumberFormatException nfe) {
            System.out.println(nfe.getMessage());
        }

        long start = System.currentTimeMillis();
        byte[] pixels = (new MultithreadRenderer(width,height,nSamples,nDepth)).render();
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

        if((HBox.getWidth()-145) > HBox.getHeight()*(width/height)) {
            imageView.setFitHeight(HBox.getHeight());
            System.out.println(HBox.getHeight());
        } else {
            System.out.println(HBox.getWidth());
            imageView.setFitWidth(HBox.getWidth()-145);
        }

        statusLabel.setText("Wygenerowano w " + (finish-start)/1000.0 + " sekund");
    }

    @FXML
    public void saveImage() throws IOException {
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
            if(!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

}
