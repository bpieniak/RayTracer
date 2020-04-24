package org.pt3k;

public class hit_record {
    public Vec3 p;
    public Vec3 normal;
    public float t;
    boolean front_face;
    Material material;

    void set_front_face(Ray r, Vec3 outward_normal) {
        boolean front_face = (r.getDirection().dot(outward_normal) < 0);
        if(front_face) {
            normal = outward_normal;
        } else {
            normal = outward_normal.inverse();
        }
    }
}