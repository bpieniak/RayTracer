package org.pt3k;

import com.aparapi.Kernel;
import com.aparapi.internal.kernel.KernelManager;
import com.aparapi.internal.kernel.KernelPreferences;

import java.util.Random;

public class Renderer {

    private int samples_per_pixel = 1;

    public int width;
    public int height;
    public byte[] pixels;
    long lastTime;

    Vec3 color;

    public Renderer(int width, int height) {
        this.width = width;
        this.height = height;
        pixels = new byte[height * width * 3];
    }

    public byte[] render() {
        byte[] pixels = new byte[height * width * 3];
        int i = 0;
        for (int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                pixels[i] = intToByte(255*y/height);
                pixels[i+1] = intToByte(255*x/width);
                pixels[i+2] = intToByte((int) (256*0.2));
                i=i+3;
            }
        }
        fpsCounter();
        return pixels;
    }

    public Vec3 ray_color(final Ray r, hittable_list world) {
        hit_record rec = new hit_record();

        if(world.hit(r,0,Float.MAX_VALUE,rec)) {
            return (rec.normal.add(new Vec3(1,1,1))).mul(0.5f);
        }

        Vec3 unit_direction = r.getDirection().unit_vector();
        float t = (float) ((unit_direction.getY() + 1)*0.5);
        return ((new Vec3(1,1,1)).mul(1-t)).add(new Vec3(0.5f,0.7f,1.0f).mul(t));
    }

    public byte[] aparapiRender() {
        byte[] pixels = new byte[height * width * 3];

        Camera cam = new Camera();
        hittable_list world = new hittable_list();

        world.add(new Sphere(new Vec3(0,0,-1), 0.5f));
        world.add(new Sphere(new Vec3(0,-100.5f,-1), 100));

        Kernel kernel = new Kernel() {
            @Override
            public void run() {
                int gid = getGlobalId();
                int x = gid%width;
                int y = (gid/width);

                float u = (x)/(float)width;
                float v = (y)/(float)height;

                Ray r = cam.getRay(u,v);
                Vec3 color = ray_color(r,world);

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
