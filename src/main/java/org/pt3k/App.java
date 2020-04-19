package org.pt3k;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javax.swing.*;


public class App extends Application implements EventHandler<ActionEvent> {

    private static Renderer renderer;

    GridPane grid;

    Button button;
    PixelWriter pixelWriter;
    ImageView imageView;

    TextField resolutionX;
    TextField resolutionY;
    TextField numberOfSamples;
    TextField depth;

    public static int width = 1200;
    public static int height = width/2;
    public int imageWidth;
    public int imageHeight;
    public int samples;
    public int maxDepth;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {

        stage.setMinWidth(300);
        stage.setTitle("Ray Tracer");



        //Grid
        grid = new GridPane();
        //grid.setGridLinesVisible(true);

        ColumnConstraints column0 = new ColumnConstraints();
        column0.setHalignment(HPos.CENTER);
        column0.setPercentWidth(20);

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(80);

        grid.getColumnConstraints().addAll(column0,column1);


        //Options menu
        GridPane options = new GridPane();
        options.setPadding(new Insets(10,10,20,10));
        //options.setGridLinesVisible(true);

        //generate button
        button = new Button();
        button.setText("GENERUJ");
        button.setMinWidth(200);
        button.setAlignment(Pos.TOP_CENTER);
        button.setOnAction(this::handle);
        options.add(button,0,0);
        GridPane.setMargin(button, new Insets(10,5,10,5));

        //resolution
        GridPane resolutionPanel = new GridPane();
        //resolutionPanel.setGridLinesVisible(true);

        Label resolutionText = new Label();
        resolutionText.setFont(new Font("Arial",14));
        resolutionText.setText("Rozdzielczość");
        resolutionPanel.add(resolutionText,0,0);

        resolutionX = new TextField();
        resolutionX.setStyle("-fx-min-width: 90;" +
                "-fx-max-width: 100");
        resolutionX.setText("800");
        makeTextFieldNumeraticOnly(resolutionX);
        resolutionPanel.add(resolutionX,0,1);

        Label resolutionValuesSeparator = new Label();
        resolutionValuesSeparator.setText("x");
        resolutionPanel.add(resolutionValuesSeparator,1,1);

        resolutionY = new TextField();
        resolutionY.setStyle("-fx-min-width: 90;" +
                "-fx-max-width: 100");
        resolutionY.setText("600");
        makeTextFieldNumeraticOnly(resolutionY);
        resolutionPanel.add(resolutionY,2,1);

        options.add(resolutionPanel,0,1);

        //samples
        Label numberOfSamplesLabel = new Label();
        numberOfSamplesLabel.setFont(new Font("Arial",14));
        numberOfSamplesLabel.setText("Liczba próbek");
        GridPane.setMargin(numberOfSamplesLabel, new Insets(5,2,2,2));

        numberOfSamples = new TextField();
        numberOfSamples.setStyle("-fx-min-width: 90;" +
                "-fx-max-width: 100");
        numberOfSamples.setText("10");
        makeTextFieldNumeraticOnly(numberOfSamples);

        options.add(numberOfSamplesLabel,0,2);
        options.add(numberOfSamples,0,3);

        //depth
        Label depthLabel = new Label();
        depthLabel.setFont(new Font("Arial",14));
        depthLabel.setText("Depth");
        GridPane.setMargin(depthLabel, new Insets(5,2,2,2));

        depth = new TextField();
        depth.setStyle("-fx-min-width: 90;" +
                "-fx-max-width: 100");
        depth.setText("10");
        makeTextFieldNumeraticOnly(depth);

        options.add(depthLabel,0,4);
        options.add(depth,0,5);

        //end of options grid
        grid.add(options,0,0);


        Scene scene = new Scene(grid,width,height);
        stage.setScene(scene);

        stage.show();
    }


    //image generating
    @Override
    public void handle(ActionEvent actionEvent) {

        if(actionEvent.getSource()==button) {
            buttonHandle();
        }
    }

    private void buttonHandle() {

        int newWidth = imageWidth;
        int newHeight = imageHeight;
        int newSamples = samples;
        int newDepth = maxDepth;

        try {
            newWidth = Integer.parseInt(resolutionX.getText().trim());
            newHeight = Integer.parseInt(resolutionY.getText().trim());
            newSamples = Integer.parseInt(numberOfSamples.getText().trim());
            newDepth = Integer.parseInt(depth.getText().trim());
        } catch (NumberFormatException nfe) {
            System.out.println(nfe.getMessage());
        }

        if(newWidth != imageWidth || newHeight != imageHeight || newSamples != samples || newDepth != maxDepth) {
            imageWidth = newWidth;
            imageHeight = newHeight;
            samples = newSamples;
            maxDepth = newDepth;
            renderer = new Renderer(imageWidth,imageHeight,samples,maxDepth);
        }
        System.out.println(imageWidth + "x" + imageHeight);

        byte[] pixels = getRenderedImage();

        grid.getChildren().remove(imageView);

        WritableImage image = new WritableImage(imageWidth,imageHeight);
        pixelWriter = image.getPixelWriter();
        pixelWriter.setPixels(0,0,imageWidth,imageHeight,PixelFormat.getByteRgbInstance(),pixels,0,imageWidth*3);
        imageView = new ImageView(image);
        grid.add(imageView, 1,0);
    }
    
    public byte[] getRenderedImage() {
        byte[] unflippedPixels = renderer.aparapiRender();
        byte[] pixels = new byte[width*height*3];

        //horizontal flip
        for(int i = 0; i < unflippedPixels.length; i=i+3) {
            pixels[i] = unflippedPixels[unflippedPixels.length-i-3];
            pixels[i+1] = unflippedPixels[unflippedPixels.length-i-2];
            pixels[i+2] = unflippedPixels[unflippedPixels.length-i-1];
        }
        
        return pixels;
    }

    public void makeTextFieldNumeraticOnly(TextField textField) {
        textField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if(!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

}