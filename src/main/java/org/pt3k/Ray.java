package org.pt3k;


public class Ray {
    public Vec3 origin;
    public Vec3 direction;

    public Ray() {}

    public Ray(final Vec3 origin, final Vec3 direction) {
        this.origin = origin;
        this.direction = direction;
    }

    public Vec3 at(float t) {
        Vec3 tdir = direction.mul(t);
        return origin.add(tdir);
    }

    public Vec3 getOrigin() { return origin; }
    public Vec3 getDirection() { return  direction; }
}
