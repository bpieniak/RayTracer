package org.pt3k.shapes;

import org.pt3k.Ray;
import org.pt3k.HitRecord;
import org.pt3k.materials.Material;

import java.util.ArrayList;

public class Cube implements Hittable {

    private ArrayList<Hittable> walls;

    public Cube(float x0, float y0, float z0, float lengthX, float lenghtZ,float height, Material mat) {
        walls = new ArrayList<>();

        walls.add(new XZrectangle(x0,x0+lengthX,z0,z0+lenghtZ,y0, mat));
        walls.add(new XZrectangle(x0,x0+lengthX,z0,z0+lenghtZ,y0+height, mat));
        walls.add(new XYrectangle(x0,x0+lengthX,y0,y0+height,z0, mat));
        walls.add(new XYrectangle(x0,x0+lengthX,y0,y0+height,z0+lenghtZ, mat));
        walls.add(new YZrectangle(z0,z0+lenghtZ,y0,y0+height,x0, mat));
        walls.add(new YZrectangle(z0,z0+lenghtZ,y0,y0+height,x0+lengthX, mat));
    }

    @Override
    public boolean hit(Ray r, float t_min, float t_max, HitRecord hitRecord) {
        HitRecord rec = new HitRecord();
        boolean hit_anything = false;
        float closest = t_max;

        for(Hittable h : walls) {
            if(h.hit(r,t_min,closest,rec)) {
                hit_anything = true;
                closest = rec.t;
                hitRecord.normal = rec.normal;
                hitRecord.t = rec.t;
                hitRecord.front_face = rec.front_face;
                hitRecord.p = rec.p;
                hitRecord.material = rec.material;
                hitRecord.u = rec.u;
                hitRecord.v = rec.v;
            }
        }
        return hit_anything;
    }
}
