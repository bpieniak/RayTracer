package org.pt3k;

public class Camera {
    public Vec3 lower_left_corner;
    public Vec3 horizontal;
    public Vec3 vertical;
    public Vec3 origin;

     public Camera(float vfov, float aspect, Vec3 lookfrom, Vec3 lookat, Vec3 vup) {
         Vec3 u, v, w;
         origin = lookfrom;

         float theta = (float) Math.toRadians(vfov);
         float half_height = (float) Math.tan(theta/2f);
         float half_width = aspect*half_height;
         w = (lookfrom.sub(lookat)).unit_vector();
         u = (vup.cross(w)).unit_vector();
         v = w.cross(u);

         lower_left_corner = origin.sub(u.mul(half_width)).sub(v.mul(half_height)).sub(w);
         horizontal = u.mul(2*half_width);
         vertical = v.mul(2*half_height);
     }

     public Ray getRay(float u, float v) {
         return new Ray(origin,lower_left_corner.add( horizontal.mul(u) ).add( vertical.mul(v) ).sub(origin));
     }
}
