package org.pt3k;

import org.pt3k.materials.*;
import org.pt3k.shapes.Sphere;
import org.pt3k.shapes.hittable;

import java.util.ArrayList;
import java.util.List;

public class Scene implements hittable {

    public List<hittable> list;

    Scene(List<hittable> list) {
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

    public static ArrayList<hittable> randomScene() {
        ArrayList<hittable> worldList = new ArrayList<>();

        Checker checker = new Checker(
                new SolidColor(0.2f,0.8f,0.2f),
                new SolidColor(0.9f,0.9f,0.9f)
        );

        worldList.add(new Sphere(new Vec3(0,-1000,0), 1000, new Lambertian(checker)));

        int i = 1;
        for(int a = -6; a < 6; a = a+2) {
            for(int b = -6; b < 6; b = b+2) {
                float choose_mat = (float) Math.random();
                Vec3 center = new Vec3((float )(a + 2*Math.random()), 0.2f, (float )(b + 2*Math.random()));

                if(choose_mat < 0.9) {
                    Vec3 albedo = MyRandom.randomVector().mulvec(MyRandom.randomVector());
                    worldList.add(new Sphere(center,0.2f,new Lambertian(new SolidColor(albedo))));
                } else if(choose_mat < 0.95) {
                    Vec3 albedo = MyRandom.randomVector();
                    worldList.add(new Sphere(center, 0.2f, new Metal(albedo)));
                } else {
                    worldList.add(new Sphere(center,0.2f, new Dielectric(1.5f)));
                }
            }
        }

        worldList.add(new Sphere(new Vec3(0,1,0),1,new Dielectric(1.5f)));
        worldList.add(new Sphere(new Vec3(-4,1,0),1,new Lambertian(new SolidColor(0.4f,0.2f,0.1f))));
        worldList.add(new Sphere(new Vec3(4,1,0),1,new Metal(new Vec3(0.7f,0.6f,0.5f))));

        return worldList;
    }
}
