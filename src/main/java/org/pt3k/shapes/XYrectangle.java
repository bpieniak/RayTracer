package org.pt3k.shapes;

import org.pt3k.Ray;
import org.pt3k.Vec3;
import org.pt3k.hit_record;
import org.pt3k.materials.Material;

public class XYrectangle implements hittable{

    float x0, x1, y0, y1, k;
    Material material;

    public XYrectangle(float x0, float x1, float y0, float y1, float k, Material mat) {
        this.x0 = x0;
        this.x1 = x1;
        this.y0 = y0;
        this.y1 = y1;
        this.k = k;
        material = mat;
    }

    @Override
    public boolean hit(Ray r, float t_min, float t_max, hit_record hitRecord) {

        float t = (k-r.getOrigin().getZ())/r.getDirection().getZ();
        if(t < t_min || t > t_max)
            return false;

        float x = r.origin.getX() + t*r.getDirection().getX();
        float y = r.origin.getY() + t*r.getDirection().getY();

        if(x < x0 || x > x1 || y < y0 || y > y1)
            return false;

        hitRecord.u = (x-x0)/(x1-x0);
        hitRecord.v = (y-y0)/(y1-y0);
        hitRecord.t = t;

        Vec3 outwardNormal = new Vec3(0,0,1);
        hitRecord.set_front_face(r,outwardNormal);
        hitRecord.material = material;
        hitRecord.p = r.at(t);
        return true;
    }
}
