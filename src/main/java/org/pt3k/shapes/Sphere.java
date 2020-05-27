package org.pt3k.shapes;

import org.pt3k.Aabb;
import org.pt3k.Ray;
import org.pt3k.Vec3;
import org.pt3k.hit_record;
import org.pt3k.material.Material;

public class Sphere implements hittable {

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

    @Override
    public boolean boundingBox(float t0, float t1, Aabb outputBox) {
        outputBox = new Aabb(
                center.sub(new Vec3(radius,radius,radius)),
                center.add(new Vec3(radius,radius,radius)));
        return true;
    }
}
