package org.pt3k;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Renderer {

    public int width;
    public int height;
    public int numberOfSamples;
    public int maxDepth;
    public byte[] pixels;
    long lastTime;
    hit_record rec;
    Random generator;

    public Renderer(int width, int height, int numberOfSamples, int maxDepth) {
        this.width = width;
        this.height = height;
        this.numberOfSamples = numberOfSamples;
        this.maxDepth = maxDepth;

        pixels = new byte[height * width * 3];
        rec = new hit_record();
        generator = new Random();
    }


    public Vec3 ray_color(final Ray r, hittable_list world, int depth) {

        if(depth <= 0) {
            return new Vec3(0,0,0);
        }

        if(world.hit(r,0.01f,Float.MAX_VALUE,rec)) {
            Wrapper wrapper = new Wrapper();

             if(rec.material.scatter(r,rec,wrapper)) {
                 return ray_color(wrapper.scattered,world,depth-1).mulvec(wrapper.attenuation);
             }

            return new Vec3(0,0,0);
        }

        Vec3 unit_direction = r.getDirection().unit_vector();
        float t = (float) ((unit_direction.getY() + 1)*0.5);
        return ((new Vec3(1,1,1)).mul(1-t)).add(new Vec3(0.2f,0.3f,0.6f).mul(t));
    }

    public ArrayList<hittable> randomScene() {
        ArrayList<hittable> worldList = new ArrayList<>();

        worldList.add(new Sphere(new Vec3(0,-1000,0), 1000, new Lambertian(new Vec3(0.5f,0.5f,0.5f))));

        int i = 1;
        for(int a = -11; a < 11; a++) {
            for(int b = -11; b < 11; b++) {
                float choose_mat = (float) Math.random();
                Vec3 center = new Vec3((float )(a + 0.9*Math.random()), 0.2f, (float )(b + 0.9*Math.random()));

                if(choose_mat < 0.9) {
                    Vec3 albedo = MyRandom.randomVector().mulvec(MyRandom.randomVector());
                    worldList.add(new Sphere(center,0.2f,new Lambertian(albedo)));
                } else if(choose_mat < 0.95) {
                    Vec3 albedo = MyRandom.randomVector();
                    worldList.add(new Sphere(center, 0.2f, new Metal(albedo)));
                } else {
                    worldList.add(new Sphere(center,0.2f, new Dielectric(1.5f)));
                }
            }
        }

        worldList.add(new Sphere(new Vec3(0,1,0),1,new Dielectric(1.5f)));
        worldList.add(new Sphere(new Vec3(-4,1,0),1,new Lambertian(new Vec3(0.4f,0.2f,0.1f))));
        worldList.add(new Sphere(new Vec3(4,1,0),1,new Metal(new Vec3(0.7f,0.6f,0.5f))));

        return worldList;
    }

    public byte[] singleCoreRenderer() {

        Camera cam = new Camera(20,(float) width/height,
                new Vec3(13,2,3),
                new Vec3(0,0,0),
                new Vec3(0,1,0));

        List<hittable> worldList = randomScene();

        //worldList.add(new Sphere(new Vec3(0,0,-1), 0.5f, new Lambertian(new Vec3(0.8f,0.3f,0.3f))));
        //worldList.add(new Sphere(new Vec3(0,-600.5f,-1), 600, new Lambertian(new Vec3(0.3f,0.7f,0.1f)) ));
        //worldList.add(new Sphere(new Vec3(1,0,-1), 0.5f, new Dielectric(1.5f)));
        //worldList.add(new Sphere(new Vec3(1,0,-1), -0.48f, new Dielectric(1.5f)));
        //worldList.add(new Sphere(new Vec3(-1,0,-1), 0.5f, new Metal(new Vec3(0.4f,0.3f,0.8f))));

        hittable_list world = new hittable_list(worldList);
        System.out.println("aaaa");
        int i = 0;
        for(int x = height - 1; x >= 0; x--) {
            if(x%20==0) {
                System.out.println(x);
            }
            for(int y = 0; y < width; y++) {
                Vec3 color = new Vec3(0,0,0);

                for(int s =  0; s < numberOfSamples; ++s) {
                    float u = ((y + generator.nextFloat())/(float)width);
                    float v = ((x + generator.nextFloat())/(float)height);

                    Ray r = cam.getRay(u,v);
                    color = color.add(ray_color(r,world,maxDepth));
                }
                color.scale(numberOfSamples);

                pixels[3*i] = intToByte((int) (255*color.getX()));
                pixels[3*i+1] = intToByte((int) (255*color.getY()));
                pixels[3*i+2] = intToByte((int) (255*color.getZ()));
                i++;
            }
        }
        return pixels;
    }

    private void fpsCounter() {
        System.out.println(1.0/((System.currentTimeMillis()-lastTime)/1000.0));
        lastTime = System.currentTimeMillis();
    }

    private byte intToByte(int i) {
        if(i >= 128) {
            return (byte) (i-256);
        }
        return (byte) i;
    }
}
