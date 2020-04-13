package org.pt3k;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


public class App extends Application {

    public static int width = 1000;
    public static int height = width/2;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        WritableImage image = new WritableImage(width,height);

        Renderer renderer = new Renderer(width,height);

        new AnimationTimer()
        {

            @Override
            public void handle(long l) {
                byte[] revertedPixels = renderer.aparapiRender();
                byte[] pixels = new byte[width*height*3];

                //horizontal flip
                for(int i = 0; i < revertedPixels.length; i=i+3) {
                    pixels[i] = revertedPixels[revertedPixels.length-i-3];
                    pixels[i+1] = revertedPixels[revertedPixels.length-i-2];
                    pixels[i+2] = revertedPixels[revertedPixels.length-i-1];
                }


                PixelWriter pixelWriter = image.getPixelWriter();

                pixelWriter.setPixels(0,0,width,height,PixelFormat.getByteRgbInstance(),pixels,0,width*3);

                ImageView newImageView = new ImageView(image);

                HBox root = new HBox(newImageView);
                root.setAlignment(Pos.CENTER);
                root.setStyle("-fx-background-color: gray");
                Scene scene = new Scene(root,width,height);
                stage.setScene(scene);
            }
        }.start();

        stage.show();
    }

}