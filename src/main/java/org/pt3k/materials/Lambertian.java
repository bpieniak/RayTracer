package org.pt3k.materials;

import org.pt3k.*;

/**
 * Matowy material ulegajacy odbiciu Lambertowskiemu
 */
public class Lambertian implements Material {

    Texture albedo;

    public Lambertian(Texture albedo) {
        this.albedo = albedo;
    }

    public boolean scatter(Ray r_in, hit_record rec, Wrapper wrapper) {
        Vec3 scatterDirection = rec.normal.add(MyRandom.random_unit_vector());
        wrapper.scattered = new Ray(rec.p, scatterDirection);
        wrapper.attenuation = albedo.value(rec.u,rec.v,rec.p);
        return true;
    }

    @Override
    public Vec3 emitted(float u, float v, Vec3 p) {
        return new Vec3(0,0,0);
    }
}
