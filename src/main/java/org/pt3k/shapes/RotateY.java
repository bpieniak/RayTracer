package org.pt3k.shapes;

import org.pt3k.Ray;
import org.pt3k.Vec3;
import org.pt3k.HitRecord;

public class RotateY implements Hittable {

    private Hittable hittable;
    private double sinTheta;
    private double cosTheta;

    public RotateY(Hittable object, double angle) {
        hittable = object;

        double radians = Math.toRadians(angle);
        sinTheta = Math.sin(radians);
        cosTheta = Math.cos(radians);
    }

    @Override
    public boolean hit(Ray r, float t_min, float t_max, HitRecord hitRecord) {

        Vec3 origin;
        Vec3 direction;

        origin = new Vec3((float) (r.getOrigin().getX()*cosTheta - sinTheta*r.getOrigin().getZ()),
                r.getOrigin().getY(),
                (float) (r.getOrigin().getX()*sinTheta + cosTheta*r.getOrigin().getZ()));

        direction = new Vec3((float) (r.getDirection().getX()*cosTheta - sinTheta*r.getDirection().getZ()),
                r.getDirection().getY(),
                (float) (r.getDirection().getX()*sinTheta + cosTheta*r.getDirection().getZ()));

        Ray rotatedR = new Ray(origin,direction);

        if(!hittable.hit(rotatedR,t_min,t_max,hitRecord))
            return false;

        Vec3 p = new Vec3((float) (hitRecord.p.getX()*cosTheta - sinTheta*hitRecord.p.getZ()),
                hitRecord.p.getY(),
                (float) (hitRecord.p.getX()*sinTheta + cosTheta*hitRecord.p.getZ()));

        Vec3 normal = new Vec3((float) (hitRecord.normal.getX()*cosTheta - sinTheta*hitRecord.normal.getZ()),
                hitRecord.normal.getY(),
                (float) (hitRecord.normal.getX()*sinTheta + cosTheta*hitRecord.normal.getZ()));

        hitRecord.p = p;
        hitRecord.set_front_face(rotatedR,normal);

        return true;
    }
}
