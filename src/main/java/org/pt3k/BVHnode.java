package org.pt3k.shapes;

import org.pt3k.Aabb;
import org.pt3k.Ray;
import org.pt3k.hit_record;

public class BVHnode implements hittable {
    @Override
    public boolean hit(Ray r, float t_min, float t_max, hit_record hitRecord) {
        return false;
    }

    @Override
    public boolean boundingBox(float t0, float t1, Aabb outputBox) {
        return false;
    }
}
