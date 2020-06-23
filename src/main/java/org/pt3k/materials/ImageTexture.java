package org.pt3k.materials;

import javafx.embed.swing.SwingFXUtils;
import javafx.stage.FileChooser;
import org.pt3k.Vec3;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

public class ImageTexture implements Texture {

    private static final int bytesPerPixel = 3;

    private int width, height;
    private int bytesPerScanline;
    private int[] data;

    /**
     * Odczytuje dane z tekstury i zapisuje w tablicy data.
     * @param filepath sciezka do obrazu tekstury
     */
    public ImageTexture(String filepath) throws IOException {
        getImageData(filepath);
    }

    public ImageTexture() throws IOException {
        String filepath = chooseTexture();
        getImageData(filepath);
    }

    void getImageData(String filepath) throws IOException {

        if(filepath == null) {
            data = new int[]{255,0,255};
            width = 1;
            height = 1;
            bytesPerScanline = 3;
            return;
        }
        System.out.println(filepath);
        File img = new File(filepath);

        BufferedImage image = ImageIO.read(img);
        WritableRaster r = image.getRaster();
        DataBufferByte d = (DataBufferByte) r.getDataBuffer();

        byte[] byteData = d.getData();

        width = image.getWidth();
        height = image.getHeight();

        data = new int[byteData.length];

        for (int i = 0; i < byteData.length; i++) {
            data[i] = byteData[i] & 0xFF;
        }

        bytesPerScanline = bytesPerPixel*width;
    }

    @Override
    public Vec3 value(float u, float v, Vec3 p) {
        if(data == null)
            return new Vec3(0,1,1);

        u = Vec3.clamp(u,0,1);
        v = 1.0f - Vec3.clamp(v,0,1);

        int i = (int) (u*width);
        int j = (int) (v*height);

        if(i >= width) i = width-1;
        if(j >= height) j = height-1;

        float colorScale = 1.0f/255.0f;
        Vec3 pixel = new Vec3(data[j*bytesPerScanline+i*bytesPerPixel+2],
                data[j*bytesPerScanline+i*bytesPerPixel+1],
                data[j*bytesPerScanline+i*bytesPerPixel]);

        return pixel.mul(colorScale);
    }

    private String chooseTexture(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose texture image");
        fileChooser.selectedExtensionFilterProperty();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPG", "*.jpg"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            return file.getPath();
        }
        return null;
    }
}
