package org.pt3k;

import org.pt3k.materials.Material;

public class hit_record {
    public Vec3 p;
    public Vec3 normal;
    public Material material;
    public float t;
    public float u;
    public float v;
    public boolean front_face;


    public void set_front_face(Ray r, Vec3 outward_normal) {
        front_face = (r.getDirection().dot(outward_normal) < 0);
        if(front_face) {
            normal = outward_normal;
        } else {
            normal = outward_normal.inverse();
        }
    }
}