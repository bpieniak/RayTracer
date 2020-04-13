package org.pt3k;

public interface hittable {
    boolean hit(final Ray r, float t_min, float t_max, hit_record hitRecord);
}
