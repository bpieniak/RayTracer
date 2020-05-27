package org.pt3k;

import org.pt3k.Aabb;
import org.pt3k.Ray;
import org.pt3k.hit_record;
import org.pt3k.shapes.hittable;

import java.util.*;

public class BVHnode implements hittable {

    hittable left;
    hittable right;
    Aabb box;

    public BVHnode() {}
    public BVHnode(Scene scene, float time0, float time1) {
        new BVHnode(scene.getList(), 0, scene.getList().size(), time0, time1);
    }

    public BVHnode(List<hittable> objects, int start, int end, float time0, float time1) {

        Random generator = new Random();

        int axis = generator.nextInt(3);

        if(axis == 0) {
            Collections.sort(objects, new BoxCompareX());
        } else if(axis == 1) {
            Collections.sort(objects, new BoxCompareY());
        } else if(axis == 2) {
            Collections.sort(objects, new BoxCompareZ());
        }

        int objectSpan = end - start;

        if(objectSpan == 1) {
            left = right = objects.get(start);
        }
    }


    @Override
    public boolean hit(Ray r, float t_min, float t_max, hit_record hitRecord) {
        if(!box.hit(r,t_min,t_max))
            return false;

        boolean hit_left = left.hit(r, t_min, t_max, hitRecord);
        boolean hit_right = right.hit(r, t_min, hit_left ? hitRecord.t : t_max, hitRecord);

        return hit_left || hit_right;
    }

    @Override
    public boolean boundingBox(float t0, float t1, Aabb outputBox) {
        outputBox = box;
        return true;
    }
}

class BoxCompareX implements Comparator {

    @Override
    public int compare(Object o, Object t1) {
        Aabb boxA = new Aabb();
        Aabb boxB = new Aabb();

        hittable a = (hittable) o;
        hittable b = (hittable) t1;

        if(!a.boundingBox(0,0,boxA) || !b.boundingBox(0,0,boxB))
            System.out.println("No bounding box in bvh_node constructor.");

        if(boxA.min.getElement(0) < boxB.min.getElement(0))
            return 1;
        else
            return -1;
    }
}

class BoxCompareY implements Comparator {

    @Override
    public int compare(Object o, Object t1) {
        Aabb boxA = new Aabb();
        Aabb boxB = new Aabb();

        hittable a = (hittable) o;
        hittable b = (hittable) t1;

        if(!a.boundingBox(0,0,boxA) || !b.boundingBox(0,0,boxB))
            System.out.println("No bounding box in bvh_node constructor.");

        if(boxA.min.getElement(1) < boxB.min.getElement(1))
            return 1;
        else
            return -1;
    }
}

class BoxCompareZ implements Comparator {

    @Override
    public int compare(Object o, Object t1) {
        Aabb boxA = new Aabb();
        Aabb boxB = new Aabb();

        hittable a = (hittable) o;
        hittable b = (hittable) t1;

        if(!a.boundingBox(0,0,boxA) || !b.boundingBox(0,0,boxB))
            System.out.println("No bounding box in bvh_node constructor.");

        if(boxA.min.getElement(2) < boxB.min.getElement(2))
            return 1;
        else
            return -1;
    }
}