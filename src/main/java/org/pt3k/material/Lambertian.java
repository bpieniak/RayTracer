package org.pt3k.material;

import org.pt3k.*;

public class Lambertian implements Material {

    Vec3 albedo;

    public Lambertian(Vec3 albedo) {
        this.albedo = albedo;
    }

    public boolean scatter(Ray r_in, hit_record rec, Wrapper wrapper) {
        Vec3 scatterDirection = rec.normal.add(MyRandom.random_unit_vector());
        wrapper.scattered = new Ray(rec.p, scatterDirection);
        wrapper.attenuation = albedo;
        return true;
    }
}
