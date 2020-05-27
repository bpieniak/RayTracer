package org.pt3k;

import org.pt3k.shapes.hittable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class MultithreadRenderer {

    public int width;
    public int height;
    public int numberOfSamples;
    public int maxDepth;
    public byte[] pixels;
    hit_record rec;
    Random generator;

    Camera cam;

    public MultithreadRenderer(int width, int height, int numberOfSamples, int maxDepth) {
        this.width = width;
        this.height = height;
        this.numberOfSamples = numberOfSamples;
        this.maxDepth = maxDepth;

        pixels = new byte[width*height*3];
        generator = new Random();
        rec = new hit_record();
    }

    public byte[] render() throws InterruptedException {

        int threadCount = Runtime.getRuntime().availableProcessors();
        System.out.println(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        ArrayList<hittable> randomScene = Scene.randomScene();

        for(int thread = 0; thread < threadCount; thread++) {
            new Thread(new Worker(width,height,pixels,threadCount,thread,latch,numberOfSamples,maxDepth, randomScene)).start();
        }

        latch.await();

        //flip
        byte[] image = new byte[width*height*3];
        for(int i = 0; i < pixels.length; i = i+3) {
            image[i] = pixels[pixels.length-i-3];
            image[i+1] = pixels[pixels.length-i-2];
            image[i+2] = pixels[pixels.length-i-1];
        }

        System.out.println(Arrays.toString(pixels));

        return image;
    }

}
