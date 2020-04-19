package org.pt3k;

import com.aparapi.Kernel;
import com.aparapi.internal.kernel.KernelManager;
import com.aparapi.internal.kernel.KernelPreferences;

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

    Vec3 random_in_unit_sphere() {
        Vec3 p = new Vec3();
        do {
            p = p.random();
        } while (p.length_squared() >= 1);
        return p;
    }


    public Vec3 ray_color(final Ray r, hittable_list world, int depth) {

        if(depth <= 0) {
            return new Vec3(0,0,0);
        }

        if(world.hit(r,0,Float.MAX_VALUE,rec)) {
            Vec3 target = rec.p.add(rec.normal).add(random_in_unit_sphere());
            return ray_color( new Ray(rec.p,target.sub(rec.p)),world,depth-1).mul(0.5f);
        }

        Vec3 unit_direction = r.getDirection().unit_vector();
        float t = (float) ((unit_direction.getY() + 1)*0.5);
        return ((new Vec3(1,1,1)).mul(1-t)).add(new Vec3(0.5f,0.7f,1.0f).mul(t));
    }

    public byte[] aparapiRender() {

        Camera cam = new Camera(90,(float) width/height,
                new Vec3(0,0,0),
                new Vec3(0,0,-1),
                new Vec3(0,1,0));

        hittable_list world = new hittable_list();


        float R = (float) Math.cos(Math.PI/4);
        world.add(new Sphere(new Vec3(-R,0,-2), R, new Vec3(1,0,0)));
        world.add(new Sphere(new Vec3(R,0,-2), R, new Vec3(0,1,0)));
        world.add(new Sphere(new Vec3(0,-100f-R,-1), 100, new Vec3(0,0,1)));

        Vec3 zero = new Vec3(0,0,0);

        Kernel kernel = new Kernel() {
            @Override
            public void run() {
                int gid = getGlobalId();
                int x = gid%width;
                int y = (gid/width);

                Vec3 color = zero;

                for(int s =  0; s < numberOfSamples; ++s) {
                    float u = (x + generator.nextFloat())/(float)width;
                    float v = (y + generator.nextFloat())/(float)height;

                    Ray r = cam.getRay(u,v);
                   color = color.add(ray_color(r,world,maxDepth));
                }
                color.scale(numberOfSamples);

                pixels[3*gid] = intToByte((int) (255*color.getX()));
                pixels[3*gid + 1] = intToByte((int) (255*color.getY()));
                pixels[3*gid + 2] = intToByte((int) (255*color.getZ()));
            }
        };
        kernel.execute(width*height);
        fpsCounter();
        kernel.dispose();
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
