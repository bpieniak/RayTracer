package org.pt3k;

import org.pt3k.material.Dielectric;
import org.pt3k.material.Lambertian;
import org.pt3k.material.Metal;
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

    @Override
    public boolean boundingBox(float t0, float t1, Aabb outputBox) {
        if(list.isEmpty())
            return false;

        Aabb temp_box = new Aabb();
        boolean first_box = true;

        for(hittable h : list) {
            if(!h.boundingBox(t0,t1,temp_box))
                return false;
            outputBox = first_box ? temp_box : surroundingBox(outputBox,temp_box);
            first_box = false;
        }
        return true;
    }

    private Aabb surroundingBox(Aabb box0, Aabb box1) {
        Vec3 small = new Vec3(Math.min(box0.min.getX(), box1.min.getX()),
                Math.min(box0.min.getY(), box1.min.getY()),
                Math.min(box0.min.getZ(), box1.min.getZ()));

        Vec3 big = new Vec3(Math.max(box0.max.getX(), box1.max.getX()),
                Math.max(box0.max.getY(), box1.max.getY()),
                Math.max(box0.max.getZ(), box1.max.getZ()));

        return new Aabb(small,big);
    }

    public List<hittable> getList() {
        return list;
    }

    public static ArrayList<hittable> randomScene() {
        ArrayList<hittable> worldList = new ArrayList<>();

        worldList.add(new Sphere(new Vec3(0,-1000,0), 1000, new Lambertian(new Vec3(0.5f,0.5f,0.5f))));

        int i = 1;
        for(int a = -11; a < 11; a++) {
            for(int b = -11; b < 11; b++) {
                float choose_mat = (float) Math.random();
                Vec3 center = new Vec3((float )(a + 0.9*Math.random()), 0.2f, (float )(b + 0.9*Math.random()));

                if(choose_mat < 0.9) {
                    Vec3 albedo = MyRandom.randomVector().mulvec(MyRandom.randomVector());
                    worldList.add(new Sphere(center,0.2f,new Lambertian(albedo)));
                } else if(choose_mat < 0.95) {
                    Vec3 albedo = MyRandom.randomVector();
                    worldList.add(new Sphere(center, 0.2f, new Metal(albedo)));
                } else {
                    worldList.add(new Sphere(center,0.2f, new Dielectric(1.5f)));
                }
            }
        }

        worldList.add(new Sphere(new Vec3(0,1,0),1,new Dielectric(1.5f)));
        worldList.add(new Sphere(new Vec3(-4,1,0),1,new Lambertian(new Vec3(0.4f,0.2f,0.1f))));
        worldList.add(new Sphere(new Vec3(4,1,0),1,new Metal(new Vec3(0.7f,0.6f,0.5f))));

        return worldList;
    }
}
