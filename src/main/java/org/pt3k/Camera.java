package org.pt3k;

public class Camera {
    public Vec3 lower_left_corner;
    public Vec3 horizontal;
    public Vec3 vertical;
    public Vec3 origin;

     public Camera() {
         lower_left_corner = new Vec3(-2,-1,-1);
         horizontal = new Vec3(4,0,0);
         vertical = new Vec3(0,2,0);
         origin = new Vec3(0,0,0);
     }

     public Ray getRay(float u, float v) {
         return new Ray(origin,lower_left_corner.add( horizontal.mul(u) ).add( vertical.mul(v) ).sub(origin));
     }
}
