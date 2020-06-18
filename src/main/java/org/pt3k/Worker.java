package org.pt3k;

import org.pt3k.shapes.hittable;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * Klasa uruchamiana na watku renderujaca obraz.
 */
public class Worker implements Runnable {

    int width, height;
    int numberOfSamples, maxDepth;
    int start, end;

    CountDownLatch countDownLatch;

    byte[] image;

    hit_record rec;
    ArrayList<hittable> sceneArray;
    Vec3 backgroundColor;
    Camera cam;

    Random generator;

    /**
     * Konstruktor klasy Worker.
     * @param width szerokosc obliczanego obrazu
     * @param height wysokosc obliczanego obrazu
     * @param image tablicy byteow do ktorej zostana zapisane obliczone wartosci obrazu
     * @param threadCount liczba watkow ktore renderuja obraz
     * @param threadID ID tego watku
     * @param cdl Count down latch
     * @param s Liczba probek na kazdy pixel
     * @param d Maksymalna liczba odbic promienia
     * @param c Kamera
     * @param rs ArrayList wszystkich obiektow w scenie
     * @param bg Kolor tla.
     */
    Worker(int width, int height, final byte[] image, int threadCount, int threadID, CountDownLatch cdl, int s, int d, Camera c,ArrayList<hittable> rs, Vec3 bg) {
        this.width = width;
        this.height = height;
        this.image = image;
        countDownLatch = cdl;
        numberOfSamples = s;
        maxDepth = d;
        cam = c;
        sceneArray = rs;
        backgroundColor = bg;

        int perThread = height/threadCount;
        start = threadID*perThread;
        end = (threadID + 1)*perThread;
        if(threadID == threadCount-1)
            end = height;

        rec = new hit_record();
        generator = new Random();
    }


    /**
     * Metoda klasy Runnable ktora tworzy promienie z kamery do sceny, oblicza kolor kazdego pixela
     * i zapisuje go do zadanej tablicy.
     */
    @Override
    public void run() {

        Scene scene = new Scene(sceneArray);

        Vec3 background = backgroundColor;

        for(int x = start; x < end; x++) {
            for(int y = 0; y < width; y++) {
                Vec3 color = new Vec3(0,0,0);

                for(int s =  0; s < numberOfSamples; ++s) {
                    float u = ((y + MyRandom.randomFloatInRange(-1,1))/(float)width);
                    float v = ((x + MyRandom.randomFloatInRange(-1,1))/(float)height);

                    Ray r = cam.getRay(u,v);
                    color = color.add(ray_color(r,background,scene,maxDepth));
                }
                color.scale(numberOfSamples);

                image[(x*width+y)*3] = intToByte((int) (255*color.getX()));
                image[(x*width+y)*3 + 1] = intToByte((int) (255*color.getY()));
                image[(x*width+y)*3 + 2] = intToByte((int) (255*color.getZ()));
            }
        }

        countDownLatch.countDown();
    }


    /**
     * Metoda obliczajaca kolor danego promienia na podstawie trafionych obiektow.
     * @param r promiec
     * @param background kolor tla
     * @param world scena
     * @param depth glebokosc promienia
     * @return obliczony kolor promienia
     */
    public Vec3 ray_color(final Ray r, Vec3 background,Scene world, int depth) {

        Wrapper wrapper = new Wrapper();

        if(depth <= 0) {
            return new Vec3(0,0,0);
        }

        if(!world.hit(r,0.01f,Float.MAX_VALUE,rec)) {
            return background;
        }

        Vec3 emitted = rec.material.emitted(rec.u,rec.v,rec.p);

        if(!rec.material.scatter(r,rec,wrapper))
            return emitted;

        return (ray_color(wrapper.scattered,background,world,depth-1).mulvec(wrapper.attenuation)).add(emitted);
    }

    private byte intToByte(int i) {

        if(i > 128) {
            return (byte) (i-256);
        }
        return (byte) i;
    }
}
