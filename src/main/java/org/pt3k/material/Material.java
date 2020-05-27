package org.pt3k;

public interface Material {

    boolean scatter(Ray r_in, hit_record rec, Wrapper wrapper);
}
