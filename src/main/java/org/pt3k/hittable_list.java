package org.pt3k;

import java.util.Vector;

public class hittable_list implements hittable {

    hittable[] objects = new hittable[64];
    int count = 0;

    hittable_list() {};
    hittable_list(hittable object) { add(object); }

    void add(hittable object) {
        objects[count] = object;
        count++;
    }

    @Override
    public boolean hit(Ray r, float t_min, float t_max, hit_record hitRecord) {
         hit_record rec = new hit_record();
         boolean hit_anything = false;
         float closest = t_max;

         for(int i = 0; i < count; i++) {
             if(objects[i].hit(r,t_min,closest,rec)) {
                 hit_anything = true;
                 closest = rec.t;
                 hitRecord.normal = rec.normal;
                 hitRecord.t = rec.t;
                 hitRecord.front_face = rec.front_face;
                 hitRecord.p = rec.p;
             }
         }
         return hit_anything;
    }
}
