package org.pt3k;

import java.util.Random;

public class Dielectric implements Material {

    private static Random generator = new Random();
    float refIdx;

    public Dielectric(float ri) {
        refIdx = ri;
    }

    @Override
    public boolean scatter(Ray r_in, hit_record rec, Wrapper wrapper) {
        wrapper.attenuation = new  Vec3(1,1,1);
        float etaiOverEtat;
        if (rec.front_face) {
            etaiOverEtat = (1.0f/refIdx);
        } else {

            etaiOverEtat = refIdx;
        }

        Vec3 unitDirection = r_in.direction.unit_vector();
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

    float schlick(float cosine, float refIdx) {
        float r0 = (1-refIdx)/(1+refIdx);
        r0 = r0*r0;
        return (float) (r0 + (1-r0)*Math.pow((1-cosine),5));
    }
}
