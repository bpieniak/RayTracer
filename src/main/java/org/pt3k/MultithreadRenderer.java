package org.pt3k;

import org.pt3k.shapes.Hittable;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * Klasa powolujaca watki renderujace obraz.
 */
public class MultithreadRenderer {

    public int width;
    public int height;
    public int numberOfSamples;
    public int maxDepth;
    public byte[] pixels;
    private HitRecord rec;
    private Camera cam;
    private Vec3 background;
    private ArrayList<Hittable> scene;
    private Random generator;

    public MultithreadRenderer(int width, int height, int numberOfSamples, int maxDepth, Camera c, Vec3 bg, ArrayList<Hittable> s) {
        this.width = width;
        this.height = height;
        this.numberOfSamples = numberOfSamples;
        this.maxDepth = maxDepth;

        cam = c;
        background = bg;
        scene = s;
        pixels = new byte[width*height*3];
        generator = new Random();
        rec = new HitRecord();
    }

    public byte[] render() throws InterruptedException {

        int threadCount = Runtime.getRuntime().availableProcessors();
        CountDownLatch latch = new CountDownLatch(threadCount);

        for(int thread = 0; thread < threadCount; thread++) {
            new Thread(new Worker(width,height,pixels,threadCount,thread,latch,numberOfSamples,maxDepth,cam,scene,background)).start();
        }
        latch.await();

        //flip
        byte[] image = new byte[width*height*3];
        for(int i = 0; i < pixels.length; i = i+3) {
            image[i] = pixels[pixels.length-i-3];
            image[i+1] = pixels[pixels.length-i-2];
            image[i+2] = pixels[pixels.length-i-1];
        }

        return image;
    }

}
