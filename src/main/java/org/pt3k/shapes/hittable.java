package org.pt3k.shapes;

import org.pt3k.Ray;
import org.pt3k.hit_record;

public interface hittable {
    boolean hit(final Ray r, float t_min, float t_max, hit_record hitRecord);
}