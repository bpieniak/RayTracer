package org.pt3k.shapes;

import org.pt3k.Ray;
import org.pt3k.HitRecord;

/**
 * Interfejs dla obiektu ktory moze zostac trafiony przez promien.
 */
public interface Hittable {
    boolean hit(final Ray r, float t_min, float t_max, HitRecord hitRecord);
}
