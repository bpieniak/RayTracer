package org.pt3k.shapes;

import org.pt3k.Ray;
import org.pt3k.Vec3;
import org.pt3k.hit_record;
import org.pt3k.materials.Material;

/**
 * Klasa reprezentujaca sfere.
 */
public class Sphere implements hittable{

    public Vec3 center;
    public float radius;
    public Material material;

    public Sphere(Vec3 cen, float radius, Material material) {
        this.center = cen;
        this.radius = radius;
        this.material = material;
    }

    @Override
    public boolean hit(Ray r, float t_min, float t_max, hit_record hitRecord) {

        Vec3 oc = r.getOrigin().sub(center);
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
                getSphereUV((hitRecord.p.sub(center)).div(radius),hitRecord);
                return true;
            }
            temp = (-b+root)/(2.0f*a);
            if(temp > t_min && temp < t_max) {
                hitRecord.t = temp;
                hitRecord.p = r.at(temp);
                Vec3 outward_normal = (r.at(temp).sub(center)).div(radius);
                hitRecord.set_front_face(r,outward_normal);
                hitRecord.material = material;
                getSphereUV((hitRecord.p.sub(center)).div(radius),hitRecord);
                return true;
            }
        }

        return false;
    }

    void getSphereUV(Vec3 p, hit_record hitRecord) {

        double phi = Math.atan2(p.getZ(),p.getX());
        double theta = Math.asin(p.getY());
        hitRecord.u = (float) (1-(phi+Math.PI)/(2*Math.PI));
        hitRecord.v = (float) ((theta + Math.PI/2)/Math.PI);
    }
}
