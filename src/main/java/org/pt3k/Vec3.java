package org.pt3k;



public class Vec3 {

    private float x;
    private float y;
    private float z;

    public Vec3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Vec3 inverse() {
        return new Vec3(-this.x,-this.y,-this.z);
    }

    public Vec3 add(final Vec3 v) {
        Vec3 vec = new Vec3();
        vec.x = this.x+v.x;
        vec.y = this.y+v.y;
        vec.z = this.z+v.z;
        return vec;
    }

    public Vec3 sub(final Vec3 v) {
        Vec3 vec = new Vec3();
        vec.x = this.x-v.x;
        vec.y = this.y-v.y;
        vec.z = this.z-v.z;
        return vec;
    }

    public Vec3 mul(final float t) {
        Vec3 vec = new Vec3();
        vec.x = this.x*t;
        vec.y = this.y*t;
        vec.z = this.z*t;
        return vec;
    }

    public Vec3 div(final float t) {
        Vec3 vec = new Vec3();
        vec.x = this.x/t;
        vec.y = this.y/t;
        vec.z = this.z/t;
        return vec;
    }

    public Vec3 sum(final Vec3 v) {
        Vec3 vec = new Vec3();
        vec.x = this.x + v.x;
        vec.y = this.y + v.y;
        vec.z = this.z + v.z;
        return vec;
    }

    public final float length() {
        return (float) Math.sqrt(length_squared());
    }

    public final float length_squared() {
        return x*x + y*y + z*z;
    }

    public float dot(final Vec3 v) {
        return this.x * v.x
                + this.y * v.y
                + this.z * v.z;
    }

    public Vec3 cross(final Vec3 u, final Vec3 v) {
        return new Vec3(u.y*v.z - u.z*v.y,
                u.z*v.x - u.x*v.z,
                u.x*v.y - u.y*v.x);
    }

    public Vec3 unit_vector() {
        return this.div(this.length());
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public void print() {
        System.out.println("[" + x + "," + y + "," + z + "]");
    }

    float clamp(float x, float min, float max) {
        if(x < min) return min;
        if(x > max) return max;
        return x;
    }

    public void scale(int samples) {
        float scale = 1.0f/samples;
        //System.out.println(x + " " + y + " " + z);
        x = Math.round((255*clamp((scale * x),0,1)));
        y = Math.round((255*clamp((scale * y),0,1)));
        z = Math.round((255*clamp((scale * z),0,1)));
        //System.out.println(x + " " + y + " " + z);
        //System.out.println();
    }
}

