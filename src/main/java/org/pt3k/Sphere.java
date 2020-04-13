package org.pt3k;

public class Sphere implements hittable{

    Vec3 center;
    float radius;

    public Sphere() {}
    public Sphere(Vec3 cen, float radius) {
        this.center = cen;
        this.radius = radius;
    }

    @Override
    public boolean hit(Ray r, float t_min, float t_max, hit_record hitRecord) {

        Vec3 oc = r.origin.sub(center);
        float a = r.getDirection().length_squared();
        float half_b = oc.dot(r.getDirection());
        float c = oc.length_squared() - radius*radius;
        float discriminant = half_b*half_b-a*c;

        if (discriminant > 0) {
            float root = (float) Math.sqrt(discriminant);
            float temp = (-half_b-root)/a;
            if(temp > t_min && temp < t_max) {
                hitRecord.t = temp;
                hitRecord.p = r.at(temp);
                Vec3 outward_normal = (r.at(temp).sub(center)).div(radius);
                hitRecord.set_front_face(r,outward_normal);
                return true;
            }
            temp = (-half_b+root)/a;
            if(temp > t_min && temp < t_max) {
                hitRecord.t = temp;
                hitRecord.p = r.at(temp);
                Vec3 outward_normal = (r.at(temp).sub(center)).div(radius);
                hitRecord.set_front_face(r,outward_normal);
                return true;
            }
        }

        return false;
    }
}
