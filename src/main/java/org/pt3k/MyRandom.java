package org.pt3k;

import java.util.Random;

public class MyRandom {
    private static Random generator;

    public static float randomFloatInRange(float min, float max) {
        generator = new Random(System.nanoTime());

        return generator.nextFloat()*(max-min) + min;
    }

    public static Vec3 random_unit_vector() {
        generator = new Random(System.nanoTime());

        float a = (float) (generator.nextFloat()*2*Math.PI);
        float z = generator.nextFloat()*2-1;
        float r = (float) Math.sqrt(1-z*z);
        return new Vec3((float) (r*Math.cos(a)), (float) (r*Math.sin(a)), z);
    }

    public static Vec3 random_in_hemisphere(Vec3 normal) {
        Vec3 inUnitSphere = random_unit_vector();

        if(inUnitSphere.dot(normal) > 0) {
            return inUnitSphere;
        } else {
            return inUnitSphere.inverse();
        }
    }

    public static Vec3 randomVector() {
        return new Vec3((float) Math.random(),(float) Math.random(),(float) Math.random());
    }
}
