package org.pt3k;

public class Sphere implements hittable{

    Vec3 center;
    float radius;
    Material material;

    public Sphere() {}
    public Sphere(Vec3 cen, float radius, Material material) {
        this.center = cen;
        this.radius = radius;
        this.material = material;
    }

    @Override
    public boolean hit(Ray r, float t_min, float t_max, hit_record hitRecord) {

        Vec3 oc = r.origin.sub(center);
        float a = r.getDirection().dot(r.getDirection());
        float b = 2.0f * oc.dot(r.getDirection());
        float c = oc.dot(oc) - radius*radius;
        float discriminant = b*b - 4.0f*a*c;

        if (discriminant > 0) {
            float root = (float) Math.sqrt(discriminant);
            float temp = (-b-root)/(2.0f*a);
            if(temp > t_min && temp < t_max) {
                hitRecord.t = temp;
                hitRecord.p = r.at(temp);
                Vec3 outward_normal = (r.at(temp).sub(center)).div(radius);
                hitRecord.set_front_face(r,outward_normal);
                hitRecord.material = material;
                return true;
            }
            temp = (-b+root)/(2.0f*a);
            if(temp > t_min && temp < t_max) {
                hitRecord.t = temp;
                hitRecord.p = r.at(temp);
                Vec3 outward_normal = (r.at(temp).sub(center)).div(radius);
                hitRecord.set_front_face(r,outward_normal);
                hitRecord.material = material;
                return true;
            }
        }

        return false;
    }
}
