package org.pt3k;

import java.util.List;

public class hittable_list implements hittable {

    public List<hittable> list;

    hittable_list(List<hittable> list) {
        this.list = list;
    }

    @Override
    public boolean hit(Ray r, float t_min, float t_max, hit_record hitRecord) {
         hit_record rec = new hit_record();
         boolean hit_anything = false;
         float closest = t_max;

         for(hittable h : list) {
             if(h.hit(r,t_min,closest,rec)) {
                 hit_anything = true;
                 closest = rec.t;
                 hitRecord.normal = rec.normal;
                 hitRecord.t = rec.t;
                 hitRecord.front_face = rec.front_face;
                 hitRecord.p = rec.p;
                 hitRecord.material = rec.material;
             }
         }
         return hit_anything;
    }
}
