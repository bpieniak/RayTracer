package org.pt3k.materials;

import org.pt3k.Ray;
import org.pt3k.Vec3;
import org.pt3k.Wrapper;
import org.pt3k.HitRecord;

/**
 * Material dielektryk. Po trafieniu promienia w ten material promien ulega jednoczesnie odbiciu i zalamaniu.
 */
public class Dielectric implements Material {

    private float refIdx;
    public Dielectric(float ri) {
        refIdx = ri;
    }

    @Override
    public boolean scatter(Ray r_in, HitRecord rec, Wrapper wrapper) {
        wrapper.attenuation = new Vec3(1,1,1);
        float etaiOverEtat;
        if (rec.front_face) {
            etaiOverEtat = (1.0f/refIdx);
        } else {
            etaiOverEtat = refIdx;
        }

        Vec3 unitDirection = r_in.getDirection().unit_vector();
        float cos_theta = Math.min(rec.normal.dot(unitDirection.inverse()),1.0f);
        float sin_theta = (float) Math.sqrt(1.0-cos_theta*cos_theta);
        if(etaiOverEtat*sin_theta > 1.0f) {
            Vec3 reflected = Vec3.reflect(unitDirection,rec.normal);
            wrapper.scattered = new Ray(rec.p, reflected);
            return true;
        }

        float reflectProb = schlick(cos_theta,etaiOverEtat);
        if(Math.random() < reflectProb) {
            Vec3 reflected = Vec3.reflect(unitDirection,rec.normal);
            wrapper.scattered = new Ray(rec.p,reflected);
            return true;
        }

        Vec3 refracted = Vec3.refract(unitDirection, rec.normal, etaiOverEtat);
        wrapper.scattered = new Ray(rec.p, refracted);
        return true;
    }

    @Override
    public Vec3 emitted(float u, float v, Vec3 p) {
        return new Vec3(0,0,0);
    }

    float schlick(float cosine, float refIdx) {
        float r0 = (1-refIdx)/(1+refIdx);
        r0 = r0*r0;
        return (float) (r0 + (1-r0)*Math.pow((1-cosine),5));
    }
}
