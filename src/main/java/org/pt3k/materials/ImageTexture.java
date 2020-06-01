package org.pt3k.materials;

import org.pt3k.Vec3;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;

public class ImageTexture implements Texture {

    static final int bytesPerPixel = 3;

    int width, height;
    int bytesPerScanline;
    int[] data;


    ImageTexture() { }

    public ImageTexture(String filepath) throws IOException {

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
}
