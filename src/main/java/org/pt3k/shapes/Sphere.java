package org.pt3k.shapes;

import org.pt3k.Ray;
import org.pt3k.Vec3;
import org.pt3k.HitRecord;
import org.pt3k.materials.Material;

/**
 * Klasa reprezentujaca sfere.
 */
public class Sphere implements Hittable {

    private Vec3 center;
    private float radius;
    private Material material;

    public Sphere(Vec3 cen, float radius, Material material) {
        this.center = cen;
        this.radius = radius;
        this.material = material;
    }

    /**
     * Metoda sprawdzajaca czy promien trafil w sfere w scenie
     * @param r promien
     * @param t_min minimalna odleglosc trafienia
     * @param t_max maxymalna odleglosc trafienia
     * @param hitRecord informacje o trafieniu
     * @return czy promien trafil
     */
    @Override
    public boolean hit(Ray r, float t_min, float t_max, HitRecord hitRecord) {

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

    void getSphereUV(Vec3 p, HitRecord hitRecord) {

        double phi = Math.atan2(p.getZ(),p.getX());
        double theta = Math.asin(p.getY());
        hitRecord.u = (float) (1-(phi+Math.PI)/(2*Math.PI));
        hitRecord.v = (float) ((theta + Math.PI/2)/Math.PI);
    }
}
