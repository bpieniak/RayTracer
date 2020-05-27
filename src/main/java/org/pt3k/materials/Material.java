package org.pt3k.materials;

import org.pt3k.Ray;
import org.pt3k.Vec3;
import org.pt3k.Wrapper;
import org.pt3k.hit_record;

public interface Material {

    boolean scatter(Ray r_in, hit_record rec, Wrapper wrapper);
}
