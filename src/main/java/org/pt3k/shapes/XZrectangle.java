package org.pt3k.shapes;

import org.pt3k.Ray;
import org.pt3k.Vec3;
import org.pt3k.hit_record;
import org.pt3k.materials.Material;

/**
 * Prostokat lezacy w osi XZ.
 */
public class XZrectangle implements hittable{

    float x0, x1, z0, z1, k;
    Material material;

    public XZrectangle(float x0, float x1, float z0, float z1, float k, Material mat) {
        this.x0 = x0;
        this.x1 = x1;
        this.z0 = z0;
        this.z1 = z1;
        this.k = k;
        material = mat;
    }

    /**
     * Metoda sprawdzajaca czy promien trafil w prostokat lezacy w osi XZ w scenie
     * @param r promien
     * @param t_min minimalna odleglosc trafienia
     * @param t_max maxymalna odleglosc trafienia
     * @param hitRecord informacje o trafieniu
     * @return czy promien trafil
     */
    @Override
    public boolean hit(Ray r, float t_min, float t_max, hit_record hitRecord) {

        float t = (k-r.getOrigin().getY())/r.getDirection().getY();
        if(t < t_min || t > t_max)
            return false;

        float x = r.getOrigin().getX() + t*r.getDirection().getX();
        float z = r.getOrigin().getZ() + t*r.getDirection().getZ();

        if(x < x0 || x > x1 || z < z0 || z > z1)
            return false;

        hitRecord.u = (x-x0)/(x1-x0);
        hitRecord.v = (z-z0)/(z1-z0);
        hitRecord.t = t;

        Vec3 outwardNormal = new Vec3(0,1,0);
        hitRecord.set_front_face(r,outwardNormal);
        hitRecord.material = material;
        hitRecord.p = r.at(t);
        return true;
    }
}
