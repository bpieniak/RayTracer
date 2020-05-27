package org.pt3k;

import org.pt3k.shapes.hittable;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class Worker implements Runnable {

    int width, height;
    int numberOfSamples, maxDepth;
    int start, end;

    CountDownLatch countDownLatch;

    byte[] image;

    hit_record rec;
    ArrayList<hittable> sceneArray;

    Random generator;

    Worker(int width, int height, final byte[] image, int threadCount, int threadID, CountDownLatch cdl, int s, int d, ArrayList<hittable> rs) {
        this.width = width;
        this.height = height;
        this.image = image;
        countDownLatch = cdl;
        numberOfSamples = s;
        maxDepth = d;
        sceneArray = rs;

        int perThread = height/threadCount;
        start = threadID*perThread;
        end = (threadID + 1)*perThread;
        if(threadID == threadCount-1)
            end = height;

        rec = new hit_record();
        generator = new Random();
    }

    @Override
    public void run() {
        //System.out.println(Thread.currentThread() + " " + start + " " + end);

        Camera cam = new Camera(20,(float) width/height,
                new Vec3(13,2,3),
                new Vec3(0,0,0),
                new Vec3(0,1,0));

        Scene scene = new Scene(sceneArray);

        Vec3 backgroud = new Vec3(0.2f,0.2f,0.2f);

        for(int x = start; x < end; x++) {
            for(int y = 0; y < width; y++) {
                Vec3 color = new Vec3(0,0,0);

                for(int s =  0; s < numberOfSamples; ++s) {
                    float u = ((y + MyRandom.randomFloatInRange(-1,1))/(float)width);
                    float v = ((x + MyRandom.randomFloatInRange(-1,1))/(float)height);

                    Ray r = cam.getRay(u,v);
                    color = color.add(ray_color(r,backgroud,scene,maxDepth));
                }
                color.scale(numberOfSamples);

                image[(x*width+y)*3] = intToByte((int) (255*color.getX()));
                image[(x*width+y)*3 + 1] = intToByte((int) (255*color.getY()));
                image[(x*width+y)*3 + 2] = intToByte((int) (255*color.getZ()));
            }
        }

        countDownLatch.countDown();
    }

    public Vec3 ray_color(final Ray r, Vec3 background,Scene world, int depth) {

        if(depth <= 0) {
            return new Vec3(0,0,0);
        }

        if(!world.hit(r,0.01f,Float.MAX_VALUE,rec)) {
            return background;
        }
        Wrapper wrapper = new Wrapper();


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
