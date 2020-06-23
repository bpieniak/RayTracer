package org.pt3k.materials;

import org.pt3k.Ray;
import org.pt3k.Vec3;
import org.pt3k.Wrapper;
import org.pt3k.HitRecord;

/**
 * Material reprezentujacy zrodlo swiatlo.
 */
public class DiffuseLight implements Material {
    private Texture emit;

    public DiffuseLight(Texture a) {
        emit = a;
    }

    @Override
    public boolean scatter(Ray r_in, HitRecord rec, Wrapper wrapper) {
        return false;
    }

    @Override
    public Vec3 emitted(float u, float v, Vec3 p) {
        return emit.value(u,v,p);
    }
}
