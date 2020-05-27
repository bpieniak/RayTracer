package org.pt3k;

public class Wrapper{
    public Ray scattered;
    public Vec3 refracted;
    public Vec3 attenuation;

    public Wrapper() {
        scattered = new Ray();
        attenuation = new Vec3();
        refracted = new Vec3();
    }
}