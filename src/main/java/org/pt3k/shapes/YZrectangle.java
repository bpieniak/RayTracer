package org.pt3k.shapes;

import org.pt3k.Ray;
import org.pt3k.Vec3;
import org.pt3k.hit_record;
import org.pt3k.materials.Material;

public class YZrectangle implements hittable{

    float z0, z1, y0, y1, k;
    Material material;

    public YZrectangle(float z0, float z1, float y0, float y1, float k, Material mat) {
        this.z0 = z0;
        this.z1 = z1;
        this.y0 = y0;
        this.y1 = y1;
        this.k = k;
        material = mat;
    }

    @Override
    public boolean hit(Ray r, float t_min, float t_max, hit_record hitRecord) {

        float t = (k-r.getOrigin().getX())/r.getDirection().getX();
        if(t < t_min || t > t_max)
            return false;

        float y = r.origin.getY() + t*r.getDirection().getY();
        float z = r.origin.getZ() + t*r.getDirection().getZ();

        if(z < z0 || z > z1 || y < y0 || y > y1)
            return false;

        hitRecord.u = (z-z0)/(z1-z0);
        hitRecord.v = (y-y0)/(y1-y0);
        hitRecord.t = t;

        Vec3 outwardNormal = new Vec3(1,0,0);
        hitRecord.set_front_face(r,outwardNormal);
        hitRecord.material = material;
        hitRecord.p = r.at(t);
        return true;
    }
}
